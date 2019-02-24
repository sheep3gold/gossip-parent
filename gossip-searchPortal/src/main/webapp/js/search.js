//1. 获取路径上的请求参数
var kv = window.location.search;
var v = kv.split("=")[1];
v = decodeURI(v);

v = v.replace(/\+/g, " ");
// v = v.replace(/%2B/g, "\+");
//2. 判断是否获取到
if (kv == null || kv == "") {
    // 此时都需要跳转到index.html
    window.location.href = "/index.html";
}
//3. 将内容写入到搜索框中
$("#inputSeach").val(v);

//3. 异步请求,获取结果
// ajaxQuery(1,15);
ajaxQuery(1,15);
// alert(keyWords);



function ajaxQuery(page,pageSize) {
    // alert(1);
    //清空数据
    $(".itemList").html("");
    // 获取关键字
    var keywords = $("#inputSeach").val();
    // alert(keywords);
    //获取日期范围
    var dateStart = $("input[name=dateStart]").val();
    var dateEnd = $("input[name=dateEnd]").val();
    //获取来源
    var source = $("input[name=source]").val();
    //获取编辑
    var editor = $("input[name=editor]").val();

    var params = {"keywords": keywords,"dateStart":dateStart,
        "dateEnd":dateEnd,"source":source, "editor": editor,
        "pageBean.page":page,"pageBean.pageSize":pageSize};
    $.post("/s.action", params, function (data) {

        if (!data.flag) {
            alert(data.error);
            window.location.href = "/index.html";
        }

        //1. 将响应回来的数据写入页面即可
        var divStr = "";
        $(data.pageBean.newsList).each(function () {

            var docurl = this.docurl;
            docurl = docurl.substring(7, 20);

            divStr += "<div class=\"item\">\n" +
                "\t\t\t\t<div class=\"title\"><a href=\"" + this.docurl + "\">" + this.title + "</a></div>\n" +
                "\t\t\t\t<div class=\"contentInfo_src\">\n" +
                "\t\t\t\t\t<a href=\"#\"><img src=\"../img/item.jpeg\" alt=\"\" class=\"imgSrc\" width=\"121px\" height=\"75px\"></a>\n" +
                "\t\t\t\t\t<div class=\"infoBox\">\n" +
                "\t\t\t\t\t\t<p class=\"describe\">\n" +
                "\t\t\t\t\t\t\t" + this.content + "\n" +
                "\t\t\t\t\t\t</p>\n" +
                "\t\t\t\t\t\t<p><a class=\"showurl\" href=\"" + this.docurl + "\">" + docurl + " " + this.time + "</a> <span class=\"lab\">" + this.editor + " - " + this.source + "</span></p>\n" +
                "\t\t\t\t\t</div>\n" +
                "\t\t\t\t</div>\n" +
                "\t\t\t</div>";
        });
        $(".itemList").html(divStr);

        var pageStr = "<ul>";
        //添加首页
        pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(1,15)'>首页</a></li>";
        //实现上一页：当前页-1操作
        //禁用属性：javascript:void(0)
        var page = data.pageBean.page;
        var pageNumber = data.pageBean.pageNumber;
        if (page > 1) {
            pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(" + (page - 1) + ",15)'>< 上一页</a></li>";
        }
        //实现页码：默认只展示7个页码

        //1)如果总页数小于等于7页，将全部显示
        //2）如果总页数大于7页？1，2，3，4，5，6，7，8，9，10，11
        //	2.1如果当前页的页码是1-4之间的数字，展示前7页
        //	2.2如果当前页的页码是大于4，并且小于最大页-2，展示[当前页-3～最大页+3]
        //	2.3如果当前页大于最大页-2，展示[最大页-6～最大页]
        if (pageNumber <= 7) {
            //1)如果总页数小于等于7页，将全部显示
            for (var i = 1; i <= pageNumber; i++) {
                if (i == page) {
                    pageStr += "<li class='on'>" + i + "</li>";
                }else {
                    pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(" + i + ",15)'>"+i+"</a></li>";
                }
            }
        }else {
            //2）如果总页数大于7页？1，2，3，4，5，6，7，8，9，10，11
            //	2.1如果当前页的页码是1-4之间的数字，展示前7页
            if (page >= 1 && page <= 4) {
                for (var i = 1; i <= 7; i++) {
                    if (i == page) {
                        pageStr += "<li class='on'>" + i + "</li>";
                    }else {
                        pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(" + i + ",15)'>"+i+"</a></li>";
                    }
                }
            }
            //	2.2如果当前页的页码是大于4，并且小于最大页-2，展示[当前页-3～最大页+3]
            if (page > 4 && page < (pageNumber - 2)) {
                for (var i=(page-3);i<=(page+3);i++){
                    if (i == page) {
                        pageStr += "<li class='on'>" + i + "</li>";
                    }else {
                        pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(" + i + ",15)'>"+i+"</a></li>";
                    }
                }
            }
            //	2.3如果当前页大于最大页-2，展示[最大页-6～最大页]
            if (page >= pageNumber - 2) {
                for (var i = pageNumber - 6; i <= pageNumber; i++) {
                    if (i == page) {
                        pageStr += "<li class='on'>" + i + "</li>";
                    }else {
                        pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(" + i + ",15)'>"+i+"</a></li>";
                    }
                }
            }
        }

        //实现下一页
        if (page < pageNumber) {
            pageStr+="<li><a href='javascript:void(0)' onclick='ajaxQuery("+(page+1)+",15)'>下一页 ></a></li>"
        }

        //添加尾页
        pageStr += "<li><a href='javascript:void(0)' onclick='ajaxQuery(" + pageNumber + ",15)'>末页</a></li>";

        pageStr += "</ul>";
        //将分页条设置到页面中
        $(".pageList").html(pageStr);
    }, "json");

    //排名
    $.get("/top.action",{"num":9},function (data) {
        var i = 0;
        var rankList = "<div class=\"topLab\">排名 <span>搜索指数</span></div>";
        $(data).each(function () {
            i++;
            var topKey = this.topKeys;
            var score = this.score;
            /**
             * <div class="topLab">排名 <span>搜索指数</span></div>
             <li class="A"><a href=""><span>1</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li class="B"><a href=""><span>2</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li class="C"><a href=""><span>3</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li><a href=""><span>4</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li><a href=""><span>5</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li><a href=""><span>6</span>新闻联播 下等人 <span>1345<em class="add"></em></span></a></li>
             <li><a href=""><span>7</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li><a href=""><span>8</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <li><a href=""><span>9</span>新闻联播 下等人 <span>1345<em class="add"></em></span></a></li>
             <li><a href=""><span>10</span>新闻联播 下等人 <span>1345<em></em></span></a></li>
             <div class="source"> 数据来源 百度数据库</div>
             */
            if (i == 1) {
                rankList+="<li class=\"A\"><a href=\"\"><span>1</span>"+topKey+"<span>"+score+"<em></em></span></a></li>\n"
            }
            else if (i==2){
                rankList+="<li class=\"B\"><a href=\"\"><span>2</span>"+topKey+"<span>"+score+"<em></em></span></a></li>\n"
            }
            else if (i == 3) {
                rankList+="<li class=\"C\"><a href=\"\"><span>3</span>"+topKey+"<span>"+score+"<em></em></span></a></li>\n"
            } else {
                rankList += "<li><a href=\"\"><span>" + i + "</span>" + topKey + "<span>" + score + "<em></em></span></a></li>\n"
            }

        })
        $(".seachItem").html(rankList);
    },"json")
}



function ajaxTopKey(num) {
    var url = "/top.action";
    $.get(url, {"num": num}, function (data) {
        var divStr = "";
        $(data).each(function () {
            var topKey = this.topKeys;
            var score = this.score;
            divStr += "<div class='item' onclick='ajaxTop(this)'><span>"+topKey+"</span>" + topKey + "<span style='float: right;color: red'>" + score + "</span></div>";
        })
        $(".recommend").html(divStr);
    }, "json");
}

function ajaxTop(obj) {
    var topkey = $(obj).children(":first").text();
    window.location.href = "/list.html?keywords=" + topkey;
}

