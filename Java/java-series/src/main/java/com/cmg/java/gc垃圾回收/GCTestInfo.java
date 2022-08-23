package com.cmg.java.gc垃圾回收;

public class GCTestInfo {

//    串行GC
//    堆内存256MB
//    java -XX:+UseSerialGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis.java
//    9个youngGC，之后一直fullGC
//    youngGC在10毫秒左右，fullGC在10毫秒左右
//    内存不够，出现OOM


//    堆内存512MB
//    java -XX:+UseSerialGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
//    多次youngGC，1次fullGC
//    youngGC在15毫秒左右，fullGC50毫秒
//    生成对象9727


//    堆内存1GB
//    java -XX:+UseSerialGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis
//    多次youngGC youngGC在30毫秒左右
//    生成对象14916


//    堆内存4GB
//    java -XX:+UseSerialGC -Xms4g -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
//    只进行了3次youngGC youngGC在80毫秒左右
//    生成对象13590

//    总结：
//    堆内存越大，执行youngGC和fullGC的次数越少
//    单次youngGC和fullGC的时间随着内存的增大而增大
//    在堆内存为1g时，生成的对象最多
//    采用串行GC，随着堆内存的增大，GC的次数会减少，但是每次GC的时间会增加。



//    并行GC
//    堆内存256MB
//    java -XX:+UseParallelGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis.java
//    10个youngGC，之后一直fullGC
//    youngGC在4毫秒左右，fullGC20毫秒左右
//    内存不够，出现OOM


//    堆内存512MB
//    java -XX:+UseParallelGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
//    多次youngGC，多次fullGC
//    youngGC在8毫秒左右，fullGC42毫秒左右
//    生成对象8281


//    堆内存1GB
//    java -XX:+UseParallelGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis
//    多次youngGC一次fullGC
//    youngGC在15毫秒左右，fullGC50毫秒
//    生成对象15066


//    堆内存4GB
//    java -XX:+UseParallelGC -Xms4g -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
//    只进行了4次youngGC youngGC在38毫秒左右
//    生成对象17035


//    总结：
//    堆内存越大，执行youngGC和fullGC的次数越少
//    单次youngGC和fullGC的时间随着内存的增大而增大
//    在堆内存为越大，生成对象越多
//    采用并行GC比串行GC每次GC的时间更少


//    CMS GC
//    堆内存256MB
//    java -XX:+UseConcMarkSweepGC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis.java
//    9个youngGC，youngGC在10毫秒左右，fullGC在30毫秒左右
//    生成对象4329

//    堆内存512MB
//    java -XX:+UseConcMarkSweepGC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
//
//    多次youngGC，多次fullGC
//    youngGC在12毫秒左右，fullGC在57毫秒左右
//            生成对象11254
//    堆内存1GB
//    java -XX:+UseConcMarkSweepGC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis
//    多次youngGC，多次fullGC
//    youngGC在15毫秒左右，fullGC在60毫秒左右
//    生成对象15820

//    堆内存4GB
//    java -XX:+UseConcMarkSweepGC -Xms4g -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
//    只进行了6次youngGC youngGC在50毫秒左右
//    生成对象15145



//    总结：
//    堆内存越大，执行youngGC和fullGC的次数越少
//    单次youngGC和fullGC的时间随着内存的增大而增大
//    在堆内存为越大，生成对象越多


//    G1GC
//    堆内存256MB
//    java -XX:+UseG1GC -Xms256m -Xmx256m -XX:+PrintGCDetails GCLogAnalysis.java
//    内存不够，出现OOM
//    GC时间1.6ms左右


//    堆内存512MB
//    java -XX:+UseG1GC -Xms512m -Xmx512m -XX:+PrintGCDetails GCLogAnalysis
//    GC时间1.3ms左右
//    生成对象11284

//    堆内存1GB
//    java -XX:+UseG1GC -Xms1g -Xmx1g -XX:+PrintGCDetails GCLogAnalysis
//    GC时间3.2ms左右
//    生成对象14569


//    堆内存4GB
//    java -XX:+UseG1GC -Xms4g -Xmx4g -XX:+PrintGCDetails GCLogAnalysis
//    GC时间8.7ms左右
//    生成对象16853


//    总结：
//    堆内存越大，越适合用G1GC
//    在堆内存为越大，生成对象越多



//    如何选择GC
//    选择正确的GC算法，唯一可行的方式就是去尝试，一般性的指导原则：

//    如果系统考虑吞吐优先，CPU资源都用来最大程度处理业务，用Parallel GC。
//    如果系统考虑低延迟有限，每次GC时间尽量短，用CMS GC。
//    如果系统内存堆较大，同时希望整体来看平均GC时间可控，使用G1 GC。


//    对于内存大小的考量：
//    一般4G以上，算是比较大，用G1的性价比较高。
//    一般超过8G，比如16G-64G内存，非常推荐使用G1 GC。

}
