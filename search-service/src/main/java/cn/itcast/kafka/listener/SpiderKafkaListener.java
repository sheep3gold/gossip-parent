package cn.itcast.kafka.listener;

import cn.itcast.gossip.pojo.News;
import cn.itcast.search.service.IndexWriter;
import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
public class SpiderKafkaListener implements MessageListener<String,String> {

    @Autowired
    private IndexWriter indexWriter;

    private static Gson gson = new Gson();

    private static SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'H'HH:mm:ss'Z'");


    @Override
    public void onMessage(ConsumerRecord<String, String> record) {
        try {
            //1.接收到kafka的数据
            String newsJson = record.value();

            //2.将这个json数据转换为news对象
            System.out.println(newsJson);
            News news = gson.fromJson(newsJson, News.class);

            //3.将news中日期转换为UTC格式
            String time = news.getTime();
            if (time!=null&&!"".equals(time)) {
                Date date = format1.parse(time);
                time = format2.format(date);
                news.setTime(time);
            }
            List<News> newsList = Arrays.asList(news);
            indexWriter.saveBean(newsList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
