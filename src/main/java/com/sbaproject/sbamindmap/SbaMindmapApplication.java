package com.sbaproject.sbamindmap;

import com.sbaproject.sbamindmap.config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SbaMindmapApplication {

    public static void main(String[] args) {
        DotEnvConfig.loadEnv();
        SpringApplication.run(SbaMindmapApplication.class, args);
    }

}
