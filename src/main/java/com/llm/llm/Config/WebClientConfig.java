package com.llm.llm.Config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import io.netty.channel.ChannelOption;
import java.time.Duration;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient(MeterRegistry registry) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(3))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .filter((request, next) -> {
                    Timer.Sample sample = Timer.start(registry);
                    long startNs = System.nanoTime();
                    return next.exchange(request)
                            .doFinally(__ -> {
                                long elapsedNs = System.nanoTime() - startNs;
                                sample.stop(
                                        Timer.builder("webclient.latency")
                                                .description("Latency for external calls via WebClient")
                                                .tag("method", request.method().name())
                                                .tag("uri", request.url().getPath()) // path만 태그로(고정 태그 권장)
                                                .publishPercentileHistogram()
                                                .publishPercentiles(0.5, 0.95, 0.99)
                                                .register(registry)
                                );
                            });
                })
                .build();
    }
}

