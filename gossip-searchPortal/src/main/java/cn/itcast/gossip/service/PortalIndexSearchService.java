package cn.itcast.gossip.service;

import cn.itcast.gossip.pojo.News;
import cn.itcast.gossip.pojo.PageBean;
import cn.itcast.gossip.pojo.ResultBean;

import java.util.List;
import java.util.Map;

public interface PortalIndexSearchService {
    public ResultBean findByQuery(ResultBean resultBean) throws Exception;

//    public PageBean findByPageQuery(ResultBean resultBean) throws Exception;

    public List<Map<String,Object>> findByRedisTopKeyWords(Integer num);
}
