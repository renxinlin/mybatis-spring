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
package org.mybatis.spring.support;

import static org.springframework.util.Assert.notNull;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.dao.support.DaoSupport;

/**
 * Convenient super class for MyBatis SqlSession data access objects.
 * It gives you access to the template which can then be used to execute SQL methods.
 * <p>
 * This class needs a SqlSessionTemplate or a SqlSessionFactory.
 * If both are set the SqlSessionFactory will be ignored.
 * <p>
 * {code Autowired} was removed from setSqlSessionTemplate and setSqlSessionFactory
 * in version 1.2.0.
 * 
 * @author Putthibong Boonbong
 *
 * @see #setSqlSessionFactory
 * @see #setSqlSessionTemplate
 * @see SqlSessionTemplate
 */

/**
 * 继承了spring的DaoSupport
 * 非常重要的一个父类 这个类为所有的mapperfactoryBean提供了
 * sqlSessionFactory 从而提供了sqlSessionTemplate[sqlSession]
 * 所有的mapperFactorybean都继承了这个接口 从而实现了给mapper代理类注入sqlsession的能力
 */
public abstract class SqlSessionDaoSupport extends DaoSupport {

  private SqlSession sqlSession;
  /**
   * 是否调用外部的sqlsession
   */
  private boolean externalSqlSession;

  /**
   * 一般项目中只需要配置SqlSessionFactory
   * SqlSessionFactory 和 SqlSessionTemplate二配一【issue2 由于没采用spring的 SqlSessionTemplatebean 所以这里每个mapperFactoryBean都有一个自己的SqlSessionTemplate对象】
   * @param sqlSessionFactory
   */
  public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
    if (!this.externalSqlSession) {
      this.sqlSession = new SqlSessionTemplate(sqlSessionFactory);
    }
  }

  /**
   * 如果设置了SqlSessionTemplate则修改externalSqlSession值
   * 需要结合 definition.getPropertyValues().add("sqlSessionFactory", this.sqlSessionFactory);
   * 该方法优先于applyPropertyValues
   * 也就是SqlSessionTemplate优先级》SqlSessionFactory
   *
   * @param sqlSessionTemplate
   */
  public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
    this.sqlSession = sqlSessionTemplate;
    this.externalSqlSession = true;
  }

  /**
   * Users should use this method to get a SqlSession to call its statement methods
   * This is SqlSession is managed by spring. Users should not commit/rollback/close it
   * because it will be automatically done.
   *
   * @return Spring managed thread safe SqlSession
   */
  public SqlSession getSqlSession() {
    return this.sqlSession;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void checkDaoConfig() {
    notNull(this.sqlSession, "Property 'sqlSessionFactory' or 'sqlSessionTemplate' are required");
  }

}
