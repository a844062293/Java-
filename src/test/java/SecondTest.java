import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 第一个爬虫测试
 * Created by DuFei on 2017/7/27.
 */
public class SecondTest {
    public static void main(String[] args) throws IOException {
        //建立一个新的请求客户端
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //使用HttpGet方式请求网址
        HttpGet httpGet = new HttpGet("https://www.ithome.com/");
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

            // System.out.println(content);
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> titles = new ArrayList<String>();
        List<String> urls = new ArrayList<String>();
        //假设我们获取的HTML的字符内容如下

        Document doc = Jsoup.parse(content);
        //第二步，根据我们需要得到的标签，选择提取相应标签的内容
        Elements elements = doc.select("div[class=lst lst-1 new-list]").select("li[class=new]");
        for( Element element : elements ){

            String title = element.select("a[target=_blank]").text();
            titles.add(title);
            urls.add(element.select("a").attr("href"));

        }
        //输出测试
        for( int j = 0 ;j< titles.size();j++){
            String title = titles.get(j);
            String url = urls.get(j);
            System.out.println(title+":");
            System.out.println(url);
            System.out.println(titles.size());
        }



    }
}

