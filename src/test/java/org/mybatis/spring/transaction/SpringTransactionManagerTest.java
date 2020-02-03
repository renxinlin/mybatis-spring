/**
 *    Copyright 2010-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.spring.transaction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mybatis.spring.AbstractMyBatisSpringTest;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public final class SpringTransactionManagerTest extends AbstractMyBatisSpringTest {

  /**
   * 优先级在平台事务管理器之下[DataSourceTransactionManager]
   * @throws Exception
   */
  @Test
  public void shouldNoOpWithTx() throws Exception {
    DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
    txDef.setPropagationBehaviorName("PROPAGATION_REQUIRED");
    TransactionStatus status = txManager.getTransaction(txDef);

    SpringManagedTransactionFactory transactionFactory = new SpringManagedTransactionFactory();
    SpringManagedTransaction transaction = (SpringManagedTransaction) transactionFactory.newTransaction(dataSource, null, false);
    transaction.getConnection();
    transaction.commit();
    transaction.close();
    assertEquals("should not call commit on Connection", 0, connection.getNumberCommits());
    assertFalse("should not close the Connection", connection.isClosed());

    txManager.commit(status);
  }

  //    @Test
  //    public void shouldManageWithOtherDatasource() throws Exception {
  //        DefaultTransactionDefinition txDef = new DefaultTransactionDefinition();
  //        txDef.setPropagationBehaviorName("PROPAGATION_REQUIRED");
  //        TransactionStatus status = txManager.getTransaction(txDef);
  //
  //        SpringManagedTransactionFactory transactionFactory = new SpringManagedTransactionFactory(new MockDataSource());
  //        SpringManagedTransaction transaction = (SpringManagedTransaction) transactionFactory.newTransaction(connection, false);
  //        transaction.commit();
  //        transaction.close();
  //        assertEquals("should call commit on Connection", 1, connection.getNumberCommits());
  //        assertTrue("should close the Connection", connection.isClosed());
  //
  //        txManager.commit(status);
  //    }


  /**
   * 自我管理事务提交成功
   * @throws Exception
   */
  @Test
  public void shouldManageWithNoTx() throws Exception {
    SpringManagedTransactionFactory transactionFactory = new SpringManagedTransactionFactory();
    SpringManagedTransaction transaction = (SpringManagedTransaction) transactionFactory.newTransaction(dataSource, null, false);
    transaction.getConnection();
    transaction.commit();
    transaction.close();
    assertEquals("should call commit on Connection", 1, connection.getNumberCommits());
    assertTrue("should close the Connection", connection.isClosed());
  }

  /**
   * 自动提交不应该事务commit
   * @throws Exception
   */
  @Test
  public void shouldNotCommitWithNoTxAndAutocommitIsOn() throws Exception {
    SpringManagedTransactionFactory transactionFactory = new SpringManagedTransactionFactory();
    SpringManagedTransaction transaction = (SpringManagedTransaction) transactionFactory.newTransaction(dataSource, null, false);
    connection.setAutoCommit(true);
    transaction.getConnection();
    transaction.commit();
    transaction.close();
    assertEquals("should not call commit on a Connection with autocommit", 0, connection.getNumberCommits());
    assertTrue("should close the Connection", connection.isClosed());
  }

  /**
   * 无spring事务且无自动提交【创建事务设置autocommit不起作用;底层没有设置其为true依旧是默认值false】
   * 可以提交事务
   * @throws Exception
   */
  @Test
  public void shouldIgnoreAutocommit() throws Exception {
    SpringManagedTransactionFactory transactionFactory = new SpringManagedTransactionFactory();
    SpringManagedTransaction transaction = (SpringManagedTransaction) transactionFactory.newTransaction(dataSource, null, true);
    transaction.getConnection();
    transaction.commit();
    transaction.close();
    assertEquals("should call commit on Connection", 1, connection.getNumberCommits());
    assertTrue("should close the Connection", connection.isClosed());
  }

}
