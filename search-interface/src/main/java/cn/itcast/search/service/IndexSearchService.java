package cn.itcast.search.service;

import cn.itcast.gossip.pojo.News;
import cn.itcast.gossip.pojo.PageBean;
import cn.itcast.gossip.pojo.ResultBean;

import java.util.List;

public interface IndexSearchService {
    public ResultBean findByQuery(ResultBean resultBean) throws Exception;

//    public PageBean findByPageQuery(ResultBean resultBean) throws Exception;

}
