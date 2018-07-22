import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.*;


import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
/**
 * 第一个爬虫测试
 * Created by DuFei on 2017/7/27.
 */
public class FirstTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        for (int k = 0 ;k <10;k++){
        String className = "com.mysql.jdbc.Driver";
         String dbname="company";   //你创建的数据库名字
         String dbuname="root";    //登陆数据库的账号，默认为root
          String dbupwd="VB110119asd";  //登陆密码
         String url1="jdbc:mysql://127.0.0.1:3306/"+dbname+"?user="+dbuname+"&password="+dbupwd+"?useUnicode=true&characterEncoding=utf8";
        //建立一个新的请求客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //使用HttpGet方式请求网址

        HttpGet httpGet = new HttpGet("http://www.maxlaw.cn/news/toutiao?curpage="+k+3);
        //获取网址的返回结果
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取返回结果中的实体
        HttpEntity entity = response.getEntity();
        //将返回的实体输出
        String content = EntityUtils.toString(entity,"utf-8");
        try {

            //System.out.println(content);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> titles = new ArrayList<String>();
        List<String> urls = new ArrayList<String>();
        //假设我们获取的HTML的字符内容如下

        Document doc = Jsoup.parse(content);
        //第二步，根据我们需要得到的标签，选择提取相应标签的内容
        Elements elements = doc.select("div[class=otherbox_left_list]").select("li[class=clearfix]");
        for( Element element : elements ){

           String title = element.select("div[class=list_t_left]").select("a[target=_blank]").text();
            titles.add(title);
            urls.add(element.select("div[class=list_t_left]").select("a[target=_blank]").attr("href"));

        }
        //加载数据库驱动
        Class.forName(className);
        //创建数据库连接
        java.sql.Connection conn = null;
        try {
            for (int i = 0 ; i < titles.size() ; i ++){
            conn = DriverManager.getConnection(url1, dbuname,dbupwd);
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO `new`(new_titles,new_url) VALUES (?,?)");
            //pstmt.setInt(1,i);
            pstmt.setString(1,titles.get(i));
            pstmt.setString(2,"http://www.maxlaw.cn"+urls.get(i));

                boolean rs = pstmt.execute();
                System.out.println(rs);
                conn.close();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //创建执行sql语句的对象

        //输出测试
        for( int j = 0 ;j< titles.size();j++){
            String title = titles.get(j);
            String url = urls.get(j);
            System.out.println(title+":");
            System.out.println("http://www.maxlaw.cn"+url);
            System.out.println(titles.size());
        }



    }
}}