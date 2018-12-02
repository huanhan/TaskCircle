package com.tc.web.controller;

import com.google.gson.Gson;
import com.tc.db.entity.User;
import com.tc.dto.LoginUser;
import com.tc.dto.RegisterUser;
import com.tc.dto.TokenResult;
import org.junit.Test;
import org.springframework.security.test.context.support.WithMockUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class UserControllerTest extends BasicControllerTest{

    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Test
    public void whenLoginSuccess() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/authentication/form")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .header("Authorization","Basic dGM6dGNzZWNyZXQ=")
                .param("username","test1234")
                .param("password","123456"))
                //.param("grant_type","password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        TokenResult token = new Gson().fromJson(result,TokenResult.class);
        accessToken = token.getAccessToken();
    }

    @Test
    @WithMockUser(username = "test1234")
    public void whenQueryMeSuccess() throws Exception {
//        MultiValueMap<String, String> maps = new LinkedMultiValueMap<>();
//        Map<String,String> map = new HashMap<>();
//        map.put("principal","test1234");
//        maps.put("authentication",map.);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new Ge("ROLE_ADMIN"));
        grantedAuthorities.add(new Ge("ROLE_USER"));
        Authentication authentication = new UsernamePasswordAuthenticationToken("test1234","123456",grantedAuthorities);
        User user = new User();
        user.setUsername("test1234");
        user.setEncoderPassword("123456");
        LoginUser loginUser = new LoginUser(user);
        String content = new Gson().toJson(loginUser);
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/user/me")
                .header("Authorization","bearer " + accessToken)
        .param("authentication","test1234"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test1234"))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenRegisterSuccess() throws Exception {
        RegisterUser registerUser = new RegisterUser("test1234","123456");
        String content = new Gson().toJson(registerUser);
        logger.info(content);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test1234"))
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenUpdateSuccess() throws Exception {
        User user = new User();
        user.setId(4L);
        user.setName("我是管理员");

        Date date = new Date(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        String content = new Gson().toJson(user);
        System.out.println(content);
        String result = mockMvc.perform(MockMvcRequestBuilders.put("/user/4")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + accessToken)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("我是管理员"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }



    class Ge implements GrantedAuthority{

        private String authority;

        public Ge(String authority) {
            this.authority = authority;
        }

        @Override
        public String getAuthority() {
            return authority;
        }
    }
}
