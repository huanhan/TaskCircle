package com.tc.web.controller;

import com.google.gson.Gson;
import com.tc.db.entity.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;


public class UrlResourceControllerTest extends BasicControllerTest{
    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Test
    public void whenAddSuccess() throws Exception {


        Authentication authentication = new UsernamePasswordAuthenticationToken("test1234","123456");
        String authenticationStr = new Gson().toJson(authentication);
        logger.info(authenticationStr);
        Resource resource = new Resource();
        resource.setType("GET");
        resource.setPath("/user/me");
        resource.setMethod("me");
        resource.setClassName("UserController");
        resource.setName("获取本人详情信息");
        String content = new Gson().toJson(resource);
        logger.info(content);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/resource")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + accessToken)
                .content(content).content(authenticationStr))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenDeleteAllSuccess() throws Exception {

        List<Long> lists = new ArrayList<>();
        lists.add(2L);
        String content = new Gson().toJson(lists);
        logger.info(content);

        String result = mockMvc.perform(MockMvcRequestBuilders.delete("/resource/all")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + accessToken)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenGetDetailSuccess() throws Exception {
        String result = mockMvc.perform(MockMvcRequestBuilders.get("/resource/3")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }
}
