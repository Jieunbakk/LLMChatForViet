package com.llm.llm.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class LlmWebClientConfig {


    @Value("${spring.web.client.llm}")
    String url;

    @Bean
    public WebClient LlmwebClient(){
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        }
                ).build();

    }
}
