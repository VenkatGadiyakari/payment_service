package com.ticketing.payment.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Payment Service")
                        .description("Manages Razorpay payment link creation and webhook processing. " +
                                "Called internally by the order-service. Webhook endpoint receives events " +
                                "directly from Razorpay and updates payment + order state.")
                        .version("1.0.0"));
    }
}
