package cn.itcast.gossip.controller;

import cn.itcast.gossip.pojo.News;
import cn.itcast.gossip.pojo.PageBean;
import cn.itcast.gossip.pojo.ResultBean;
import cn.itcast.gossip.service.PortalIndexSearchService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Controller
public class PortalIndexSearchController {
    @Autowired
    private PortalIndexSearchService portalIndexSearchService;

    @RequestMapping("/s")
    @ResponseBody
    public ResultBean findByQuery(ResultBean resultBean) {
        try {
            //接收其前端参数

            //判断是否接收到了参数
            if (resultBean == null) {
                return new ResultBean("参数不正常，非法操作", false);
            }
            if (resultBean.getKeywords() == null || "".equals(resultBean.getKeywords())) {
                //返回首页
                return new ResultBean("参数不正常，非法操作", false);
            }
            if (resultBean.getPageBean() == null) {
                resultBean.setPageBean(new PageBean());
            }

            //调用service
//            List<News> newsList = null;
            resultBean = portalIndexSearchService.findByQuery(resultBean);

            //返回给前端
            return resultBean;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResultBean("系统在维护，请稍后...", false);
        }
    }

    /*@RequestMapping("/ps")
    @ResponseBody
    public PageBean findByPageQuery(ResultBean resultBean) {

        try {
            //1.判断关键词是否正常接收
            if (resultBean == null) {
                //返回首页
                return null;
            }
            if (resultBean.getKeywords() == null || "".equals(resultBean.getKeywords())) {
                //返回首页
                return null;
            }
            //如果没有pageBean表示用户在前端没有传递当前页和每页的条数，但是依然进行查询
            if (resultBean.getPageBean() == null) {
                resultBean.setPageBean(new PageBean());
            }

            //2.调用service
            PageBean pageBean = portalIndexSearchService.findByPageQuery(resultBean);
            return pageBean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    @RequestMapping("/top")
    @ResponseBody
    public List<Map<String,Object>> findByTopKeywords(Integer num) {
        //1.判断num是否正确传递
        if (num == null || num <= 0) {
            num = 5;
        }

        //2.调用service层
        List<Map<String,Object>> topList = portalIndexSearchService.findByRedisTopKeyWords(num);
        //3.返回给前端
        return topList;
    }
}
