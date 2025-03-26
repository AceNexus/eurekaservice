package com.acenexus.tata.eurekaservice;

import com.netflix.discovery.EurekaClient;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EurekaShutdownHandler {

    private static final Logger logger = LoggerFactory.getLogger(EurekaShutdownHandler.class);

    private final EurekaClient eurekaClient;

    public EurekaShutdownHandler(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    @PreDestroy
    public void shutdown() {
        logger.info("Shutting down Eureka Client");
        eurekaClient.shutdown();
    }

}