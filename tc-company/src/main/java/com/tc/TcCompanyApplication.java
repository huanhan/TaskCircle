package com.tc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
public class TcCompanyApplication {
    public static void main(String[] args){
        SpringApplication.run(TcCompanyApplication.class);
    }
}
