package com.tc.web.controller;

import com.google.gson.Gson;
import com.tc.dto.TokenResult;
import jdk.nashorn.internal.parser.Token;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicControllerTest {

    protected String accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0MTIzNCIsInNjb3BlIjpbImFsbCIsInJlYWQiLCJ3cml0ZSJdLCJjb21wYW55IjoidGMiLCJleHAiOjE1NDM3NDI4ODIsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4sUk9MRV9VU0VSIl0sImp0aSI6IjkzOGYxMjM5LWQ2ZDktNGMyNi1hOGVmLTA5Mzk4MWZmOWY2NSIsImNsaWVudF9pZCI6InRjIn0.3Yeblc95kaETHPy3IhQK_v3P3Lc7mHdSMCCCqUcJ134";
    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

        //String content = "{\"username\":\"test1234\",\"password\":123456\",\"grant_type\":password\"}";





    }
}
