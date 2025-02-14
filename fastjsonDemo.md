pom.xml
```xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
            <version>2.6.4</version> <!-- 与Spring Boot 2.7.9兼容的版本 -->
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.2.4</version>
        </dependency>
```

application.yml
```yml
spring:
  redis:
    host: 
    port: 
    password:    # 如果 Redis 设置了密码的话
    database: 0
    jedis:
      pool:
        max-active: 10
        max-wait: 3000000000000
        max-idle: 5
        min-idle: 1
    timeout: 600000000ms
```
fastjsonDemo.md
```java
package com.tiaohe;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.RedisCallback;

import java.io.StringReader;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ConcurrentMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

@SpringBootApplication
public class FastjsonDemo implements CommandLineRunner {
    private static Random random = new Random();

    public static int nextInt(int origin, int bound) {
        // 生成一个 [0, bound - origin) 范围的随机数
        return random.nextInt(bound - origin) + origin;
    }
    private static final int TEST_RUNS = 5; // 测试运行次数
    private static final ExecutorService executorService = Executors.newFixedThreadPool(4); // 线程池，支持并行任务
    // 定义缓存和缓存失效机制
    private static final ConcurrentMap<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private static final ScheduledExecutorService cacheCleaner = Executors.newSingleThreadScheduledExecutor();

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public static void main(String[] args) {
        SpringApplication.run(FastjsonDemo.class, args);
    }

    @Override
    public void run(String... args) {
        // 启动缓存清理任务，每秒清理一次
        cacheCleaner.scheduleAtFixedRate(this::cleanCache, 1, nextInt(4, 12), TimeUnit.SECONDS);

        // 1. 生成测试数据
        serializeData();

        // 2. 异步反序列化性能对比
        compareAsyncPerformance();
    }

    // 使用 Random 来生成随机的 User 和 Address 数据
    public void serializeData() {
        Random random = new Random();
        List<Address> addresses = new ArrayList<>();

        // 随机生成地址数据（减少数据量以提高测试速度）
        for (int i = 0; i < 50000; i++) {
            String street = "Street " + random.nextInt(100);
            String city = "City " + random.nextInt(5); // 随机选择一个城市
            addresses.add(new Address(street, city));
        }

        // 随机生成用户数据
        String name = "User" + random.nextInt(1000);
        int age = 18 + random.nextInt(50); // 随机生成年龄在 18 到 67 之间

        User user = new User(name, age, addresses);

        // 批量操作：序列化数据并写入 Redis
        try {
            String json = JSON.toJSONString(user, SerializerFeature.PrettyFormat);
            stringRedisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                connection.stringCommands().set("user".getBytes(), json.getBytes());
                return null;
            });
            System.out.println("Serialized data written to Redis");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 从 Redis 中读取数据并缓存到本地
    private String getCachedData(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null || entry.isExpired()) {
            String value = stringRedisTemplate.opsForValue().get(key);
            cache.put(key, new CacheEntry(value, System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(nextInt(4,12))));
            return value;
        }
        return entry.value;
    }

    // 缓存清理任务
    private void cleanCache() {
        long now = System.currentTimeMillis();
        cache.entrySet().removeIf(entry -> {
            boolean expired = entry.getValue().isExpired(now);
            if (expired) {
                System.out.println("Cache entry expired and removed: " + entry.getKey() + " at " + now); // 增加日志
                System.out.println("----------------------expired---------------------------------------");
            }
            return expired;
        });
    }

    // ----------------- 1. 未优化的反序列化 -----------------
    // 优化前的反序列化（直接使用 JSON.parseObject）
    public User deserializeDataBeforeOptimization() {
        try {
            String json = getCachedData("user");
            return JSON.parseObject(json, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------- 2. 优化后的反序列化（使用 JSONReader） -----------------
    // 优化后：使用 JSONReader 提高读取效率，减少内存占用
    public User deserializeDataAfterOptimization() {
        try {
            String json = getCachedData("user");
            JSONReader reader = new JSONReader(new StringReader(json));
            User user = reader.readObject(User.class);
            reader.close();
            return user;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------- 3. 进一步优化后的反序列化（通过 ByteBuffer） -----------------
    // 优化后：使用 ByteBuffer 进行数据读取，避免多余的内存分配
    public User deserializeDataAfterByteBufferOptimization() {
        try {
            String json = getCachedData("user");
            ByteBuffer byteBuffer = ByteBuffer.wrap(json.getBytes("UTF-8"));
            String jsonString = new String(byteBuffer.array(), "UTF-8");
            return JSON.parseObject(jsonString, User.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ----------------- 4. 异步反序列化对比 -----------------
    // 异步反序列化性能对比
    @SneakyThrows
    public void compareAsyncPerformance() {
        AtomicLong totalBeforeTime = new AtomicLong(0);
        AtomicLong totalAfterTime = new AtomicLong(0);
        AtomicLong totalByteBufferTime = new AtomicLong(0);

        for (int i = 0; i < TEST_RUNS; i++) {
            long startTime = System.currentTimeMillis();

            // 使用异步方式反序列化未优化版本
            int finalI2 = i;
            CompletableFuture<Void> beforeFuture = CompletableFuture.supplyAsync(() -> {
                long beforeStartTime = System.currentTimeMillis();
                User userBefore = deserializeDataBeforeOptimization();
                long beforeEndTime = System.currentTimeMillis();
                long beforeTime = beforeEndTime - beforeStartTime;
                System.out.println("Time taken for Before Optimization (Run " + (finalI2 + 1) + "): " + beforeTime + "ms");
                totalBeforeTime.addAndGet(beforeTime);
                return null;
            }, executorService);

            // 使用异步方式反序列化优化版本（JSONReader）
            int finalI1 = i;
            CompletableFuture<Void> afterFuture = CompletableFuture.supplyAsync(() -> {
                long afterStartTime = System.currentTimeMillis();
                User userAfter = deserializeDataAfterOptimization();
                long afterEndTime = System.currentTimeMillis();
                long afterTime = afterEndTime - afterStartTime;
                System.out.println("Time taken for After Optimization (Run " + (finalI1 + 1) + "): " + afterTime + "ms");
                totalAfterTime.addAndGet(afterTime);
                return null;
            }, executorService);

            // 使用异步方式反序列化优化版本（ByteBuffer）
            int finalI = i;
            CompletableFuture<Void> byteBufferFuture = CompletableFuture.supplyAsync(() -> {
                long byteBufferStartTime = System.currentTimeMillis();
                User userByteBuffer = deserializeDataAfterByteBufferOptimization();
                long byteBufferEndTime = System.currentTimeMillis();
                long byteBufferTime = byteBufferEndTime - byteBufferStartTime;
                System.out.println("Time taken for ByteBuffer Optimization (Run " + (finalI + 1) + "): " + byteBufferTime + "ms");
                totalByteBufferTime.addAndGet(byteBufferTime);
                return null;
            }, executorService);

            // 等待异步任务完成
            CompletableFuture.allOf(beforeFuture, afterFuture, byteBufferFuture).join();

            long endTime = System.currentTimeMillis();
            System.out.println("Total time taken for async tasks (Run " + (i + 1) + "): " + (endTime - startTime) + "ms");
            System.out.println("----------------------------------------------------------------------------");
            Thread.sleep(5000);
        }

        System.out.println("Average time taken for Before Optimization: " + (totalBeforeTime.get() / TEST_RUNS) + "ms");
        System.out.println("Average time taken for After Optimization: " + (totalAfterTime.get() / TEST_RUNS) + "ms");
        System.out.println("Average time taken for ByteBuffer Optimization: " + (totalByteBufferTime.get() / TEST_RUNS) + "ms");
    }

    // ----------------- 模型类 -----------------
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Address {
        private String street;
        private String city;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class User {
        private String name;
        private int age;
        private List<Address> addresses;
    }

    // 缓存条目类
    private static class CacheEntry {
        final String value;
        final long expiryTime;

        CacheEntry(String value, long expiryTime) {
            this.value = value;
            this.expiryTime = expiryTime;
        }

        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }

        boolean isExpired(long currentTime) {
            return currentTime > expiryTime;
        }
    }
}

```