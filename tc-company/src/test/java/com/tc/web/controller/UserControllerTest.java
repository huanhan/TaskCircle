package com.tc.web.controller;

import com.google.gson.Gson;
import com.tc.db.entity.User;
import com.tc.dto.RegisterUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class UserControllerTest extends BasicControllerTest{

    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);



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
                .header("Authorization","bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0MTIzNCIsInNjb3BlIjpbImFsbCIsInJlYWQiLCJ3cml0ZSJdLCJjb21wYW55IjoidGMiLCJleHAiOjE1NDMzMjcwOTcsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4sUk9MRV9VU0VSIl0sImp0aSI6IjFlNzk4YTZiLTRlNjctNDBmYS05N2FlLTIyY2UyYTAxZDNhNiIsImNsaWVudF9pZCI6InRjIn0.sNiKSgch0hgzpic3YqCwvS3ZIAiR2SgynmbazNGiMAU")
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("我是管理员"))
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}
