package ir.omidashouri.mobileappws;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {


//    http://localhost:8080/mobile-app-ws/v3/api-docs
//    http://localhost:8080/mobile-app-ws/swagger-ui.html

//    http://localhost:8080/mobile-app-ws/v3/api-docs/users
//    if do not set 'springdoc.swagger-ui.path=/swagger-ui.html' in 'application.properties' use the following url:
//    http://localhost:8080/mobile-app-ws/swagger-ui/index.html?configUrl=/mobile-app-ws/v3/api-docs/swagger-config#/users/getUsers
//    or use 'http://localhost:8080/mobile-app-ws/swagger-ui/index.html?url=' then set 'http://localhost:8080/mobile-app-ws/v3/api-docs' and click 'Explore' bottom

//http://localhost:8080/mobile-app-ws/swagger-ui/index.html?configUrl=/mobile-app-ws/v3/api-docs/swagger-config
//USER: omidashouri@gmail.com   PASSWORD:   123

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization"))
/*                        .addParameters("myHeader1", new Parameter().in("header").schema(new StringSchema()).name("myHeader1"))
                        .addHeaders("myHeader2", new Header().description("myHeader2 header").schema(new StringSchema()))*/
                )
                .info(new Info().title("SpringShop API").version("3.00").description("Mobile App API")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .addSecurityItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }

    @Bean
    public GroupedOpenApi loginApi(){
        return GroupedOpenApi.builder()
                .setGroup("Authentication-login")
                .pathsToMatch("/users/login")
                .packagesToScan("ir.omidashouri.mobileappws")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi(){
        return GroupedOpenApi.builder()
                .setGroup("User")
                .pathsToMatch("/v1/users/**")
                .packagesToScan("ir.omidashouri.mobileappws")
                .build();
    }

    @Bean
    public GroupedOpenApi attachmentApi(){
        return GroupedOpenApi.builder()
                .setGroup("Attachment")
                .pathsToMatch("/v1/attachments/**")
                .packagesToScan("ir.omidashouri.mobileappws")
                .build();
    }

/*    @Bean
    public GroupedOpenApi groupOpenApi() {
        String paths[] = {"/v1/**","/users/login"};
        String packagesToscan[] = {"ir.omidashouri.mobileappws"};
        return GroupedOpenApi.builder().setGroup("users").pathsToMatch(paths).packagesToScan(packagesToscan)
                .build();
    }*/

/*    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }*/
}
