# 使用 OpenJDK 21（Alpine 版）作為基礎映像，體積較小且適合容器化部署
FROM eclipse-temurin:21-jre-alpine

# 設定工作目錄為 /app
WORKDIR /app

# 複製編譯後的 JAR 檔案到容器內
COPY eurekaservice-*.jar /app/

# 確保 JAR 檔案已成功複製
RUN ls -la /app/ && ls -la /app/configservice-*.jar || echo "No JAR found!"

# 建立指向最新 JAR 檔案的符號連結，確保一致的啟動方式
RUN set -e && \
    latest_jar=$(ls -t /app/eurekaservice-*.jar | head -n1) && \
    echo "Latest JAR: $latest_jar" && \
    ln -sf "$latest_jar" /app/eurekaservice.jar

# 設定 Spring Boot 啟動的環境變數，使用 "develop" 環境
ENV SPRING_PROFILES_ACTIVE=develop

# HOST 設定應用程式的主機位址
ENV HOST=127.0.0.1

# 以 Java 啟動應用程式
CMD ["java", "-jar", "/app/eurekaservice.jar"]