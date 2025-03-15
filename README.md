# Eureka Service

## 概述

基於 **Spring Cloud Netflix**，提供 **服務註冊與發現** 功能，負責管理微服務的註冊、狀態管理與服務查詢。

## 快速入門

### Eureka 微服務初始化步驟

#### 1. 配置 `build.gradle.kts`

```kotlin
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
}
```

#### 2. 配置 `bootstrap.yml`

```yaml
server:
  port: 8761

spring:
  application:
    name: eureka-server

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
  server:
    enable-self-preservation: true
```

#### 3. 啟動 Eureka Server：在 Spring Boot 主應用類上添加 `@EnableEurekaServer` 註解

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

### Eureka 客戶端初始化步驟

#### 1. 配置 `build.gradle.kts`

```kotlin
dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
}
```

#### 2. 配置 `bootstrap.yml`

```yaml
spring:
  application:
    name: my-service

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### 3. 啟動微服務客戶端：在 Spring Boot 主應用類上添加 `@EnableEurekaClient` 註解

```java
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class MyServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyServiceApplication.class, args);
    }
}
```

## 參考資源

- [Spring Guide - Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery)
- 使用 [Spring Boot 3.4.3](doc%2FSpringBoot_3.4.3_Docs_20250303.png)
  與 [Java SE 21 (LTS)](doc%2FJava_LTS_Versions_20250303.png)