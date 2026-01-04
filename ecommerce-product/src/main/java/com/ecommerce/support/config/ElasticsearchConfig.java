package com.ecommerce.support.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.Arrays;
import java.util.List;

@EnableElasticsearchRepositories
@Configuration
public class ElasticsearchConfig {

    /**
     * ES8/Boot3 표준 프로퍼티: spring.elasticsearch.uris
     * 예) http://localhost:9200 (여러 개는 콤마로 표현)
     */
    @Value("${spring.elasticsearch.uris:http://localhost:9200}")
    private String uris;

    @Value("${spring.elasticsearch.connection-timeout-ms:2000}")
    private int connectTimeoutMs;

    @Value("${spring.elasticsearch.socket-timeout-ms:5000}")
    private int socketTimeoutMs;


    /**
     * ES8에서도 이 RestClient를 "기반"으로 transport/client 가 올라감
     */
    @Bean(destroyMethod = "close")
    public RestClient restClient() {
        HttpHost[] hosts = Arrays.stream(uris.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .map(HttpHost::create)          // http://host:port 형태 파싱
                .toArray(HttpHost[]::new);

        RestClientBuilder builder = RestClient.builder(hosts);

        builder.setRequestConfigCallback(rcb -> rcb
                .setConnectTimeout(connectTimeoutMs)
                .setSocketTimeout(socketTimeoutMs)
        );
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            return httpClientBuilder;
        });

        return builder.build();
    }

    /**
     * ES8 표준: Transport (RestClient + Json Mapper)
     */
    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    /**
     * ES8 표준: Elasticsearch Java API Client
     * - co.elastic.clients.elasticsearch.ElasticsearchClient
     */
    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }

    /**
     * Spring Data Elasticsearch용 Operations/Template
     * - Repository, IndexOperations, mapping/CRUD 등에 사용
     */
    @Bean(name = "elasticsearchTemplate")
    public ElasticsearchOperations elasticsearchOperations(ElasticsearchClient client) {
        return new ElasticsearchTemplate(client);
    }

}
