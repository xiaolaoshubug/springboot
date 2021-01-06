package com.oay.utils;

import com.oay.entity.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import javax.jws.Oneway;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*********************************************************
 * @Package: com.oay.utils
 * @ClassName: HtmlParseUtil.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/
public class HtmlParseUtil {

    /**
     * 中文需要另外解析
     * @param keywords 参数
     * @return
     * @throws IOException
     */
    public List<Content> parseJd(String keywords) {
        String url = "https://search.jd.com/Search?keyword=" + keywords;
        try {
            Document parse = Jsoup.parse(new URL(url), 3000);
            Element element = parse.getElementById("J_goodsList");
            Elements elements = element.getElementsByTag("li");
            List<Content> list = new ArrayList<>();
            for (Element el : elements) {
                String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
                String price = el.getElementsByClass("p-price").eq(0).text();
                String title = el.getElementsByClass("p-name").eq(0).text();
                Content content = new Content(img, price, title);
                list.add(content);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
