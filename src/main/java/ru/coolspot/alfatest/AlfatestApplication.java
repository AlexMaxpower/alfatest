package ru.coolspot.alfatest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AlfatestApplication {

    public static void main(String[] args) {
        SpringApplication.run(AlfatestApplication.class, args);
    }

}
