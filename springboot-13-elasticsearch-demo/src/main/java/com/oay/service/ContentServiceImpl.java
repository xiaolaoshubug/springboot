package com.oay.service;

import com.alibaba.fastjson.JSON;
import com.oay.entity.Content;
import com.oay.utils.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/*********************************************************
 * @Package: com.oay.service
 * @ClassName: ContentServiceImpl.java
 * @Description：描述
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    @Qualifier("restHighLevelClient")
    private RestHighLevelClient client;

    @Override
    public Boolean parse(String keyword) {
        List<Content> contents = new HtmlParseUtil().parseJd(keyword);
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (int i = 0; i < contents.size(); i++) {
            bulkRequest.add(
                    new IndexRequest("jd_goods")
                            .source(JSON.toJSONString(contents.get(i)), XContentType.JSON)
            );
        }
        try {
            BulkResponse bulk = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            return !bulk.hasFailures();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> searchPage(String keyword, int pageNo, int pageSize) {
        //  条件搜索
        SearchRequest searchRequest = new SearchRequest("jd_goods");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //  分页
        sourceBuilder.from(pageNo);
        sourceBuilder.size(pageSize);
        //  精准匹配查询
        //TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", "java");
        MatchAllQueryBuilder matchAllQueryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(matchAllQueryBuilder);
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
        //  高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        //  关闭多处高亮
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");
        sourceBuilder.highlighter(highlightBuilder);
        //  执行搜索
        searchRequest.source(sourceBuilder);
        try {
            SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
            //  解析结构
            List<Map<String, Object>> list = new ArrayList<>();
            for (SearchHit searchHit : search.getHits().getHits()) {
                //  解析高亮字段
                Map<String, HighlightField> fields = searchHit.getHighlightFields();
                //  高亮的title
                HighlightField title = fields.get("title");
                //  原来的结果
                Map<String, Object> sourceAsMap = searchHit.getSourceAsMap();
                if (title != null) {
                    Text[] texts = title.fragments();
                    String new_title = "";
                    //  将原来的title替换成高亮的title
                    for (Text text : texts) {
                        new_title += text;
                    }
                    //  替换title
                    sourceAsMap.put("title", new_title);
                }
                list.add(sourceAsMap);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
