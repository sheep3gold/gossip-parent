package cn.itcast.gossip.pojo;

import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

public class News implements Serializable {
    @Field
    private long id;//id
    @Field
    private String title; //新闻的标题
    @Field
    private String docurl; // 新闻的url
    @Field
    private String time;  //时间
    @Field
    private String label; //
    @Field
    private String source;
    @Field
    private String content; //新闻的正文
    @Field
    private String editor; //新闻的编辑

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDocurl() {
        return docurl;
    }

    public void setDocurl(String docurl) {
        this.docurl = docurl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {

        this.editor = editor;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", docurl='" + docurl + '\'' +
                ", time='" + time + '\'' +
                ", label='" + label + '\'' +
                ", source='" + source + '\'' +
                ", content='" + content + '\'' +
                ", editor='" + editor + '\'' +
                '}';
    }
}
