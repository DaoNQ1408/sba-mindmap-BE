package com.sbaproject.sbamindmap;

import com.sbaproject.sbamindmap.config.DotEnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class SbaMindmapApplication {

    public static void main(String[] args) {
        DotEnvConfig.loadEnv();
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        System.out.println("✅ Default JVM timezone set to: " + TimeZone.getDefault().getID());
        SpringApplication.run(SbaMindmapApplication.class, args);
    }

}
