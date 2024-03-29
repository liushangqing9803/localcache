package cn.mianshiyi.example.controller;


import cn.mianshiyi.example.demo.EtcdTestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
//@Controller
public class EtcdTestController {

    @Resource
    private EtcdTestCache etcdTestCache;

    @PostConstruct
    public void init() {
        System.out.println(1111111111);
        ScheduledExecutorService QPS_COUNT_THREAD_POOL = Executors.newScheduledThreadPool(1);

        QPS_COUNT_THREAD_POOL.scheduleAtFixedRate(() -> {
            String cache = etcdTestCache.getCache("1");
            System.out.println(cache);
            System.out.println(11111);
        }, 1, 1, TimeUnit.SECONDS);
    }

    @RequestMapping("/etcdlocalException")
    @ResponseBody
    public String localException() throws Exception {
        etcdTestCache.broadcast("1".getBytes(StandardCharsets.UTF_8));
        return "sss";

    }

}
