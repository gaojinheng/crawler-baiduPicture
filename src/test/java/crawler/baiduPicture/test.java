package crawler.baiduPicture;

import crawler.baiduPicture.service.Crawler;
import org.junit.Test;

public class test {

    private Crawler crawler = new Crawler();
    @Test
    public void intallPicture() throws Exception {
        //设置搜索对象
        crawler.setSearchName("郑恩地");

        //设置每页获取图片数
        crawler.setPicNum(60);

        //设置获取多少页
        crawler.setPageNum(10);

        //爬取图片
        crawler.CrawlerBaiduPicture();


    }
}
