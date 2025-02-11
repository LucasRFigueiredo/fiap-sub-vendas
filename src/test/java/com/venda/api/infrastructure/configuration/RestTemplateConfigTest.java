package com.venda.api.infrastructure.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

class RestTemplateConfigTest {

    @Test
    void testRestTemplateBeanCreation() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(RestTemplateConfig.class);

        assertTrue(context.containsBean("restTemplate"));

        RestTemplate restTemplate = context.getBean(RestTemplate.class);

        assertNotNull(restTemplate);

        context.close();
    }
}
