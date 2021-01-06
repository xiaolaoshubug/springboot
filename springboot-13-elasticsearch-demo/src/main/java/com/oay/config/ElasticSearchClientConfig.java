package com.oay.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*********************************************************
 * @Package: com.oay.config
 * @ClassName: ElasticSearchClientConfig.java
 * @Description： 自定义ElasticSearchClient
 * -----------------------------------
 * @author：ouay
 * @Version：v1.0
 * @Date: 2021-01-06
 *********************************************************/

@Configuration
public class ElasticSearchClientConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")
                )
        );
    }
}
