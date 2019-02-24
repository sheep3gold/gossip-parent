package cn.itcast.search.service.impl;

import cn.itcast.gossip.pojo.News;
import cn.itcast.gossip.pojo.PageBean;
import cn.itcast.gossip.pojo.ResultBean;
import cn.itcast.search.service.IndexSearchService;
import com.alibaba.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IndexSearchServiceImpl implements IndexSearchService {
    @Autowired
    private SolrServer solrServer;

    @Override
    public ResultBean findByQuery(ResultBean resultBean) throws Exception {
        //封装查询条件：查询新闻信息,展示时需要进行排序吗？
        SolrQuery solrQuery = new SolrQuery(resultBean.getKeywords());

        //保证新闻展示时候是按照由近到远的
        solrQuery.setSort("time", SolrQuery.ORDER.desc);

        //添加高亮设置
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //封装搜索的条件：可选的条件
        //日期的气质时间条件
        String dateStart = resultBean.getDateStart();
        String dateEnd = resultBean.getDateEnd();
        if (dateStart != null && !"".equals(dateStart) && dateEnd != null && !"".equals(dateEnd)) {
            SimpleDateFormat format1 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            //日期的起止是正常传递了，日期的格式问题
            //02/04/2019 12:14:50
            Date oldDateStart = format1.parse(dateStart);
            String newDateStart = format2.format(oldDateStart);

            Date oldDateEnd = format1.parse(dateEnd);
            String newDateEnd = format2.format(oldDateEnd);

            solrQuery.addFilterQuery("time:[" + newDateStart + " TO " + newDateEnd + "]");
        }
        //封装编辑条件
        String editor = resultBean.getEditor();
        if (editor != null && !"".equals(editor)) {
            solrQuery.addFilterQuery("editor:" + editor);
        }

        //封装来源条件
        String source = resultBean.getSource();
        if (source != null && !"".equals(source)) {
            solrQuery.addFilterQuery("source:" + source);
        }

        //封装分页的条件
        Integer page = resultBean.getPageBean().getPage();
        Integer pageSize = resultBean.getPageBean().getPageSize();

        solrQuery.setStart((page - 1) * pageSize);//起始条数
        solrQuery.setRows(pageSize);//每页的条数

        //执行查询
        QueryResponse response = solrServer.query(solrQuery);

        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        //获取数据，返回
        SolrDocumentList documentList = response.getResults();
        resultBean = resutlNewsList(documentList);

        for (News news : resultBean.getPageBean().getNewsList()) {
            Map<String, List<String>> listMap = map.get(news.getId() + "");
            List<String> list = listMap.get("title");
            if (list != null && list.size() > 0) {
                news.setTitle(list.get(0));
            }
            list = listMap.get("content");
            if (list != null && list.size() > 0) {
                news.setContent(list.get(0));
            }
        }

        Long pageCount = documentList.getNumFound();//获取总条数
        resultBean.getPageBean().setPageCount(pageCount.intValue());//封装总条数

        //总页数：总条数/每页总条数
        Double pageNumber = Math.ceil((double) pageCount / pageSize);
        resultBean.getPageBean().setPageNumber(pageNumber.intValue());
        resultBean.getPageBean().setPage(page);
        resultBean.getPageBean().setPageSize(pageSize);
        return resultBean;
    }

    /*@Override
    public PageBean findByPageQuery(ResultBean resultBean) throws Exception {
        Integer page = resultBean.getPageBean().getPage();
        Integer pageSize = resultBean.getPageBean().getPageSize();

        //1.封装查询的条件
        //1.1 基本搜索
        SolrQuery solrQuery = new SolrQuery("text:" + resultBean.getKeywords());
        //1.2 高亮展示数据的设置
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("content");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //1.3 搜索工具的实现
        //编辑 和 来源
        String editor = resultBean.getEditor();
        if (editor != null && !"".equals(editor)) {
            //说明前端传递了编辑了
            solrQuery.addFilterQuery("editor:" + editor);
        }

        String source = resultBean.getSource();
        if (source != null && !"".equals(source)) {
            //说明前端传递了来源
            solrQuery.addFilterQuery("source:" + source);
        }

        //日期范围条件封装
        String dateStart = resultBean.getDateStart();
        String dateEnd = resultBean.getDateEnd();
        System.out.println("222222");
        if (dateStart != null && !"".equals(dateStart) && dateEnd != null && !"".equals(dateEnd)) {
            System.out.println("333333");
            //日期范围查询：UTC格式：yyyy-MM-dd'T'HH:mm:ss'Z'
            // 12/10/2018 16:18:25 : dd/MM/yyyy HH:mm:ss
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'z'");
            SimpleDateFormat format2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

            Date start = format2.parse(dateStart);
            Date end = format2.parse(dateEnd);

            dateStart = format1.format(start);
            dateEnd = format1.format(end);

            solrQuery.addFilterQuery("time:[" + dateStart + " TO " + dateEnd + "]");
        }

        //1.4日期进行排序 倒序
        solrQuery.setSort("time", SolrQuery.ORDER.desc);
        //1.5分页参数的封装
        solrQuery.setStart((page - 1) * pageSize);
        solrQuery.setRows(pageSize);

        //2.执行查询
        QueryResponse response = solrServer.query(solrQuery);
        SolrDocumentList documentList = response.getResults();
        //3.获取结果
        ArrayList<News> newsList = resutlNewsList(documentList);

        long pageCount = documentList.getNumFound();
        //4.封装pageBean
        PageBean pageBean = resultBean.getPageBean();
        //4.1封装的总条数
        pageBean.setPageCount((int) pageCount);
        //4.2封装当前页数据
        pageBean.setNewsList(newsList);
        //4.3封装总页数
        Integer pageCount1 = pageBean.getPageCount();
        double pageNumber = Math.ceil((double) pageCount / pageSize);//向上取整
        pageBean.setPageNumber((int) pageNumber);

        return pageBean;
    }*/

    //专门用来处理数据
    private ResultBean resutlNewsList(SolrDocumentList documentList) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ArrayList<News> newsList = new ArrayList<>();
        for (SolrDocument document : documentList) {
            News news = new News();

            String id = (String) document.get("id");
            news.setId(Long.parseLong(id));

            String title = (String) document.get("title");
            news.setTitle(title);

            Date timeDate = (Date) document.get("time");
            if (timeDate!=null && !"".equals(timeDate)) {
                timeDate.setTime(timeDate.getTime() - (1000 * 60 * 60 * 8));
                String time = simpleDateFormat.format(timeDate);
                news.setTime(time);
            }
            String source = (String) document.get("source");
            news.setSource(source);

            String content = (String) document.get("content");
            news.setContent(content);

            String editor = (String) document.get("editor");
            news.setEditor(editor);

            String docurl = (String) document.get("docurl");
            news.setDocurl(docurl);

            newsList.add(news);
        }
        ResultBean resultBean = new ResultBean();
        PageBean pageBean = new PageBean();
        resultBean.setPageBean(pageBean);
        resultBean.getPageBean().setNewsList(newsList);
        return resultBean;
    }
}
