package crawler.baiduPicture.util;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpUtils {

    private PoolingHttpClientConnectionManager cm;
    private static int fileName = 1;
    public HttpUtils() {
        this.cm = new PoolingHttpClientConnectionManager();

        //设置最大连接数
        cm.setMaxTotal(100);

        //设置每个主机的最大连接数
        cm.setDefaultMaxPerRoute(10);
    }

    /**
     * 根据请求地址加载页面数据并返回
     * @param url
     * @return
     */
    public String doGetHtml(String url){
        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        //设置httpClient请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(getConfig());

        //设置User-Agent
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/89.0.4389.90 Safari/537.36");

        CloseableHttpResponse response = null;
        try {
            //使用httpClient发起请求，获取响应
            response = httpClient.execute(httpGet);

            //解析响应，返回结果
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否不为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null){
                    String content = EntityUtils.toString(response.getEntity());
                    return content;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭response,httpClient已交给连接池管理，不用关闭
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        返回空字符串
        return "";
    }

    /**
     * 根据图片的url下载图片
     * @param url
     * @return
     */
    public String doGetImage(String url){
        //获取httpClient对象
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();

        //设置httpClient请求对象，设置url地址
        HttpGet httpGet = new HttpGet(url);

        //设置请求信息
        httpGet.setConfig(getConfig());

        CloseableHttpResponse response = null;
        try {
            //使用httpClient发起请求，获取响应

            response = httpClient.execute(httpGet);
            //解析响应，返回结果
            if (response.getStatusLine().getStatusCode() == 200){
                //判断响应体Entity是否不为空，如果不为空就可以使用EntityUtils
                if (response.getEntity() != null){
                    //下载图片
                    //获取图片的后缀
                    String extName = url.substring(url.lastIndexOf("."));//从最后一个.开始截取

                    //下载图片
                    File file = new File("C:\\Users\\86134\\Desktop\\images");
                    if (!file.exists()){
                        file.mkdir();
                    }
                    OutputStream outputStream = new FileOutputStream(new File("C:\\Users\\86134\\Desktop\\images\\" + fileName++ + extName));

                    response.getEntity().writeTo(outputStream);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭response,httpClient已交给连接池管理，不用关闭
            if (response != null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

//        如果下载失败返回空字符串
        return "";
    }

    /**
     * 获取的html页面中的url
     * @param html
     * @return
     */
    public HashSet<String> doGetUrl(String html) throws Exception {
        HashSet<String> urlSet = new HashSet<String>();


        String regex = "(ht|f)tp(s?)\\:\\/\\/[0-9a-zA-Z]([-.\\w]*[0-9a-zA-Z])*(:(0-9)*)*(\\/?)([a-zA-Z0-9\\-\\.\\?\\,\\'\\/\\\\&%\\+\\$#_=]*)?";

        Pattern pat = Pattern.compile(regex);
        Matcher mat = pat.matcher(html);
        int count = 0;
        while(mat.find()) {
            count++;
            String group = mat.group();
            urlSet.add(group);
        }
        return urlSet;

    }

    //设置请求信息
    private RequestConfig getConfig() {
        RequestConfig config = RequestConfig.custom()
                .setConnectTimeout(1000)//创建连接的最长时间
                .setConnectionRequestTimeout(500)//获取时间的最长时间
                .setSocketTimeout(10000)//数据传输的最长时间
                .build();
        return config;
    }
}
