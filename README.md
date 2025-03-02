# Eureka Service

## **🔍 概述**

- Eureka 是 **Spring Cloud Netflix** 提供的 **服務註冊與發現** 工具，負責管理微服務的註冊與查詢
- 主要由 **Eureka Server**（註冊中心）與 **Eureka Client**（微服務）組成：
    - **Eureka Server**：提供微服務註冊與查詢機制，維護微服務的狀態
    - **Eureka Client**：將自己註冊到 Eureka Server，並能動態發現其他微服務

---

## **📚 參考資料**

### 🔹 官方文件

- [Spring Guide - Service Registration and Discovery](https://spring.io/guides/gs/service-registration-and-discovery)

### 🔹 相關截圖

- [**Spring Initializr（專案初始化）**](doc%2FSpringInitializr.png)
    - Gradle (Kotlin) DSL：提供更嚴謹的類型安全與 IDE 自動補全
    - [截至 2025/3/3，Spring Boot 穩定版為 3.4.3](doc%2FSpringBoot_3.4.3_Docs_20250303.png)
    - [截至 2025/3/3，最新 Java LTS 版本為 Java SE 21](doc%2FJava_LTS_Versions_20250303.png)

---

## **🌱 Git Flow 開發流程**

- Git Flow 是一種 Git 工作流程，提供結構化的分支管理，確保**開發**、**測試**、**正式**環境的**穩定**與**可控**

### **1️⃣ 分支**

- **main（正式環境）**
    - 最終穩定版的正式分支
    - **僅** 在版本穩定後，才會從 `staging` 合併，並標記版本（tag）
    - **不直接** 在此分支開發

- **staging（測試環境）**
    - 用於測試開發完成的功能
    - 功能開發完成後，從 `develop` 合併至 `staging` 進行測試
    - 測試通過後，才會合併到 `main` 進行正式部署

- **develop（開發環境）**
    - 日常開發的主要分支
    - **新功能開發**（`feature/功能名稱`）與 **錯誤修復**（`bugfix/問題描述`）皆從 `develop` 分支建立
    - 透過 **merge** 保持最新變更，確保開發內容同步

### **2️⃣ 日常開發流程**

```bash
# 1. 確保 develop 分支是最新的
git checkout develop
git pull origin develop

# 2. 創建功能分支
git checkout -b feature/新功能名稱

# 3. 進行開發工作並提交更改
git add .
git commit -m "[feat] 實現某功能"

# 4. 與 develop 同步，避免合併衝突
git checkout develop
git pull origin develop
git checkout feature/新功能名稱
git merge develop

# 5. 解決衝突後推送功能分支
git push -u origin feature/新功能名稱

# 6. 功能完成後，合併回 develop
git checkout develop
git pull origin develop
git merge --no-ff feature/新功能名稱
git push origin develop

# 7. 刪除功能分支
git branch -d feature/新功能名稱
git push origin --delete feature/新功能名稱
```

### **3️⃣ 準備測試流程**

```bash
# 1. 將 develop 合併到 staging 進行測試
git checkout staging
git pull origin staging

# 2. 合併開發分支內容
git merge develop

# 3. 解決衝突並推送
git push origin staging

# 4. 在測試環境部署並進行測試
```

### **4️⃣ 部署到正式環境**

```bash
# 1. 測試通過後，將 staging 合併到 main
git checkout main
git pull origin main

# 2. 合併測試環境內容
git merge staging

# 3. 為此版本打標籤
git tag -a v0.0.1 -m "版本 0.0.1 發布"

# 4. 推送到遠程倉庫
git push origin main
git push origin --tags
```

### **5️⃣ 緊急修復流程**

```bash
# 1. 從 main 創建緊急修復分支
git checkout main
git pull origin main
git checkout -b hotfix/緊急問題

# 2. 修復問題並提交
git add .
git commit -m "[fix] 修復某緊急問題"

# 3. 合併到 main 並打標籤
git checkout main
git pull origin main
git merge --no-ff hotfix/緊急問題
git tag -a v0.0.1 -m "修復版本 0.0.1"
git push origin main --tags

# 4. 合併到 develop 確保修復也在開發版本中
git checkout develop
git pull origin develop
git merge --no-ff hotfix/緊急問題
git push origin develop

# 5. 同步到 staging
git checkout staging
git pull origin staging
git merge --no-ff hotfix/緊急問題
git push origin staging

# 6. 刪除緊急修復分支
git branch -d hotfix/緊急問題
git push origin --delete hotfix/緊急問題
```

## **🚀 部署步驟**

### **1️⃣ 建立資料夾**

在伺服器上，建立存放 `Eureka Service` 的專用資料夾

```shell
sudo mkdir -p /opt/tata/eurekaservice
```

### **2️⃣ 部署 JAR 文件**

將 eurekaservice.jar 放入 /opt/tata/eurekaservice

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

# 將 JAR 檔案複製到容器中
COPY eurekaserver-*.jar /app/

# 列出 /app/ 目錄，確認 JAR 是否成功複製
RUN ls -la /app/

# 找出最新的 JAR 檔案，並建立符號連結
RUN set -e && \
    latest_jar=$(ls -t /app/eurekaserver-*.jar | head -n1) && \
    echo "Latest JAR: $latest_jar" && \
    ln -sf "$latest_jar" /app/eurekaservice.jar && \
    echo "Created symlink to $latest_jar as eurekaservice.jar"

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
docker logs -f --tail 1000 eurekaservice
```