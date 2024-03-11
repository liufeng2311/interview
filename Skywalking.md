## Skywalking文档

该文档主要用于记录skywalking服务搭建的过程, 以及搭建期间遇到的问题。具体包括如下几部分：
1. Skywalking服务端搭建(OAP)
2. Skywalking客户端搭建(Agent)
3. 数据上报(Kafka)
4. 数据存储(ElasticSearch)
5. Skywalking客户端支持自定义TraceId(Java)

### Skywalking服务端搭建(OAP)

 * 
### Skywalking客户端搭建(Agent)

 *
### 数据上报(Kafka)

 *
### 数据存储(ElasticSearch)

 *
### Skywalking客户端支持自定义TraceId(Java)

* 一、安装JDK17+

      客户端编译需要JDK的版本必须在17以上, 不然编译会报错

* 二、下载skywalking-java源码 

      https://github.com/apache/skywalking-java
* 三、选择我们需要编译的分支(以v8.16.0为例)

      git checkout v8.16.0
* 四、编译源码(根目录执行)

      ./mvnw clean package -Pall
* 五、验证编译结果

      编译成功后会生成skywalking-agent文件, 我们启动项目指定该目录下的skywalking-agent.jar, 观察数据是否可以正常上报。
      如果我们使用的是IntelliJ IDE工具, 需要设置
      apm-protocol/apm-network/target/generated-sources/protobuf文件为Generated Source Codes

* 六、自定义TraceId
      
      我们往往希望链路追踪中的traceId使用我们业务中的唯一标识, 但Skywalking官方并未提供相关实现, 好在提供了编译源码的支持,
      我们只需要修改生成traceId相关源码再编译就可以实现自定义traceId。
      
      在实际业务中, 我们想打通前端到后端的整体链路, 我们希望后端系统使用前端传过来的traceId, 因此我们希望在tomcat中拦截并
      初始化skywalking中traceId的值,具体的实现类在TomcatInvokeInterceptor中。
      
      

