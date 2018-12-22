package chapter16

import org.apache.log4j.{Level, LogManager}
import org.apache.spark._
import org.apache.spark.rdd.RDD

class MyMapper(n: Int) extends Serializable{
 @transient lazy val log = org.apache.log4j.LogManager.getLogger("myLogger")
 def MyMapperDosomething(rdd: RDD[Int]): RDD[String] =
   rdd.map{ i =>
     log.warn("mapping: " + i)
     (i + n).toString
   }
}

// 컴패년 오브젝트
object MyMapper {
 def apply(n: Int): MyMapper = new MyMapper(n)
}

// 주오 오브젝트
object MyLog {
 def main(args: Array[String]) {
   val log = LogManager.getRootLogger
   log.setLevel(Level.WARN)
   val conf = new SparkConf()
                  .setAppName("My App")
                  .setMaster("local[*]")
   conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")               
   val sc = new SparkContext(conf)

   log.warn("Started")
   val data = sc.parallelize(1 to 100000)
   val mapper = MyMapper(1)
   val other = mapper.MyMapperDosomething(data)
   other.collect()
   log.warn("Finished")
 }
}