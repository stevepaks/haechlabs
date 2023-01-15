package com.example.foo1.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

@Configuration
public class EthClientConfig {

    @Bean
    public Web3j web3(@Value("${eth-node-endpoint}") String ethClientEndpoint) {
        return Web3j.build(new HttpService(ethClientEndpoint));
    }

}
