# SmallDubbo

基本流程完成，供学习使用



## 简介

SmallDubbo完成了RPC的基本功能，可以实现基于properties文件的调用端，服务提供端，注册中心配置，其中包括一些netty，并发，zookeeper连接，序列化，spring Bean配置，负载均衡的基础知识

## 配置介绍

#### **Zk.properties**



```
# 注册中心ZK地址
zookeeper.address=localhost:2181
# session超时时间
zookeeper.session.timeout=3000
# 连接超时时间
zookeeper.connection.timeout=3000
# 客户端对每个主机的初始化Channel数量
client.channelPoolSize=10
# 客户端调用RPC服务线程池的线程数
client.threadWorkers=100
# 服务端序列化协议,默认: Hessian.可选值:ProtoStuff / Hessian
server.serializer=Hessian
```

- zookeeper.address：zookeeper服务地址，在zookeeper下的conf文件可以找到，默认是2181端口
- zookeeper.session.timeout ：建立session超时时间
- zookeeper.connection.timeout 建立连接超时时间
- client.channelPoolSize ：客户端对每个ip：pot的初始化Channel数量
- client.threadWorkers：客户端调用RPC服务线程池的线程数
- server.serializer：客户端和服务端的序列化协议



#### **invoker.properties**

```
#invoker唯一id
id=1
#调用app名称
appName=test
#调用接口名称
interface=test.inter.Person
#负载均衡算法
clusterStrategy=WeightRandom
#超时
timeout=2000
```

- id:invoker唯一id
- appName:调用app名称
- interface:调用接口名称
- clusterStrategy:负载均衡算法
- timeout:超时



#### **service.properties**



```
#服务唯一ID
id=1
#服务接口名称
interface=test.inter.Person
#服务调用BeaniD
ref=sayImpl
#单个服务的最大负载连接
workerThreads=100
#服务端口
serverPort=8081
#服务app名称
appName=test
```



- id:invoker唯一id
- appName:调用app名称
- interface:调用接口名称
- workerThreads:单个服务的最大负载连接
- ref:服务调用BeaniD
- serverPort :服务端口



#### service.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">
<!--    服务实际接口实现-->
    <bean id="sayImpl" class="test.Impl.man"></bean>
</beans>
```

接口实现类的配置



测试用例在Java/test文件夹下，SmallDubbo的具体实现过程在[这里]()





