package com.tc.web.controller;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BasicControllerTest {

    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJ0ZXN0MTIzNCIsInNjb3BlIjpbImFsbCIsInJlYWQiLCJ3cml0ZSJdLCJjb21wYW55IjoidGMiLCJleHAiOjE1NDM2NTgxNjAsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4sUk9MRV9VU0VSIl0sImp0aSI6ImJlYzQxNTQ3LTkzNTUtNDc4MC1iMTJlLTU4ODVhMmQwOTkxYiIsImNsaWVudF9pZCI6InRjIn0.hA_SPNqmi7gvMVXKaQ97gJsyYw-jY1PIUojohkG1vh8";

    @Autowired
    private WebApplicationContext wac;

    protected MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
}
