package com.ling.middleware;


import net.rubyeye.xmemcached.CASOperation;
import net.rubyeye.xmemcached.Counter;
import net.rubyeye.xmemcached.GetsResponse;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.transcoders.StringTranscoder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.InetSocketAddress;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MemcachedTest {
    @Autowired
    private MemcachedClient memcachedClient;

    @Test
    public void testGetSet() throws Exception {
        //三个参数，第一个是存储的key名称，第二个是expire时间（单位：秒），超过这个时间
        //memcached将这个数据替换出去，0表示永久存储（默认是一个月），第三个参数就是实际
        //存储的数据，可以是任意Java可序列化类型
        memcachedClient.set("hello", 0, "Hello,xmemcached");
        String value = memcachedClient.get("hello");
        System.out.println("hello=" + value);
        memcachedClient.delete("hello");
    }

    @Test
    public void testMore() throws Exception {
        if (!memcachedClient.set("hello", 0, "world")) {
            System.err.println("set error");
        }
        //add命令：用于将value存储在指定的key中，如果add的key已经存在，则不会更新数据
        if (!memcachedClient.add("hello", 0, "dennis")) {
            System.err.println("Add error,key is existed");
        }
        //replace命令：用于替换已存在的key（键）的value（数据值）。如果key不存在，则替换失败，并获得响应NOT_STORED
        if (!memcachedClient.replace("hello", 0, "dennis")) {
            System.err.println("replace error");
        }
        //append命令，用于向已存在key（键）的value（数据值）后面追加数据
        memcachedClient.append("hello", " good");
        //prepend命令，用于向已存在key（键）的value（数据值）后面追加数据
        memcachedClient.prepend("hello", "hello ");
        String name = memcachedClient.get("hello", new StringTranscoder());
        System.out.println(name);
        //这个方法删除数据并且告诉Memcached，不用返回应答，因此这个方法不会等待应答直接返回，比较适合于批量处理
        memcachedClient.deleteWithNoReply("hello");
    }

    //类似数据的增和减，用于原子递增或者递减变量数值
    @Test
    public void testIncrDecr() throws Exception {
        memcachedClient.delete("Incr");
        memcachedClient.delete("Decr");
        //第一个参数指定递增的key名称，第二个参数指定递增的幅度大小，第三个参数指定当key不存在的情况下的初始值
        //重载方法省略第三个参数，默认指定为0
        System.out.println(memcachedClient.incr("Incr", 6, 12));
        System.out.println(memcachedClient.incr("Incr", 3));
        System.out.println(memcachedClient.incr("Incr", 2));
        System.out.println(memcachedClient.decr("Decr", 1, 6));
        System.out.println(memcachedClient.decr("Decr", 2));
    }

    //Xmemcached还提供了一个称为计数器的封装，它封装了incr/decr方法，使用它可以类似AtomicLong那样去操作计数
    //这个计数器适合在高并发抢购场景下做并发控制
    @Test
    public void testCounter() throws Exception {
        //第一个参数为计数器的key，第二个参数当key不存在时的默认值
        Counter counter=memcachedClient.getCounter("counter",10);
        System.out.println("counter="+counter.get());
        //执行一次给计数器加1
        long c1 =counter.incrementAndGet();
        System.out.println("counter="+c1);
        //执行一次给计数器减1
        long c2 =counter.decrementAndGet();
        System.out.println("counter="+c2);
        //会自动判断正负，进行加法或者转换成减法运算
        long c3 =counter.addAndGet(-10);
        System.out.println("counter="+c3);
    }

    @Test
    public void testTouch() throws Exception {
        memcachedClient.set("Touch", 2, "Touch Value");
        Thread.sleep(1000);
        //touch协议，更新缓存数据的超时时间，这里设置1秒过期
        memcachedClient.touch("Touch",3);
        Thread.sleep(2000);
        String value =memcachedClient.get("Touch",3000);
        System.out.println("Touch=" + value);
    }

    @Test
    public void testCas() throws Exception {
        memcachedClient.set("cas", 0, 100);
        GetsResponse<Integer> result = memcachedClient.gets("cas");
        System.out.println("result value "+result.getValue());

        long cas = result.getCas();
        //尝试将a的值更新为2
        if (!memcachedClient.cas("cas", 0, 200, cas)) {
            System.err.println("cas error");
        }
        System.out.println("cas value "+memcachedClient.get("cas"));

        memcachedClient.cas("cas", 0, new CASOperation<Integer>() {
            public int getMaxTries() {
                return 1;
            }

            public Integer getNewValue(long currentCAS, Integer currentValue) {
                return 300;
            }
        });
        System.out.println("cas value "+memcachedClient.get("cas"));
    }

    @Test
    public void testStat() throws Exception {
        Map<InetSocketAddress,Map<String,String>> result=memcachedClient.getStats();
        System.out.println("Stats=" + result.toString());
        Map<InetSocketAddress,Map<String,String>> items=memcachedClient.getStatsByItem("items");
        System.out.println("items=" + items.toString());
    }
}
