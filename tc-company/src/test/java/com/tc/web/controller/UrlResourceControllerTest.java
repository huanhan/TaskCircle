package com.tc.web.controller;

import com.google.gson.Gson;
import com.tc.db.entity.Resource;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


public class UrlResourceControllerTest extends BasicControllerTest{
    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Test
    public void whenAddSuccess() throws Exception {
        Resource resource = new Resource();
        resource.setType("GET");
        resource.setPath("/user/me");
        resource.setMethod("me");
        resource.setClassName("UserController");
        resource.setName("获取本人详情信息");
        String content = new Gson().toJson(resource);
        System.out.println(content);
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/resource")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + TOKEN)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andReturn().getResponse().getContentAsString();
        System.out.println(result);
    }
}
