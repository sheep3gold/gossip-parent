package cn.itcast.gossip.pojo;

import java.io.Serializable;

/**
 * 用于接收前端的参数和返回的前端数据的对象
 * @author zjl
 * @create 2018-10-08 15:16
 **/
public class ResultBean implements Serializable {
    public ResultBean() {
    }

    // 此构造用于快捷封装错误结果
    public ResultBean(String error, boolean flag) {
        this.error = error;
        this.flag = flag;
    }

    // 搜索关键字
    private String keywords;//关键词

    // 分页
    private  PageBean pageBean;

    //错误结果返回
    private String error = "ok"; // 错误信息

    private boolean flag = true; // 是否正确, 如有错误, 设置为false,并添加错误信息


    //搜索工具: 1) 日期参数
    private String dateStart ; //起始值
    private  String  dateEnd ;  // 结束值

    // 搜索工具: 2) 来源参数
    private String source;
    //搜索工具: 3) 新闻编辑
    private String editor;

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public PageBean getPageBean() {
        return pageBean;
    }

    public void setPageBean(PageBean pageBean) {

        this.pageBean = pageBean;
    }

    @Override
    public String toString() {
        return "ResultBean{" +
                "keywords='" + keywords + '\'' +
                ", pageBean=" + pageBean +
                ", error='" + error + '\'' +
                ", flag=" + flag +
                ", dateStart='" + dateStart + '\'' +
                ", dateEnd='" + dateEnd + '\'' +
                ", source='" + source + '\'' +
                ", editor='" + editor + '\'' +
                '}';
    }
}
