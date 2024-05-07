
# EaseSearch

#### 介绍

EaseSearch是对外提供elasticsearch查询相关接口服务
#### 软件架构

* SpringBoot

* elasticsearch

#### 安装教程

##### 基本安装

1. 克隆工程
   > git clone https://gitee.com/opensourceway/EaseSearch.git

2. 打包方式
   > mvn clean install package -Dmaven.test.skip 

3. 启动应用
   > java -jar target/EaseSearch-0.0.1-SNAPSHOT.jar


##### 容器安装~~~~

1. 克隆工程
   > git clone https://gitee.com/opensourceway/EaseSearch.git


2. 打包方式
    * 用Docker打包（到EaseSearch-search目录中， 执行Dockerfile文件： docker build -t easesearch . ）
    
3. 启动应用
    * docker run -d -v /data/EaseSearch/application.yaml:/home/easysearch/application.yaml  -e APPLICATION_PATH=/home/easysearch/application.yaml -p 8080:8080 --name my-container my-image 
#### 使用说明
https://apifox.com/apidoc/shared-164da92c-abef-421f-ada1-65cb5d140452
