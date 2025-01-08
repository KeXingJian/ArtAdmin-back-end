package com.kxj.artadmin;

import org.babyfish.jimmer.client.EnableImplicitApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableImplicitApi
public class ArtAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ArtAdminApplication.class, args);
    }

}
