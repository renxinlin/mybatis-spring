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
package org.mybatis.spring.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mybatis.spring.mapper.MapperFactoryBean;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.context.annotation.Import;

/**
 * Use this annotation to register MyBatis mapper interfaces when using Java
 * Config. It performs when same work as {@link MapperScannerConfigurer} via
 * {@link MapperScannerRegistrar}.
 *
 * <p>Configuration example:</p>
 * <pre class="code">
 * &#064;Configuration
 * &#064;MapperScan("org.mybatis.spring.sample.mapper")
 * public class AppConfig {
 *
 *   &#064;Bean
 *   public DataSource dataSource() {
 *     return new EmbeddedDatabaseBuilder()
 *              .addScript("schema.sql")
 *              .build();
 *   }
 *
 *   &#064;Bean
 *   public DataSourceTransactionManager transactionManager() {
 *     return new DataSourceTransactionManager(dataSource());
 *   }
 *
 *   &#064;Bean
 *   public SqlSessionFactory sqlSessionFactory() throws Exception {
 *     SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
 *     sessionFactory.setDataSource(dataSource());
 *     return sessionFactory.getObject();
 *   }
 * }
 * </pre>
 *
 * @author Michael Lanyon
 * @author Eduardo Macarron
 *
 * @since 1.2.0
 * @see MapperScannerRegistrar
 * @see MapperFactoryBean
 */

/**
 * MapperScan是spring结合mybatis的入口 增加该注解
 * spring启动时候会调用MapperScannerRegistrar    ---> [调用原理属于spring知识体系]
 * 将指定的需要加载的mapper接口全部建立成相应的BeanDefinition，并修改这个接口的实现为MapperFactoryBean
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(MapperScannerRegistrar.class)
public @interface MapperScan {

  /**
   * Alias for the {@link #basePackages()} attribute. Allows for more concise
   * annotation declarations e.g.:
   * {@code @EnableMyBatisMapperScanner("org.my.pkg")} instead of {@code
   * @EnableMyBatisMapperScanner(basePackages= "org.my.pkg"})}.
   */
  /**
   * 效果等同于basePackages 指定需要扫描的mapper接口包
   * @return
   */
  String[] value() default {};

  /**
   * Base packages to scan for MyBatis interfaces. Note that only interfaces
   * with at least one method will be registered; concrete classes will be
   * ignored.
   */
  /**
   * 指定需要扫描的mapper接口包
   * @return
   */
  String[] basePackages() default {};

  /**
   * Type-safe alternative to {@link #basePackages()} for specifying the packages
   * to scan for annotated components. The package of each class specified will be scanned.
   * <p>Consider creating a special no-op marker class or interface in each package
   * that serves no purpose other than being referenced by this attribute.
   */
  /**
   * Class所在的包会被扫描，源码如下
   * basePackages.addAll(
   *         Arrays.stream(annoAttrs.getStringArray("value"))
   *             .filter(StringUtils::hasText)
   *             .collect(Collectors.toList()));
   *
   *     basePackages.addAll(
   *         Arrays.stream(annoAttrs.getStringArray("basePackages"))
   *             .filter(StringUtils::hasText)
   *             .collect(Collectors.toList()));
   *
   *     basePackages.addAll(
   *         Arrays.stream(annoAttrs.getClassArray("basePackageClasses"))
   *             .map(ClassUtils::getPackageName)
   *             .collect(Collectors.toList()));
   *
   *
   *
   * @return
   */
  Class<?>[] basePackageClasses() default {};

  /**
   * The {@link BeanNameGenerator} class to be used for naming detected components
   * within the Spring container.
   */
  /**
   * bean 名称生成器 属于spring内容
   * spring默认DefaultBeanNameGenerator
   *
   * @return
   */
  Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

  /**
   * This property specifies the annotation that the scanner will search for.
   * <p>
   * The scanner will register all interfaces in the base package that also have
   * the specified annotation.
   * <p>
   * Note this can be combined with markerInterface.
   */
  /**
   *这个注解需要结合spring的ClassPathScanningCandidateComponentProvider。findCandidateComponents
   * 具有指定注解的类会被扫描
   * @return
   */
  Class<? extends Annotation> annotationClass() default Annotation.class;

  /**
   * This property specifies the parent that the scanner will search for.
   * <p>
   * The scanner will register all interfaces in the base package that also have
   * the specified interface class as a parent.
   * <p>
   * Note this can be combined with annotationClass.
   */

  /**
   * 这个注解需要结合spring的ClassPathScanningCandidateComponentProvider。findCandidateComponents
   * 具有指定接口的类会被扫描;【issue1:但是接口不会被扫描，暂未验证】
   *
   * 需要注意的是annotationClass和markerInterface可以结合起来使用
   * @return
   */
  Class<?> markerInterface() default Class.class;

  /**
   * Specifies which {@code SqlSessionTemplate} to use in the case that there is
   * more than one in the spring context. Usually this is only needed when you
   * have more than one datasource.
   */
  /**
   *  sqlSessionTemplate 是mybatis结合spring建立sqlsession的实现
   * @return
   */
  String sqlSessionTemplateRef() default "";

  /**
   * Specifies which {@code SqlSessionFactory} to use in the case that there is
   * more than one in the spring context. Usually this is only needed when you
   * have more than one datasource.
   */
  /**
   * SqlSessionFactory的名称，当项目有多个SqlSessionFactory时候需要指定该属性
   * 用于创建sqlsession
   * @return
   */
  String sqlSessionFactoryRef() default "";

  /**
   * 接口mapper的实现类的FactoryBean
   * Specifies a custom MapperFactoryBean to return a mybatis proxy as spring bean.
   *
   */
  Class<? extends MapperFactoryBean> factoryBean() default MapperFactoryBean.class;

}
