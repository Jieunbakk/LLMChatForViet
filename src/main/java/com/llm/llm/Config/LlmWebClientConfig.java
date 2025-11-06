package com.llm.llm.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.resources.ConnectionProvider;

@Configuration
public class LlmWebClientConfig {


    @Value("${spring.web.client.llm}")
    String url;

    @Bean
    public WebClient LlmwebClient(){
        System.out.println("✅ [LlmWebClientConfig] baseUrl from yml: " + url);
        return WebClient.builder()
                .baseUrl(url)
                .defaultHeaders(
                        httpHeaders -> {
                            httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
                        }
                ).build();

    }
}
