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

- Git Flow 是一種結構化的 Git 工作流程，它提供完善的分支管理，確保開發、測試和正式環境的穩定性與可控性

### **1️⃣ 分支策略：精簡工作流程 vs. 完整工作流程**

| 團隊類型       | 分支名稱      | 角色   | 主要用途      | 合併來源                   | 合併目標      |
|------------|-----------|------|-----------|------------------------|-----------|
| **精簡工作流程** | `main`    | 正式環境 | 最終穩定版     | `develop`              | -         |
|            | `develop` | 開發環境 | 日常開發      | `feature/*`、`bugfix/*` | `main`    |
| **完整工作流程** | `main`    | 正式環境 | 最終穩定版     | `staging`              | -         |
|            | `staging` | 測試環境 | 測試開發完成的功能 | `develop`              | `main`    |
|            | `develop` | 開發環境 | 日常開發      | `feature/*`、`bugfix/*` | `staging` |

### **2️⃣ 精簡工作流程：日常開發流程**

```bash
# 1. 確保 develop 分支是最新的
git checkout develop
git pull

# 2. 創建功能分支
git checkout -b feature/新功能

# 3. 進行開發工作並提交更改
git add .
git commit -m "[feat] 實現某功能"

# 4. 推送功能分支
git push -u origin feature/新功能

# 5. 功能完成後，合併回 develop
git checkout develop
git pull
git merge feature/新功能
git push

# 6. 刪除功能分支
git branch -d feature/新功能
git push origin --delete feature/新功能
```

### **3️⃣ 精簡工作流程：測試流程**

```bash
# 1. 從 develop 部署到測試環境
git checkout develop
git pull

# 2. 在測試環境部署並進行測試
```

### **4️⃣ 精簡工作流程：部署到正式環境**

```bash
# 1. 測試通過後，將 develop 合併到 main
git checkout main
git pull

# 2. 合併開發環境內容
git merge develop

# 3. 為此版本打標籤
git tag -a v0.0.1 -m "版本 0.0.1"

# 4. 推送到遠程倉庫
git push
git push --tags
```

### **5️⃣ 精簡工作流程：緊急修復流程**

```bash
# 1. 從 main 創建緊急修復分支
git checkout main
git pull
git checkout -b hotfix/問題

# 2. 修復問題並提交
git add .
git commit -m "[fix] 修復問題"

# 3. 合併到 main 並打標籤
git checkout main
git pull
git merge hotfix/問題
git tag -a v0.0.2 -m "修復版本"
git push
git push --tags

# 4. 合併到 develop
git checkout develop
git pull
git merge hotfix/問題
git push

# 5. 刪除緊急修復分支
git branch -d hotfix/問題
git push origin --delete hotfix/問題
```

### **6️⃣ 什麼時候該更新版本號？**

| 版本變更      | 說明          | 例子              |
|-----------|-------------|-----------------|
| **MAJOR** | 破壞性變更，不相容舊版 | `1.0.0 → 2.0.0` |
| **MINOR** | 新增功能，向下相容   | `1.1.0 → 1.2.0` |
| **PATCH** | 修 bug，不影響功能 | `1.2.1 → 1.2.2` |

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

完整 Dockerfile 可參考 [doc/Dockerfile](doc/Dockerfile)

### **4️⃣ 建構 Docker 映像檔**

```shell
cd /opt/tata/eurekaservice
docker build --no-cache --progress=plain -t eurekaservice .
```

### **5️⃣ 啟動服務**

```shell
docker run -di --name=eurekaservice -p 8761:8761 eurekaservice
```

### **6️⃣ 確認服務啟動是否正常**

```shell
docker logs -f --tail 1000 eurekaservice
```