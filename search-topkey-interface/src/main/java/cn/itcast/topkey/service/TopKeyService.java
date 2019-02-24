package cn.itcast.topkey.service;

/**
 * 根据热搜关键词生成搜索结果的缓存数据
 */
public interface TopKeyService {
    public void findByPageKeywordsToRedis(String keyword) throws Exception;

}
