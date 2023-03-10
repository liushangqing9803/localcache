package cn.mianshiyi.example.controller;

import cn.mianshiyi.example.demo.ZkTestCache;
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
public class TestController {

    @Resource
    private ZkTestCache zkTestCache;

    @PostConstruct
    public void init() {
        ScheduledExecutorService QPS_COUNT_THREAD_POOL = Executors.newScheduledThreadPool(1);

        QPS_COUNT_THREAD_POOL.scheduleAtFixedRate(() -> {
            String cache = zkTestCache.getCache(1L);
            System.out.println(cache);
        }, 1, 1, TimeUnit.SECONDS);
    }

    @RequestMapping("/localException")
    @ResponseBody
    public String localException() throws Exception {
      zkTestCache.broadcast("1");
        return "sss";

    }

}
