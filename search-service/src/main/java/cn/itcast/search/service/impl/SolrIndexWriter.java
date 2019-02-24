package cn.itcast.search.service.impl;

import cn.itcast.gossip.pojo.News;
import cn.itcast.search.service.IndexWriter;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrServer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class SolrIndexWriter implements IndexWriter {
    @Autowired
    private SolrServer solrServer;

    @Override
    public void saveBean(List<News> beanList) throws Exception {
        solrServer.addBeans(beanList);
        solrServer.commit();
    }
}
