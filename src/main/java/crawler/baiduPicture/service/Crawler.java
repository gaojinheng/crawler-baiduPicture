package crawler.baiduPicture.service;


import crawler.baiduPicture.util.HttpUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.util.HashSet;

public class Crawler {
    private String searchName;//搜索时图片输入的名字
    private int picNum;//一次请求得到图片的url的数量，不大于60
    private int pageNum;//获取多少页
    private HttpUtils httpUtils;//工具类

    public void CrawlerBaiduPicture() throws Exception {
        //1. 打开浏览器，创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        //2. 创建工具类对象
        HttpUtils httpUtils = new HttpUtils();

        //按照页码搜索结果进行遍历解析
        for (int i = 0; i < pageNum; i++) {

            //声明要解析的初始地址

            String url = "https://image.baidu.com/search/acjson?tn=resultjson_com&logid=6200883187742644854&ipn=rj&ct=201326592&is=&fp=result&queryWord=" + searchName + "&cl=2&lm=-1&ie=utf-8&oe=utf-8&adpicid=&st=-1&z=&ic=0&hd=&latest=&copyright=&word=" + searchName + "&s=&se=&tab=&width=&height=&face=0&istype=2&qc=&nc=1&fr=&expermode=&force=&pn=" + (i + 1) * picNum + "&rn=" + picNum + "&gsm=3c&1619267607521=";

            //获取html页面（这里我们实际获取的是json）
            String html = httpUtils.doGetHtml(url);

            //根据html获取图片的url的set集合
            HashSet<String> urlSet = httpUtils.doGetUrl(html);
            for (String urlset : urlSet) {
                //遍历set集合，下载图片
                httpUtils.doGetImage(urlset);
            }
        }
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
}
