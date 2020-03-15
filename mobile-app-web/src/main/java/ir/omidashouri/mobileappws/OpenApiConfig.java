package ir.omidashouri.mobileappws;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class OpenApiConfig {


//    http://localhost:8080/mobile-app-ws/v3/api-docs
//    http://localhost:8080/mobile-app-ws/swagger-ui.html

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("SpringShop API").version("3.00")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }

    @Bean
    public GroupedOpenApi groupOpenApi() {
        String paths[] = {"/v1/**"};
        String packagesToscan[] = {"ir.omidashouri.mobileappws"};
        return GroupedOpenApi.builder().setGroup("users").pathsToMatch(paths).packagesToScan(packagesToscan)
                .build();
    }

/*    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/
}
