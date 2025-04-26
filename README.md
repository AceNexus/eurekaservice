# Eureka Service

![Spring Boot Version](https://img.shields.io/badge/Spring%20Boot-3.4.3-brightgreen.svg)
![Spring Cloud Version](https://img.shields.io/badge/Spring%20Cloud-2024.0.0-blue.svg)
![Java Version](https://img.shields.io/badge/Java-21-orange.svg)
![License](https://img.shields.io/badge/License-MIT-lightgrey.svg)

## 概述

Eureka Service 是基於 Spring Cloud Netflix 的服務註冊與發現中心，提供微服務架構中的服務管理解決方案。主要特點包括：

- **服務註冊**：自動接收並記錄微服務實例的註冊
- **服務發現**：提供微服務之間的互相發現機制
- **健康監控**：定期檢查服務實例的健康狀態
- **高可用設計**：支持叢集部署確保穩定性
- **自我保護機制**：防止網路分區故障導致服務誤下線

## 技術堆疊

1. **框架**: Spring Boot 3.4.3
2. **服務發現**: Spring Cloud Netflix Eureka Server 2024.0.0
3. **Java 版本**: Java 21
4. **安全框架**: Spring Security
5. **監控**: Spring Boot Actuator
6. **構建工具**: Gradle (Kotlin DSL)
7. **版本控制**: 自動從 Git Tag 獲取版本號

## 系統架構

### 1. 訪問服務

透過 Web UI 或 RESTful API 存取服務註冊資訊，需先通過身份驗證。

```
http://{host}:8761/
```

- `{host}`: Eureka 服務所在主機位址（例如：127.0.0.1）

### 2. 健康檢查與監控

使用 Spring Boot Actuator 提供的健康檢查端點：

```
http://localhost:8761/actuator/health
```

## 快速入門

### Eureka 服務端初始化步驟

1. **添加依賴**：
   ```kotlin
   dependencies {
       implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-server")
   }
   ```

2. **配置 bootstrap.yml**：
   ```yaml
   server:
     port: 8761
   
   spring:
     application:
       name: eurekaservice
   
   eureka:
     client:
       register-with-eureka: false
       fetch-registry: false
     server:
       enable-self-preservation: true
   ```

3. **啟動 Eureka Server**：
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

1. **添加依賴**：
   ```kotlin
   dependencies {
       implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
   }
   ```

2. **配置 bootstrap.yml**：
   ```yaml
   spring:
     application:
       name: client-service
   
   eureka:
     client:
       service-url:
         defaultZone: http://localhost:8761/eureka/
   ```

3. **啟動微服務客戶端**：
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

## 部署指南

### 1. 自動部署 (GitHub Actions)

```bash
git checkout main
git pull --rebase origin main
git tag -a v0.0.1 -m "v0.0.1"
git push origin --tags
```

### 2. 手動部署

1. **建立資料夾**：
   ```shell
   sudo mkdir -p /opt/tata/eurekaservice
   ```

2. **放置 JAR 文件**：將 eurekaservice.jar 放入 /opt/tata/eurekaservice

3. **建立 Dockerfile**：
   ```shell
   sudo touch /opt/tata/eurekaservice/Dockerfile
   sudo chown -R ubuntu:ubuntu /opt/tata/eurekaservice/Dockerfile
   ```

4. **建構與啟動**：
   ```shell
   cd /opt/tata/eurekaservice
   docker build --no-cache -t eurekaservice .
   
   # 啟動容器（含環境變數設定）
   docker run -d --name=eurekaservice -p 8761:8761 \
   -e SERVER_HOST=127.0.0.1 \
   -e SERVER_PORT=8761 \
   -e SECURITY_USERNAME=admin \
   -e SECURITY_PASSWORD=password \
   -e SPRING_PROFILES_ACTIVE=prod \
   -e CONFIG_SERVER_USERNAME=admin \
   -e CONFIG_SERVER_PASSWORD=password \
   -e CONFIG_SERVER_URI=http://configservice:8888 \
   eurekaservice
   ```

5. **確認狀態**：
   ```shell
   docker logs -f --tail 1000 eurekaservice
   ```

## 版本管理

版本號採用語義化版本規範 (Semantic Versioning)：

- **MAJOR**: 不兼容的重大變更（例：1.0.0 → 2.0.0）
- **MINOR**: 新增功能，向下相容（例：1.1.0 → 1.2.0）
- **PATCH**: 修正錯誤，無功能變化（例：1.2.1 → 1.2.2）

## 常見問題

1. **服務無法註冊至 Eureka？**
  - 檢查網路連接
  - 驗證客戶端的 @EnableDiscoveryClient 註解是否添加

2. **服務列表顯示為空？**
  - 確認客戶端服務已成功啟動
  - 檢查服務健康狀態

## 參考資源

- [Spring Cloud Netflix Eureka 文檔](https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/)
- [Spring Guide - Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery)