/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.elasticsearch.spark.rdd

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.apache.spark.Partition
import org.apache.spark.SparkContext
import org.apache.spark.TaskContext
import org.elasticsearch.hadoop.cfg.Settings
import org.elasticsearch.hadoop.rest.InitializationUtils
import org.elasticsearch.hadoop.rest.PartitionDefinition
import org.elasticsearch.hadoop.serialization.builder.JdkValueReader

private[spark] class JavaEsRDD[T](
  @transient sc: SparkContext,
  config: scala.collection.Map[String, String] = scala.collection.Map.empty)
  extends AbstractEsRDD[(String, T)](sc, config) {

  override def compute(split: Partition, context: TaskContext): JavaEsRDDIterator[T] = {
    new JavaEsRDDIterator[T](context, split.asInstanceOf[EsPartition].esPartition)
  }
}

private[spark] class JavaEsRDDIterator[T](
  context: TaskContext,
  partition: PartitionDefinition)
  extends AbstractEsRDDIterator[(String, T)](context, partition) {

  override def getLogger() = LogFactory.getLog(JavaEsRDD.getClass())

  override def initReader(settings: Settings, log: Log) = {
    InitializationUtils.setValueReaderIfNotSet(settings, classOf[JdkValueReader], log)
  }

  override def createValue(value: Array[Object]): (String, T) = {
    (value(0).toString(), value(1).asInstanceOf[T])
  }
}