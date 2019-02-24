package cn.itcast.gossip.mapper;

import cn.itcast.gossip.pojo.News;

import java.util.List;

public interface NewsMapper {
    //根据上次id获取100条数据的方法
    public List<News> findByNextMaxIdToNews(String nextMaxId);

    //获取100条数据中的最大值
    public String findByNextMaxIdToMaxId(String nextMaxId);
}
