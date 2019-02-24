package cn.itcast.topkey.web;

import cn.itcast.topkey.service.TopKeyService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TopKeyConsumer implements MessageListener<String, String> {

    @Autowired
    private TopKeyService topKeyService;

    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        try {
            //从kafka中获取热词
            String topkey = record.value();

//            System.out.println(topkey);
            //调用热词消费的服务类，消费热词数据
            topKeyService.findByPageKeywordsToRedis(topkey);
            System.out.println("消费完成..."+topkey);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
