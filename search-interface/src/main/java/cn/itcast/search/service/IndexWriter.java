package cn.itcast.search.service;

import cn.itcast.gossip.pojo.News;

import java.util.List;

public interface IndexWriter {
    public void saveBean(List<News> beanList) throws Exception;
}
