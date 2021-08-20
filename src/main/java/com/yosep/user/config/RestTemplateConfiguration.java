package com.yosep.user.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Collections;
import java.util.function.Supplier;

@Configuration
public class RestTemplateConfiguration {


    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(50000);
        requestFactory.setReadTimeout(50000);

        RestTemplate restTemplate = new RestTemplate();
        // SimpleClientHttpRequestFactory를 래핑해서 body stream이 소진되는 것을 막아준다.
        BufferingClientHttpRequestFactory bufferingClientHttpRequestFactory = new BufferingClientHttpRequestFactory(requestFactory);
        restTemplate.setRequestFactory(bufferingClientHttpRequestFactory);
        restTemplate.setInterceptors(Collections.singletonList(new HttpClientInterceptor()));
        return restTemplate;

    }
}

class HttpClientInterceptor implements ClientHttpRequestInterceptor {
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] requestBody, ClientHttpRequestExecution execution) throws IOException {

        ClientHttpResponse response = execution.execute(request, requestBody);

        return response;
    }
}

//@Configuration
//class RestTemplateConfig {
//    @Bean
//    fun restTemplate(restTemplateBuilder: RestTemplateBuilder): RestTemplate {
//        return restTemplateBuilder
//                .requestFactory{
//            BufferingClientHttpRequestFactory(SimpleClientHttpRequestFactory())
//        }
//        .setConnectTimeout(Duration.ofMillis(5000)) // connection-timeout
//                .setReadTimeout(Duration.ofMillis(5000)) // read-timeout
//                .additionalMessageConverters(StringHttpMessageConverter(Charset.forName("UTF-8")))
//                .build()
//    }
//}