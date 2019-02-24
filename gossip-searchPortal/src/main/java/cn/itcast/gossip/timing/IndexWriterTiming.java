package cn.itcast.gossip.timing;

import cn.itcast.gossip.service.IndexWriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

//@Component
public class IndexWriterTiming {
    @Autowired
    private IndexWriterService indexWriterService;

    private boolean flag = false;
    private Integer num = 1;

    //定时方法：假设每分钟执行一次
    @Scheduled(cron = "* */30 * * * *")
    public void timing(){
        try {
            System.out.println(new Date().toLocaleString());
            if (num == 1) {
                num++;
                flag = indexWriterService.saveBean();
                return;
            }
            if (!flag) {
                return;
            }
            flag = false;
            flag = indexWriterService.saveBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
