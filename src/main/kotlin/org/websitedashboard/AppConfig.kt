package org.websitedashboard

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory()
        factory.setConnectTimeout(10000)  // 10 seconds connection timeout
        factory.setReadTimeout(10000)     // 10 seconds read timeout
        return RestTemplate(factory)
    }
}
