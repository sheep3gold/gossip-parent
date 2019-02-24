package cn.itcast.gossip.service.impl;

import cn.itcast.gossip.pojo.News;
import cn.itcast.gossip.pojo.PageBean;
import cn.itcast.gossip.pojo.ResultBean;
import cn.itcast.gossip.service.PortalIndexSearchService;
import cn.itcast.search.service.IndexSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PortalIndexSearchServiceImpl implements PortalIndexSearchService {
    @Autowired
    private JedisPool jedisPool;

    @Reference
    private IndexSearchService indexSearchService;


    @Override
    public ResultBean findByQuery(ResultBean resultBean) throws Exception {
        //调用solr服务
        resultBean = indexSearchService.findByQuery(resultBean);

        /*//修改内容
        for (News news : newsList) {
            String content = news.getContent();
            if (content != null && content.length() > 100) {
                content = content.substring(0, 99) + "...";
                news.setContent(content);
            }
        }*/

        //2.处理查询的数据
        /*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (News news : resultBean.getPageBean().getNewsList()) {
            String time = news.getTime();
            if (time != null && !"".equals(time)) {
                Date date = format.parse(time);
                date.setTime(date.getTime() - (1000 * 60 * 60 * 8));
                time = format.format(date);
                news.setTime(time);
            }
        }*/

        //直接返回给controller
        return resultBean;
    }

    //带分页的条件查询
    /*@Override
    public PageBean findByPageQuery(ResultBean resultBean) throws Exception {
        //1.调用solr的服务
        PageBean pageBean = indexSearchService.findByPageQuery(resultBean);

        //2.将news中的content进行街区70个字符
        List<News> newsList = pageBean.getNewsList();
        for (News news : newsList) {
            String content = news.getContent();
            if (content != null && content.length() > 100) {
                content = content.substring(0, 69) + "...";
                news.setContent(content);
            }
        }
        return pageBean;
    }*/

    //获取热搜关键词
    @Override
    public List<Map<String,Object>> findByRedisTopKeyWords(Integer num) {
        //1.获取连接
        Jedis redis = jedisPool.getResource();
        //2.获取热搜词
        Set<Tuple> tupleSet = redis.zrevrangeWithScores("bigData:gossip:topkey",0,num);

        //3.处理热搜词
        List<Map<String,Object>> topList = new ArrayList<>();
        for (Tuple tuple : tupleSet) {
            String element = tuple.getElement();//热搜词
            double topVal = tuple.getScore();//热度值

            Map<String, Object> map = new HashMap<>();
            map.put("topKeys", element);
            map.put("score", topVal);
            topList.add(map);
        }
        //4.关闭redis
        redis.close();
        //5.返回数据
        return topList;
    }
}
