package cn.itcast.gossip.service.impl;

import cn.itcast.gossip.mapper.NewsMapper;
import cn.itcast.gossip.pojo.News;
import cn.itcast.gossip.service.IndexWriterService;
import cn.itcast.search.service.IndexWriter;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class IndexWriterServiceImpl implements IndexWriterService {
    @Autowired
    private NewsMapper newsMapper;

    @Reference(timeout = 5000)
    private IndexWriter indexWriter;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public boolean saveBean() throws Exception {
        //1.从redis中获取上一次获取的最大值:存储在String
        Jedis jedis = jedisPool.getResource();
        String nextMaxId = jedis.get("bigData:search:nextMaxId");
        jedis.close();
        //2.判断是否有最大id值
        if (nextMaxId == null || "".equals(nextMaxId)) {
            nextMaxId = "0";
        }
        while (true) {
            System.out.println("调用数据库，查询数据");
            //3.调用mapper,获取数据
            List<News> newsList = newsMapper.findByNextMaxIdToNews(nextMaxId);

            if (newsList == null || newsList.size() == 0) {
                System.out.println("没有数据了");
                break;
            }

            nextMaxId = newsMapper.findByNextMaxIdToMaxId(nextMaxId);

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

            for (News news : newsList) {
                String time = news.getTime();
                if (time == null) {
                    continue;
                }
                Date parse = format1.parse(time);
                String newTime = format2.format(parse);
                news.setTime(newTime);
            }

            indexWriter.saveBean(newsList);

            if (newsList.size() < 100) {
                System.out.println("剩下的数据小于100");
                break;
            }

        }

        jedis = jedisPool.getResource();
        jedis.set("bigData:search:nextMaxId", nextMaxId);
        jedis.close();
        return true;
    }
}
