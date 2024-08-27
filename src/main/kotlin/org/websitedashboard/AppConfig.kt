package org.websitedashboard

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate

/**
 * Configuration class for application-specific beans.
 * Defines beans that are required by the application components.
 */
@Configuration
class AppConfig {

    /**
     * Configures a RestTemplate bean with custom timeouts.
     * @return a RestTemplate instance with a 10-second connection and read timeout.
     */
    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory()
        factory.setConnectTimeout(10000)
        factory.setReadTimeout(10000)
        return RestTemplate(factory)
    }
}
