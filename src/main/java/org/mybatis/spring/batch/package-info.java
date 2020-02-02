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
/**
 * Contains classes to facilitate the migration in Spring-Batch applications.
 */

/**
 * mybatis对spring-batch做了相关实现，从而方便批量操作
 * 包含了三个类
 * 批量写 MyBatisBatchItemWriter
 * 游标读
 * 分页读
 * 需要注意的是，开发很少会使用这样的批处理方式 一般写用foreach等
 * 这些类是和springbatch扩展
 * 起始于1.1.0
 * 在 2.0.0 中，还提供了三个建造器（builder）类来对 Java 配置提供支持：MyBatisPagingItemReaderBuilder、MyBatisCursorItemReaderBuilder 和 MyBatisBatchItemWriterBuilder
 */
package org.mybatis.spring.batch;
