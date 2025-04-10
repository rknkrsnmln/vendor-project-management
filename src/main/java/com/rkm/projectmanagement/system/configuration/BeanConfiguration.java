package com.rkm.projectmanagement.system.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rkm.projectmanagement.client.post.PostServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class BeanConfiguration {


//    @Bean
//    public RestClient restClient(RestClient.Builder clientBuilder, ApplicationProperties applicationProperties) {
//        return clientBuilder
//                .baseUrl(applicationProperties.getRestClientBaseurl())
//                .build();
//    }

    @Bean
    PostServiceInterface postServiceInterface(ApplicationProperties applicationProperties) {
        RestClient client = RestClient.create(applicationProperties.getRestClientBaseurl());
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(client))
                .build();
        return factory.createClient(PostServiceInterface.class);
    }

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
