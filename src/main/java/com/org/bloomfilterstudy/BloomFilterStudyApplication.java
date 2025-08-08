package com.org.bloomfilterstudy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.org.bloomfilterstudy.mapper")
@SpringBootApplication
public class BloomFilterStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(BloomFilterStudyApplication.class, args);
    }

}
