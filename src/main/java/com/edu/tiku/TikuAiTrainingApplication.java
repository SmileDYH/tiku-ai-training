package com.edu.tiku;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.edu.tiku.mapper")
public class TikuAiTrainingApplication {

    public static void main(String[] args) {
        SpringApplication.run(TikuAiTrainingApplication.class, args);
    }

}
