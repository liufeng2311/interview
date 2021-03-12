##JDK
   
###JDK序列化方式(https://blog.csdn.net/hjl21/article/details/86519426)
    
 * JDK中如何实现对象的序列化
    
      需要实例化的类必须实现Serializable接口和Externalizable接口。
      ObjectOutputStream.writeObject(obj)负责把对象序列化为流。
      ObjectInputStream.readObject()负责把流反序列化为对象。
      
      我们可以将流输出到文件中,也可以将流保存在内存中
      文件：FileOutputStream fileOutputStream = new FileOutputStream("D://test.txt");
      内存：ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    
 * Serializable和Externalizable的区别
    
      Serializable默认所有字段都会序列化。
      Externalizable需要指定序列化哪些字段。重写如下两个方法指定
      writeExternal()指定序列化哪些字段。
      readExternal()指定反序列化哪些字段。
    
 * serialVersionUID的作用
    
      serialVersionUID用来确定版本,反序列化时可以通过该字段的信息来验证对象的实体类是否被改变了。
      
 * Serializable如何自定义序列化方式
        
        重写如下两个方法
        
          private void writeObject(ObjectOutputStream stream) throws IOException {
            stream.defaultWriteObject(); //默认序列化非static和transient字段
            stream.writeObject(names); //自定义序列化transient字段
          }
        
          private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
            stream.defaultReadObject(); //默认反序列化非static和transient字段
            name = (String) stream.readObject(); //自定义反序列化transient字段
            names = (String) stream.readObject(); //自定义反序列化static字段
          }
  
 * 代码
        
        SerializableDemo类和SerializableDemo1类 
        
###JDK事件监听机制
 
 * 监听机制
        
        监听机制是观察者模式的一种实现。主要分为监听事件、监听器、事件发布器。   
        详情参考Spring的监听机制实现
    
###JDK动态代理
 
 * 什么是动态代理
        
        动态代理指的是根据一组接口或者一组实现类,在程序运行时动态的添加额外的逻辑。
        动态代理不改变我们原有的接口或者类,只是生成了额外的代码逻辑
        
 * 注解的作用
        
         注解仅仅代表一个标识,没有什么具体的意义,我们可以将注解理解为一个特殊的接口。
         注解主要在生成代理类时起作用,通过提前定义好的逻辑(InvocationHandler)对含有指定注解的代码进行逻辑强化
         
 * 如何实现jdk动态代理
        
        jdk动态代理的本质是生成新的字节码文件用于加强原有的类并重新加载至JVM。
        设计到两个重要的类：
        Proxy：用于生成新的字节码并加载至JVM的
        InvocationHandler：加强的逻辑,代理类方法的调用都会执行到该类中的invoke方法。
        InvocationHandler.invoke()采用反射的方式调用对应的方法。
        
 
 * 切面的实现原理
 
        通过beanFactory的后置处理器处理@Aspect标记的类,生成Advisor(切面)和Aadvice(通知)
        通过bean的后置处理器增强类
        
        JdkDynamicAopProxy类实现了InvocationHandler接口
        
        @Aspect解析
            
            https://blog.csdn.net/supzhili/article/details/98401855
            
        ProxyFactory:代理对象生产工厂,返回值分为如下两种：
            CglibAopProxy:      Cglib
            JdkDynamicAopProxy: jdk
        
        CglibAopProxy和JdkDynamicAopProxy都实现了InvocationHandler接口,
        他们内部创建通过自己的逻辑实现代理类的生成
            
 * 编程式创建切面
 
````java
package com.generate.reset;

import org.aopalliance.aop.Advice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.PointcutAdvisor;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.NameMatchMethodPointcut;

/**
 * @Author: liufeng
 * @Date: 2021/1/27
 * @desc
 */
public class MyAdvisor implements PointcutAdvisor {

  //切点
  @Override
  public Pointcut getPointcut() {
    NameMatchMethodPointcut methodPointcut = new NameMatchMethodPointcut();
    methodPointcut.addMethodName("test");
    return methodPointcut;
  }

  //通知
  @Override
  public Advice getAdvice() {
    MethodBeforeAdvice methodBeforeAdvice = (method, args, target) -> System.out.println("方法执行前");
    return methodBeforeAdvice;
  }

  @Override
  public boolean isPerInstance() {
    return false;
  }
}

class UserService {

  public static void main(String[] args) {
    ProxyFactory factory = new ProxyFactory();
    factory.setTarget(new UserService());
    factory.addAdvisor(new MyAdvisor());
    UserService userService = (UserService) factory.getProxy();
    userService.test();
  }

  public void test() {
    System.out.println("111");
  }
}


````
            
 * 实现AOP
        
        1. BeanFactoryPostProcessor: Bean工厂的后置处理器,作用于beanFactory初始化后和bean实例化前之间
        
            我们可以扫描我们自定义的注解并做相关的处理。
            @Aspect修饰的类就是通过BeanFactory的后置处理器处理切面的
        2. BeanPostProcessor: bean实例化之后放如IOC容器之前执行
            
            我们可以通过定义InvocationHandler生成类的代理类并返回
            Aop就是通过bean的后置处理器将@aspect的逻辑织入的
        3. ImportSelector: 自定义将哪些类交于spring管理,等同于@Import
            
            jar包一般都是通过这种形式指定哪些bean需要被Spring管理的
            
            
 * AOP设计那些设计模式(见设计模式)
    
    1. 工厂模式
    2. 代理模式
    3. 适配器模式
    4. 创建者模式
    5. 策略模式
    6. 模板模式
    7. 责任链模式
    
###ThreadLocal的原理

 * ThreadLocal如何实现线程安全的
        
        ThreadLocal是通过空间换时间的方式来保证线程安全的。
        每个线程都存在一个threadLocals属性,
        该属性是一个Map类型,K为ThreadLocal对象本身,V为我们保存的值。
        
 * ThreadLocalMap的K,V如何设计的
        
        不同于HashMap,ThreadLocalMap中的K继承了WeakReference类,为一个弱引用。
        只存在弱引用对象在GC时后会被回收掉。
        
        什么场景下ThreadLocal的强引用会被回收。
 
 * ThreadLocal会存在什么问题
        
        1. 当ThreadLocal对象的强引用被回收后, ThreadLocalMap中的弱引用会在GC时被回收,
           此时对应的V是无法访问的,如果线程一直不回收就会造成泄漏。
        2. 当使用线程池时,该属性使用不当可能会造成逻辑错误
           同时如果不清理的话因为线程不会被回收,也会造成内存泄漏。
        
###Switch的用法
 
 * switch关键字可以使用那些类型
        
        byte、char、short、int、enum、String、Byte、Character、Short、Integer
    
 * 原理
        
        switch关键字的底层只支持int类型, 目前switch支持的数据类型都可以转化为int,只是一种语法糖。
        byte、char、short都可以换为int类型。
        枚举类型是通过枚举的顺序来表示int类型。
        String类型的hashcode返回的是int类型。  
        基本类型的封装类拆包后为基本类型。
        
###Java中的锁
 
 * Java中锁的类型(两类)

        1. synchronized锁(jdk的内置关键字)
        2. 基于AQS和Lock实现的锁(java代码实现)
        
 * synchronized的用法
        
        1. synchronized锁的底层原理由JVM实现, 锁的是对象, 锁的信息保存在对象头中。对象包括堆上的实例对象和方法区中的类对象两种。
        2. 该关键字主要用于方法上和代码块上。
        
 * 基于AQS和Lock实现的锁
       
       Lock的底层是通过AQS实现的。
       AQS的底层是通过一个state变量、一个当前锁的持有线程exclusiveOwnerThread变量、一个基于Node的双向数组来实现。
       底层通过LockSupport的park()、unpark()方法来进行通信。
       
 * AQS的常用实现
        
        CountDownLatch、Semaphore、CyclicBarrier、Condition、ReentrantLock、ReentrantReadWriteLock
      
 * 公平锁和非公平锁
        
        公平锁获取锁时必须排队。
        非公平锁获取锁时在排队时发现此时锁如果处于空闲状态就可以去获取, 获取不到再进行排队。
        
###volatile关键字
 
 * volatile关键字
        
        volatile定义了两层语义：
        可见性：线程使用 volatile修饰的变量时,需要从主存中再次加载该变量,而不是直接使用工作内存中的
        防止重排序：普通的变量只能保证方法执行过程中所有依赖结果的地方都是正确的结果,而不能保证变量的赋值过程和代码一样
        volitile提供了内存屏障保证后面的语句不会重排序到该屏障前面
        
        使用双重检测机制创建单利模式的,必须使用volatile修饰,原因如下
        Object obj = new Object();
        上述代码其实包含三步:  
        1. Object = allocate();    //分配对象的内存空间
        2. initInstance(Object);   //初始化对象
        3. obj = Object;           //将内存分配给引用
        
        上述步骤编译期可能将顺序优化为1、3、2,因为第一步已经取到Object,直接赋值给obj在编译期看来是正确的,所以我们需要使用volatile修饰
        
 * volatile修饰引用类型和数组
        
        当volatile修饰引用类型和数组时,只能保证引用可见性,并不能保证属性的可见性。
        当我们需要获取引用类型或者数组的具体值时,通过Unsafe类中getObjectVolatile(Object o, long offset)从主存中拿到最新值
        
 * volatile的底层实现(内存屏障)
        
        编译器在生成字节码时, 会在每个使用volatile变量的位置都添加上内存屏障, 具体如下：
        
        在每个volatile写操作的前面插入一个StoreStore屏障。
        在每个volatile写操作的后面插入一个StoreLoad屏障。
        在每个volatile读操作的前面插入一个LoadLoad屏障。
        在每个volatile读操作的后面插入一个LoadStore屏障。
        
        这些内存屏障保证了变量在使用时必须从主存中进行加载,使用后必须写入主存,类似原子性操作
        同时指定了只有两个屏障间的代码可以重排序,不能跨屏障重排序

###线程池(ThreadPoolExecutor)
 
 * 原理
 
         添加至线程池中的任务都不会创建线程, 线程池中的线程都是由参数中的线程工厂去创建 
         
 * 创建线程池的方式
       
         Executors.newSingleThreadExecutor();     创建只有一个线程的线程池(无界队列)
         Executors.newScheduledThreadPool(2);     创建定时任务的线程池(无界线程数)
         Executors.newFixedThreadPool(2);         创建固定大小的线程池(无界队列)
         Executors.newCachedThreadPool();         创建一个同步线程池(无界线程数)
         
 * 参数   
         
         corePoolSize    核心线程数(核心线程数空闲时不会被回收,但可以通过参数allowCoreThreadTimeOut设置为可回收)
         maximumPoolSize 最大线程数
         keepAliveTime   非核心线程数存活时间
         unit            keepAliveTime所设置时间的单位
         workQueue       阻塞队列,存放任务体
         threadFactory   线程工厂,线程池中的线程都是由该工厂创建的
         handler         拒绝策略,当任务无法执行时采用的策略             
         
 * 拒绝策略
         
         CallerRunsPolicy         由调用者线程执行        
         AbortPolicy              抛出异常(默认拒绝策略)        
         DiscardPolicy            不做任何处理       
         DiscardOldestPolicy      丢弃最早的任务     
         
 * 阻塞队列
         
         LinkedBlockingQueue      列表无界队列, 引发OOM, 内部由
         SynchronousQueue         同步队列, 引发OOM和CPU100%
         ArrayBlockingQueue       数组有界队列, 推荐使用
         DelayedWorkQueue         延迟队列, 引发OOM和CPU100%
         
 * 返回值任务的执行       
         
         封装为Call接口且实现Future接口,实现类通过保存任务的执行状态和保存获取该任务的结果的线程链来实现,依旧采用的阻塞和唤醒
         
 * 如何实现一个定时任务线程池
         
         定时任务使用的是延迟队列,使用的大顶堆实现
         
###多线程通讯
 
 * 方式一: wait()、notify()、notifyAll()
        
        三个方法都是Object类中的非静态方法, 只能通过实例对象来调用。
        必须获取实例的锁synchronized时才可以调用这三个方法(类实例对象不能调用这三个方法)。
        notify()唤醒是随机的唤醒一个线程,只能唤醒指定的线程。
        作用在锁上面而不是线程上面。
        阻塞和唤醒必须顺序执行, 如果先唤醒再阻塞的话会造成阻塞。
        
 * 方式二：park()和unpark(Thread thread)
        
        两个方法是LockSupport类中的静态方法,通过JNI实现。
        作用在线程上而不是锁上面。阻塞和唤醒的是指定的线程而非随机线程
        JUC包下的阻塞机制都是通过LockSupport实现的。
        阻塞和唤醒不必按照顺序执行, 即使先唤醒再阻塞的话会不会造成阻塞。
 
 * 方式三：volatile
        
        通过volatile关键字的可见性实现通讯
        
 * LockSupport实现的常用工具类
        
        CountDownLatch、Semaphore、CyclicBarrier、Condition
        
 * LockSupport实现原理
        
        通过一个变量来进行控制线程的阻塞与唤醒。
        线程阻塞需要消耗一个凭证,如果存在凭证则消耗凭证并退出,否则进行等待。
        线程唤醒则是添加一个凭证,但凭证最多只有一个。
        
 * wait()、notify()和park()、unpark的隔离性
        
        两种机制才用的是不同的机制,相互不影响
 
 * wait()、sleep()、park()对中断的处理
        
        wait()、sleep()会抛出异常
        park()不抛出异常而是直接退出阻塞
        
###