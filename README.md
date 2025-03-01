# Eureka Service

## **📌 概述**

- Eureka 是 Spring Cloud Netflix 提供的服務註冊與發現工具，負責管理微服務的註冊與查詢

### **📄 相關文件**

- [Spring Guide - Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery)

---

## **🚀 部署步驟**

### **1️⃣ 建立資料夾**

在伺服器上，建立存放 `Eureka Service` 的專用資料夾

```shell
sudo mkdir -p /opt/tata/eurekaservice
sudo mkdir -p /opt/tata/app
sudo chown -R ubuntu:ubuntu /opt/tata/app
```

### **2️⃣ 部署 JAR 文件**

將 eurekaservice.jar 放入 /app/

### **3️⃣ 建立 Dockerfile**

在 /opt/tata/eurekaservice/ 目錄內，建立 Dockerfile

```text
sudo touch /opt/tata/eurekaservice/Dockerfile
sudo chown -R ubuntu:ubuntu /opt/tata/eurekaservice/Dockerfile
```

撰寫 Dockerfile

```text
# 使用 OpenJDK 21 slim 版，減少鏡像大小
FROM eclipse-temurin:21-jre-alpine

# 設定工作目錄
WORKDIR /app

# 確保 JAR 檔案存在
COPY eurekaserver-*.jar /app/

# 列出 /app/ 目錄，確認 JAR 是否成功複製
RUN ls -l /app/

# 找出最新的 JAR 檔案，並建立符號連結
RUN set -e && \
    latest_jar=$(ls -t /app/eurekaserver-*.jar 2>/dev/null | head -n1) && \
    if [ -z "$latest_jar" ]; then echo "No JAR file found!" && exit 1; fi && \
    echo "Latest JAR: $latest_jar" && \
    ln -s "$(basename "$latest_jar")" /app/eurekaservice.jar

# 設定啟動指令
CMD ["java", "-jar", "/app/eurekaservice.jar"]
```

### **4️⃣ 建構 Docker 映像檔**

```shell
cd /opt/tata/eurekaservice
docker build -t eurekaservice .
```

### **5️⃣ 啟動服務**

```shell
docker run -di --name=eurekaservice -p 8761:8761 eurekaservice
```

### **6️⃣ 確認服務啟動是否正常**
```shell
docker logs -f --tail 1000 eureka-service
```