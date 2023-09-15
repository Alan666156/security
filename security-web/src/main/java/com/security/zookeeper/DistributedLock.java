package com.security.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 基于zookeeper的开源客户端Cruator实现分布式锁
 * <p>
 * 基本原理：
 * 创建临时有序节点，每个线程均能创建节点成功，但是其序号不同，只有序号最小的可以拥有锁，其它线程只需要监听比自己序号小的节点状态即可
 * <p>
 * 基本思路如下：
 * 1、在你指定的节点下创建一个锁目录lock；
 * 2、线程X进来获取锁在lock目录下，并创建临时有序节点；
 * 3、线程X获取lock目录下所有子节点，并获取比自己小的兄弟节点，如果不存在比自己小的节点，说明当前线程序号最小，顺利获取锁；
 * 4、此时线程Y进来创建临时节点并获取兄弟节点 ，判断自己是否为最小序号节点，发现不是，于是设置监听（watch）比自己小的节点（这里是为了发生上面说的羊群效应）；
 * 5、线程X执行完逻辑，删除自己的节点，线程Y监听到节点有变化，进一步判断自己是已经是最小节点，顺利获取锁。
 *
 * @author fuhongxing
 */
@Slf4j
public class DistributedLock {
    /**
     * 可重入排它锁
     */
    private InterProcessMutex interProcessMutex;
    /**
     * 竞争资源标志
     */
    private String lockName;
    /**
     * 根节点
     */
    private String root = "/distributed/lock/";
    private static final String LOCK_PRE = "zk:lock:{}";
    private static CuratorFramework curatorFramework;
    private static String ZK_URL = "localhost:2181,101.133.234.7:2181,101.133.234.7:2182";

    /**
     * zookeeper的四种节点类型
     * 1、持久化节点 ：所谓持久节点，是指在节点创建后，就一直存在，直到有删除操作来主动清除这个节点——不会因为创建该节点的客户端会话失效而消失。
     * 2、持久化顺序节点：这类节点的基本特性和上面的节点类型是一致的。额外的特性是，在ZK中，每个父节点会为他的第一级子节点维护一份时序，会记录每个子节点创建的先后顺序。
     * 基于这个特性，在创建子节点的时候，可以设置这个属性，那么在创建节点过程中，ZK会自动为给定节点名加上一个数字后缀，作为新的节点名。这个数字后缀的范围是整型的最大值。基于持久顺序节点原理的经典应用-分布式唯一ID生成器。
     * 3、临时节点：和持久节点不同的是，临时节点的生命周期和客户端会话绑定。也就是说，如果客户端会话失效，那么这个节点就会自动被清除掉。注意，这里提到的是会话失效，而非连接断开。
     * 另外，在临时节点下面不能创建子节点，集群zk环境下，同一个路径的临时节点只能成功创建一个，利用这个特性可以用来实现master-slave选举。
     * 4、临时顺序节点：相对于临时节点而言，临时顺序节点比临时节点多了个有序，也就是说每创建一个节点都会加上节点对应的序号，先创建成功，序号越小。其经典应用场景为实现分布式锁。
     *
     * 监视器（watcher）
     * 当zookeeper创建一个节点时，会注册一个该节点的监视器，当节点状态发生改变时，watch会被触发，zooKeeper将会向客户端发送一条通知（就一条，因为watch只能被触发一次）。
     */

    static {
        curatorFramework = CuratorFrameworkFactory.newClient(ZK_URL, new ExponentialBackoffRetry(1000, 3));
        curatorFramework.start();
    }

    public static void main(String[] args) {
        // 创建一个线程池
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                5,
                10,
                1,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        for (int i = 0; i < 10; i++) {
            pool.execute(() -> {
                DistributedLock distributedLock = new DistributedLock("test");
                try {
                    distributedLock.acquireLock();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    distributedLock.releaseLock();
                }
            });
        }
        pool.shutdown();

    }

    /**
     * 实例化
     *
     * @param lockName
     */
    public DistributedLock(String lockName) {
        try {
            this.lockName = lockName;
            //Curator内部是通过InterProcessMutex（可重入锁）来在zookeeper中创建临时有序节点实现
            interProcessMutex = new InterProcessMutex(curatorFramework, root + lockName);
        } catch (Exception e) {
            log.error("initial InterProcessMutex exception=" + e);
        }
    }

    /**
     * 获取锁
     */
    public void acquireLock() {
        int flag = 0;
        try {
            //重试2次，每次最大等待1s，也就是最大等待2s
            while (!interProcessMutex.acquire(1, TimeUnit.SECONDS)) {
                flag++;
                //重试两次
                if (flag > 1) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("distributed lock acquire exception", e);
        }
        if (flag > 1) {
            log.info("Thread:{} acquire distributed lock busy(获取锁失败)", Thread.currentThread().getName());
        } else {
            log.info("Thread:{} acquire distributed lock success(获取锁成功)", Thread.currentThread().getName());
        }
    }

    /**
     * 释放锁
     */
    public void releaseLock() {
        try {
            if (interProcessMutex != null && interProcessMutex.isAcquiredInThisProcess()) {
                interProcessMutex.release();
                curatorFramework.delete().inBackground().forPath(root + lockName);
                log.info("Thread:{} release distributed lock success(锁释放成功)", Thread.currentThread().getName());
            }
        } catch (Exception e) {
            log.info("Thread:{} release distributed lock exception", Thread.currentThread().getName(), e);
        }
    }

}