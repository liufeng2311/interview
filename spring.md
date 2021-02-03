##Spring面试题

###ApplicationEvent的使用场景
    
 * ApplicationEvent是什么
        
       ApplicationEvent在Spring框架中代表事件。是对JDK中EventObject的进一步封装。
       
 * 事件发布机制(三要素)
        
        事件: 表示需要发送的事件,本质就是一个类,包含一系列的信息。
        事件发布器: 表示发布事件的类,内部含有所有事件对应的事件监听器。
        事件监听器: 表示处理事件逻辑的类,具体处理事件
        
 * 示例
````java
package com.generate.event;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: liufeng
 * @Date: 2021/1/28
 * @desc
 */

//定义监听器
public class MyApplicationListener implements ApplicationListener<MyApplicationEvent> {

  @Override
  public void onApplicationEvent(MyApplicationEvent event) {
    System.out.println(event.getName());
  }

  //发布监听
  public static void main(String[] args) {
    ApplicationContext applicationContext = new AnnotationConfigApplicationContext(MyApplicationListener.class);
    MyApplicationEvent myApplicationEvent = new MyApplicationEvent("test event!", "刘峰");
    applicationContext.publishEvent(myApplicationEvent);
  }

}

//定义监听对象
class MyApplicationEvent extends ApplicationEvent {

  private String name;

  public MyApplicationEvent(Object source, String name) {
    super(source);
    this.name = name;
  }


  public String getName() {
    return name;
  }
}
````

 * 事件发布机制原理
        
        事件发布机制的底层其实是在发布器中维护了一个事件监听列表。当发布事件时,循环调用事件监听器的监听方法。
 
 * Spring中的实现
        
        ApplicationListener接口作为一个功能性标识接口。定义了两个重要内容。
        
            ApplicationEvent: 事件,表明该监听器监听那种事件。
            onApplicationEvent(E event): 处理逻辑,表明事件发生后应执行的处理逻辑。
        
        ApplicationEventMulticaster接口保存事件和其对应的所有事件监听器的对应列表。
        
        Spring将所有扫描到的监听器都保存在一个Set集合中,事件发生时通过事件类型找出符合的监听器,执行对应的监听方法。
###SpringAOP
 
 * AOP底层原理
        
        AOP的底层还是使用的动态代理机制,分为JDK的动态代理和CGlib的动态代理。但是AOP不是直接调用的他们。
        Spring通过ProxyFactory、Advisor、Advice来创建代理类,当然他们的底层还是调用的动态代理。
        
        @Aspect会经过BeanFactory的后置处理器和Bean的后置处理器解析为Advisor、Advice,最终生成代理类。
 
 * 用到了哪些设计模式
        
            1. 工厂模式
            2. 代理模式
            3. 适配器模式
            4. 创建者模式
            5. 策略模式
            6. 模板模式
            7. 责任链模式
 
 * 几种通知间的执行顺序
    
        1.前置通知(@Before)
            
            优先级只低于@Around,但高于其他的通知
            
        2.后置通知(@After)
            
            在方法执行结束且处理完异常或处理完返回值后。
            
        3.返回通知(@AfterReturning)
             
            返回@AfterThrowing等级一样,且只能存在一个,方法正常结束
            
        4.异常通知(@AfterThrowing)
            
            和@AfterReturning等级一样,且只能存在一个,方法异常结束
            
        4.环绕通知(@Around)
        
              优先级是最高的,即最早开始执行和最晚结束执行,环绕通知是最后进行AOP的
        
        正常执行流程结果：
            
            around before
            log before
            切面测试
            log doAfterReturning
            log after
            around after
            
        异常执行流程结果(发生异常后around after不执行)
        
            around before
            log before
            切面测试
            log doAfterThrowing
            log after
 
###FactoryBean和BeanFactory的区别 

 * BeanFactory
        
        BeanFactory是我们的bean工厂。
 
 * FactoryBean
        
        我们一般通过xml或者注解的方式配置需要配spring管理的bean,通过动态代理生成的bean该如何被Spring管理呢
        就是FactoryBean需要做的事。