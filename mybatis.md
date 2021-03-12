###Mybatis的初始化和执行原理

 * 初始化过程(xml为例)
        
        1. 解析xml,生成Configuration
        
                该文件在所有类中共享, xml中的每一个节点都会被解析成MappedStatement对象。
                每个MappedStatement对象包含所有的配置信息,各个MappedStatement相互独立。
        
        2. 生成SqlSessionFactory对象
                
                根据Configuration生成SqlSessionFactory对象,负责SqlSession的生成。

        3. 生成SqlSession对象
                
                包括事务的隔离级别、DataSource信息、执行器(Executor)
                
        4. 通过执行器执行MappedStatement节点
                
                mybatis中的数据都是通过执行器来执行的,
                
###执行流程

 * 查询执行流程
        
        1. 获取执行器,包含了数据库信息、数据库隔离级别等
        2. 通过sqlSource拼接sqlNode
        3. 通过StatementHandler封装请求数据
        4. 通过Statement执行StatementHandler
        5. 通过ResultHandler执行处理结果
        
###Mybatis官方示例

````java

package com.liufeng;

import java.io.IOException;
import java.io.InputStream;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

public class MybatisConnection {
  public static void main(String[] args) throws IOException {
    //第一步：读取配置文件(解析xml中的所有节点)
    InputStream stream = Resources.getResourceAsStream("mybatis-config.xml");
    //第二步：构造SqlSessionFactoryBuilder
    SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
    //第三步：解析配置文件生成Configuration和SqlSessionFactory
    SqlSessionFactory factory = builder.build(stream);
    //第四步：获取SqlSession
    DefaultSqlSession sqlSession = (DefaultSqlSession) factory.openSession();
    sqlSession.selectList("test");
    //第五步：获取需要操作的Mapper的实例
    //CaseMeetingPersonnelMapper mapper = sqlSession.getMapper(CaseMeetingPersonnelMapper.class);
    //第六步：查询并返回结果
    //List<CaseMeetingPersonnel> person = mapper.selectAll();
  }
}

````

###Executor

 * Executor的作用
        
        Executor是Mybatis中的执行器, 所有操作都是通过Executor的实现类来执行的。
        主要实现了缓存处理。
        CachingExecutor会处理二级缓存的逻辑, 二级缓存存储在MappedStatement中。
        BaseExecutor会处理一级缓存的逻辑和查询数据库,一级缓存存储在BaseExecutor中。
        
 * Executor的实现类
        
        Executor实现类分为两种CachingExecutor和BaseExecutor。
        具体使用是BaseExecutor的实现类,CachingExecutor是对BaseExecutor进行了装饰,
        使其具备了缓存的功能, 我们常说的一级缓存。
 
        BaseExecutor的实现类有三种:
        SimpleExecutor: 这个执行器类型不做特殊的事情(我们默认使用的)。
        BatchExecutor: 这个执行器类型会执行批处理语句(批量操作)。
        ReuseExecutor: 这个执行器类型会复用预处理语句(复用statement,而不是重新创建)。
        
        BaseExecutor可以理解为模板,定义了具体的流程,但一些具体的实现由子类实现。
        
###SqlSession
 
 * SqlSession的作用
        
        SqlSession是Mybatis的API类,内部通过Executor执行。
        
###BoundSql

 * statement需要执行的sql语句。
 
###StatementHandler
 
 * StatementHandler的作用
        
        会对RoutingStatementHandler进行拦截处理。生成Statement对象
 
 * StatementHandler的实现类
        
        同Executor一样,StatementHandler的默认实现为RoutingStatementHandler和BaseStatementHandler两个类
        RoutingStatementHandler是BaseStatementHandler具体实现类的包装类。
        
        BaseStatementHandler为模板类,具体实现类：
        CallableStatementHandler：存储过程。
        PreparedStatementHandler：预处理。
        SimpleStatementHandler：简单处理。
        
###ResultSetHandler

 * ResultSetHandler的作用
        
        对结果集进行处理
        
###ParameterHandler

 * ParameterHandler的作用
 
        对PreparedStatement的参数进行处理。
        
###事务
 
 * 事务
        
        如果SqlSession设置的手动提交,需要我们手动调用commit()方法。