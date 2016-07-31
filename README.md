# netty-im

###  协议包:
- packetLen
- version
- commandId
- seqId
- shellId
- body

### 实现功能

1. 登陆认证
2. 单聊消息
3. 群聊消息
4. 心跳检测（客户端）
5. 心跳检测（服务端）
6. 客户端重连

### 未实现功能

1. 消息队列机制（Redis or MQ）
2. 日志机制（MongoDB）

### How do I use it?
#### Server 

- mvn clean install
- java -jar netty-im-0.1.jar

#### Client

- ClientA.java ： 认证消息、单聊信息、群聊消息、心跳包
- ClientB.java ： 认证消息、单聊信息、群聊消息、重连机制
- ClientC.java ： 认证消息、单聊信息、群聊消息
    
### Recent Releases

31-July-2016 - version 0.1 released

First stable release.