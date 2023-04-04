package cn.mianshiyi.example.controller;

import cn.mianshiyi.example.demo.EtcdTestCache;
import cn.mianshiyi.example.demo.JgroupTestCache;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author shangqing.liu
 */
@Controller
public class JgroupTestController {

    @Resource
    private JgroupTestCache jgroupTestCache;

    @PostConstruct
    public void init() {
        System.out.println(1111111111);
        ScheduledExecutorService QPS_COUNT_THREAD_POOL = Executors.newScheduledThreadPool(1);

        QPS_COUNT_THREAD_POOL.scheduleAtFixedRate(() -> {
            String cache = jgroupTestCache.getCache("1");
            System.out.println(cache);
            System.out.println("jgroup");
        }, 1, 1, TimeUnit.SECONDS);
    }

    @RequestMapping("/etcdlocalException")
    @ResponseBody
    public String localException() throws Exception {
        jgroupTestCache.broadcast("1");
        return "sss";

    }

}
