# ZChat 网络聊天软件

**孙培林 3210102981**

## 配置要求

**jdk 17**

## 如何运行

### 1. 启动服务端

数据库目前部署在远程服务器上，因此无需修改数据库配置可以直接启动

服务器默认使用端口为2981，在启动前请确保该端口空闲，如需修改可以在Server类中修改

运行 Server 类，路径为 src/main/java/pers/spl/chatserver/Server.java

### 2. 启动客户端

运行 Client 类，路劲为 src/main/java/pers/spl/chatclient/Client.java