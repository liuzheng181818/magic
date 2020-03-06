package com.liuz.magicCamera;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.liuz.magicCamera.dao")
public class MagicCameraApplication {

    public static void main(String[] args) {
        SpringApplication.run(MagicCameraApplication.class, args);
    }

}
