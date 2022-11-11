package com.phishing.app.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SpringFoxConfig {

    /* ----------------URLs------------------
    *  http://localhost:port/swagger-ui.html
    *  http://localhost:port/v2/api-docs
    * */

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(buildAppInfo())
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo buildAppInfo() {
        List<VendorExtension> vendorExtensionList = new ArrayList<>();
        Contact contact = new Contact("Akani", "N/A", "akani@gmail.com");
        ApiInfo apiInfo = new ApiInfo(
                "Phishing Detector",
                "An application to detect phishing",
                "v1",
                "termsOfServiceUrl", contact,
                "License: N/A",
                "License Url: N/A",
                vendorExtensionList);
        return apiInfo;
    }

}