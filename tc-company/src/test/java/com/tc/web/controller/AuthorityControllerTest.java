package com.tc.web.controller;

import com.google.gson.Gson;
import com.tc.dto.Ids;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;


public class AuthorityControllerTest extends BasicControllerTest {
    private Logger logger = LoggerFactory.getLogger(UserControllerTest.class);

    @Test
    public void whenDeleteSuccess() throws Exception {


        String content = new Gson().toJson(3);
        logger.info(content);

        String result = mockMvc.perform(MockMvcRequestBuilders.delete("/authority")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + accessToken)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }

    @Test
    public void whenDeleteAllSuccess() throws Exception {

        List<Long> del = new ArrayList<>();
        del.add(5L);
        del.add(4L);

        Ids ids = new Ids();
        ids.setlIds(del);

        String content = new Gson().toJson(ids);
        logger.info(content);

        String result = mockMvc.perform(MockMvcRequestBuilders.delete("/authority/all")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .header("Authorization","bearer " + accessToken)
                .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        logger.info(result);
    }
}
