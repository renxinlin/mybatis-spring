事务模块
spring做了一个前提假设：即一个事务的操作一定是在一个thread中执行
基于这个假设，spring在transaction创建时，
会用ThreadLocal把创建这个事务的jdbc connection绑定到当前thread，
接下来在事务的整个生命周期中都会从ThreadLocal中获取同一个jdbc connection

SpringManagedTransaction中的TransactionSynchronizationManager负责从ThreadLocal中存取jdbc connection

TransactionSynchronizationManager负责从ThreadLocal中存取jdbc connection
创建事务的时候会通过dataSource.getConnection()获取一个新的jdbc connection，然后绑定到ThreadLocal
在业务代码中执行sql时，通过DataSourceUtils.getConnection()从ThreadLocal中获取当前事务的jdbc connection, 然后在该jdbc connection上执行sql
commit和rollback事务时，从ThreadLocal中获取当前事务的jdbc connection，然后对该jdbc connection进行commit和rollback



以上是spring的事务
myabtis-spring本身无事务方案
其调用了spring事务
具体的一些特性可以参考中的注释
SpringTransactionManagerTest

一般在项目中也是使用spring的平台事务管理器管理事务