package com.acenexus.tata.eurekaservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableEurekaServer
public class EurekaserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaserviceApplication.class, args);
    }

    @Component
    static class EnvironmentReporter implements ApplicationRunner {

        private static final Logger logger = LoggerFactory.getLogger(EnvironmentReporter.class);

        @Autowired
        private Environment env;

        @Override
        public void run(ApplicationArguments args) {
            String[] activeProfiles = env.getActiveProfiles();
            String activeProfile = activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default";
            logger.info("==================================================");
            logger.info("Current active environment: {}", activeProfile);
            logger.info("Server port: {}", env.getProperty("server.port"));
            logger.info("==================================================");
        }
    }

}
