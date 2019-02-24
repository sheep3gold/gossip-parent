package cn.itcast.gossip.pojo;

import java.io.Serializable;
import java.util.List;

/**
 * 分页的pageBean
 * @author zjl
 * @create 2018-10-08 15:11
 **/
public class PageBean implements Serializable {

    private  Integer page=1; //当前页
    private  Integer pageSize=15 ; //每页显示的条数
    private  Integer pageCount ; // 总条数
    private  Integer pageNumber ; //总页数
    private List<News> newsList ; //每页的数据

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {

        this.newsList = newsList;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "page=" + page +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", pageNumber=" + pageNumber +
                ", newsList=" + newsList +
                '}';
    }
}
