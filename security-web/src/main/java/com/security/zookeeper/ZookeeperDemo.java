package com.security.zookeeper;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;

/**
 * @author fuhongxing
 * @date 2021/4/5 12:19
 */
@Slf4j
public class ZookeeperDemo {
    public static void main(String[] args) throws Exception {
        ZookeeperDemo zookeeperDemo = new ZookeeperDemo();
        zookeeperDemo.zk();
    }

    public String zk() throws Exception {
        // 创建一个与服务器的连接 需要(服务端的 ip+端口号)(session过期时间)(Watcher监听注册)
        // 监控所有被触发的事件
        ZooKeeper zk = new ZooKeeper("101.133.234.7:2181", 3000,
                event -> {
                    // TODO Auto-generated method stub
                    log.info("已经触发了" + event.getType() + "事件！");
                });
        if (ZooKeeper.States.CONNECTING == zk.getState()) {
            try {
                // 创建一个目录节点
                /**
                 * CreateMode: PERSISTENT (持续的，相对于EPHEMERAL，不会随着client的断开而消失)
                 * PERSISTENT_SEQUENTIAL（持久的且带顺序的） EPHEMERAL (短暂的，生命周期依赖于client session)
                 * EPHEMERAL_SEQUENTIAL (短暂的，带顺序的)
                 */
                zk.create("/testRootPath", "testRootData".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

                // 创建一个子目录节点
                zk.create("/testRootPath/testChildPathOne", "testChildDataOne".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info(new String(zk.getData("/testRootPath", false, null)));

                // 取出子目录节点列表
                log.info(JSON.toJSONString(zk.getChildren("/testRootPath", true)));

                // 创建另外一个子目录节点
                zk.create("/testRootPath/testChildPathTwo", "testChildDataTwo".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                log.info(JSON.toJSONString(zk.getChildren("/testRootPath", true)));

                // 修改子目录节点数据
                zk.setData("/testRootPath/testChildPathOne", "hahahahaha".getBytes(),
                        -1);
                byte[] datas = zk.getData("/testRootPath/testChildPathOne", true, null);
                String str = new String(datas, StandardCharsets.UTF_8);
                log.info(str);

                // 删除整个子目录 -1代表version版本号，-1是删除所有版本
                zk.delete("/testRootPath/testChildPathOne", -1);
                log.info(JSON.toJSONString(zk.getChildren("/testRootPath", true)));
                log.info(str);
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return null;
    }
}
