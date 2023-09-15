package com.security.zookeeper;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Curator提供了两种选举方案：Leader Latch 和 Leader Election
 * 利用zookeeper来实现Master选举的基本思路如下：
 * 选择一个根节点（与其他业务隔离），比如/jobMaster，多台机器同时在此节点下面创建一个子节点/jobMaster/lock，zookeeper保证了最终只有一台机器能够创建成功，那么这台机器将成为Master。由它来执行业务操作
 *
 * 参考：https://blog.csdn.net/wo541075754/article/details/70216046
 * @author fuhongxing
 */
@Slf4j
public class LeaderLatchTest {

    private static final String PATH = "/jobMaster/leader";

    private static String ZK_URL = "101.133.234.7:2181";

    public static void main(String[] args) {
        leaderLatchTest();
    }

    /**
     * Leader Latch：随机从候选着中选出一台作为leader，选中之后除非调用close()释放leadship，否则其他的后选择无法成为leader。其中spark使用的就是这种方法。
     */
    private static void leaderLatchTest() {
        List<LeaderLatch> latchList = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        try {
            //启动了10个client，程序会随机选中其中一个作为leader。通过注册监听的方式来判断自己是否成为leader。调用close()方法释放当前领导权。
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = getClient();
                clients.add(client);
                //LeaderLatch通过增加了一个ConnectionStateListener监听连接问题。如果出现SUSPENDED或者LOST，leader会报告自己不再是leader（直到重新建立连接，否则不会有leader）。如果LOST的连接被重新建立即RECONNECTED，leaderLatch会删除先前的zNode并重新建立zNode。
                final LeaderLatch leaderLatch = new LeaderLatch(client, PATH, "client#" + i);
                leaderLatch.addListener(new LeaderLatchListener() {
                    @Override
                    public void isLeader() {
                        log.info("{} :I am leader. I am doing jobs!", leaderLatch.getId());
                    }

                    @Override
                    public void notLeader() {
                        log.info("{} :I am not leader. I will do nothing!", leaderLatch.getId());
                    }
                });
                latchList.add(leaderLatch);
                leaderLatch.start();
            }
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            log.error("Leader Latch选举异常", e);
        } finally {
            for(CuratorFramework client : clients){
                CloseableUtils.closeQuietly(client);
            }

            for(LeaderLatch leaderLatch : latchList){
                CloseableUtils.closeQuietly(leaderLatch);
            }
        }
    }

    /**
     * Leader Election：通过LeaderSelectorListener可以对领导权进行控制， 在适当的时候释放领导权，这样每个节点都有可能获得领导权。 而LeaderLatch则一直持有leadership， 除非调用close方法，否则它不会释放领导权。
     */
    public static void leaderSelectorTest (){
        List<LeaderSelector> selectors = new ArrayList<>();
        List<CuratorFramework> clients = new ArrayList<>();
        try {
            //创建了10个LeaderSelector并对起添加监听，当被选为leader之后，调用takeLeadership方法进行业务逻辑处理，处理完成即释放领导权。其中autoRequeue()方法的调用确保此实例在释放领导权后还可能获得领导权。
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = getClient();
                clients.add(client);
                final String name = "client#" + i;
                //LeaderSelectorListener类继承了ConnectionStateListener。一旦LeaderSelector启动，它会向curator客户端添加监听器。 使用LeaderSelector必须时刻注意连接的变化。一旦出现连接问题如SUSPENDED，curator实例必须确保它可能不再是leader，直至它重新收到RECONNECTED。如果LOST出现，curator实例不再是leader并且其takeLeadership()应该直接退出
                LeaderSelector leaderSelector = new LeaderSelector(client, PATH, new LeaderSelectorListener() {
                    @Override
                    public void takeLeadership(CuratorFramework client) throws Exception {
                        log.info(name + ":I am leader.");
                        Thread.sleep(2000);
                    }
                    @Override
                    public void stateChanged(CuratorFramework client, ConnectionState newState) {

                    }
                });
                leaderSelector.autoRequeue();
                leaderSelector.start();
                selectors.add(leaderSelector);
            }
            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            log.error("Leader Election选举异常", e);
        } finally {
            for(CuratorFramework client : clients){
                CloseableUtils.closeQuietly(client);
            }
            for(LeaderSelector selector : selectors){
                CloseableUtils.closeQuietly(selector);
            }

        }
    }

    private static CuratorFramework getClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(ZK_URL)
                .retryPolicy(retryPolicy)
                .sessionTimeoutMs(6000)
                .connectionTimeoutMs(3000)
                .namespace("demo")
                .build();
        client.start();
        return client;
    }


}
