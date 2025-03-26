package com.acenexus.tata.eurekaservice;

import com.netflix.discovery.DiscoveryClient;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EurekaClientService {

    @Autowired
    private DiscoveryClient discoveryClient;

    @PreDestroy
    public void onDestroy() {
        // 確保在關閉前不再使用 client
        if (discoveryClient != null) {
            discoveryClient.shutdown();
        }
    }

}