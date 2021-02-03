##SpringBoot

### SpringBoot自装配原理
 
 * 自定义Springboot的starter
        
        1. 创建一个普通的Maven项目(创建的项目不可以有启动类, 否则别的项目依赖时找不到类!!!)    
        2. pom文件引入Springboot自动装配的依赖
                <dependency>
                  <groupId>org.springframework.boot</groupId>
                  <artifactId>spring-boot-autoconfigure</artifactId>
                  <version>2.1.4.RELEASE</version>
                </dependency>
        3. resources目录下新建/META-INF/spring.factories,内容如下：
                org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
                com.tencent.config.LiuFengAutoConfig
           SPI机制,通过指定的目录装配bean,等号前面的K值不要修改,如果使用自定义的K，需要写自己的扫描逻辑。等号后面的V值为我们需要被spring加载的配置文件
           
        4. 定义我们starter的逻辑
                1. 定义配置文件LiuFengProperties, 用以支持从yml文件中设置属性, @ConfigurationProperties(prefix = "demo")，该文件此时不会被Spring加载
                2. 使用@EnableConfigurationProperties(LiuFengProperties.class)来使配置文件被spring管理
                3. 通过Configuration + @Bean的形式配置哪些Bean需要被spring管理,将该配置文件的全路径指定为API中的V值
                4. 我们通过Condition接口来判断这些bean的加载条件,如果用户自己配置了,就不需要自动装配
            
 * 自动装配原理       
          
        1. @SpringBootApplication中的@EnableAutoConfiguration注解引用了AutoConfigurationImportSelector类,该类提供了如下机制  
        1. Spring启动后会根据SPI机制扫描/META-INF/spring.factories的内容
        2. 加载需要自动装配的bean至Spring
        
        
### SpringBoot文件加载顺序

 * 文件加载顺序(定义在ConfigFileApplicationListener文件中)
        
        String DEFAULT_SEARCH_LOCATIONS = "classpath:/,classpath:/config/,file:./,file:./config/*/,file:./config/";
        
        文件的优先级由低到高,不同属性互补,相同属性高优先级进行替换。