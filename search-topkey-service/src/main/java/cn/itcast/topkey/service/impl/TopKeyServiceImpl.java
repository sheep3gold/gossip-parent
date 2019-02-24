package cn.itcast.topkey.service.impl;

import cn.itcast.gossip.pojo.PageBean;
import cn.itcast.gossip.pojo.ResultBean;
import cn.itcast.search.service.IndexSearchService;
import cn.itcast.topkey.service.TopKeyService;
import cn.itcast.topkey.util.JedisUtil;
import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service //这个服务不需要任何人调用，所以将其放在spring容器中即可，不需要发布成一个服务
public class TopKeyServiceImpl implements TopKeyService {
    @Reference
    private IndexSearchService indexSearchService;

    @Override
    public void findByPageKeywordsToRedis(String keyword) throws Exception {
        //keyword参数：极为热搜词词语
        //1.循环发送（最大查询3页）查询solr索引库，返回resultBean对象：第一页数据
        ResultBean resultBean = new ResultBean();
        resultBean.setPageBean(new PageBean());
        resultBean.setKeywords(keyword);
//        System.out.println(keyword);
//        System.out.println(resultBean.getKeywords());

        resultBean = indexSearchService.findByQuery(resultBean);
        resultBean.setKeywords(keyword);
//        System.out.println(resultBean.getPageBean().getPageCount());
//        System.out.println(resultBean.getKeywords());
        System.out.println(resultBean);
        //循环查询前三页数据
        int num = 3;
        if (resultBean.getPageBean().getPageCount() <= 3) {
            num = resultBean.getPageBean().getPageCount();
            System.out.println(num+"内");
        }

        System.out.println(num+"外");
        //循环获取数据
        for (int i = 1; i <= num; i++) {
            resultBean.getPageBean().setPage(i);
            System.out.println(resultBean.getPageBean().getPageCount());
            ResultBean resultBean2 = indexSearchService.findByQuery(resultBean);
            System.out.println(resultBean2);

            //2.将每一页的resultBean转换为一个json数据
            Gson gson = new Gson();
            String resultBeanJson = gson.toJson(resultBean2);
//            System.out.println(resultBeanJson);
            //3.将这个json数据保存草redis中
            Jedis jedis = JedisUtil.getJedis();
            jedis.set("gossip:" + keyword + ":" + i, resultBeanJson);
            jedis.close();
        }
    }
}
