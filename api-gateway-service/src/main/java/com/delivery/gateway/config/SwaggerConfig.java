package com.delivery.gateway.config;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Primary
@RequiredArgsConstructor
@Slf4j
public class SwaggerConfig implements SwaggerResourcesProvider{

    public static final String API_URI = "/v3/api-docs"; // OpenApi description default URI
    public static final String EUREKA_SUB_PRIX = "ReactiveCompositeDiscoveryClient_";
    private final RouteDefinitionLocator routeLocator; // Gateway locator

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        routeLocator.getRouteDefinitions().subscribe(
                routeDefinition -> {
                    if (routeDefinition.getId().contains(EUREKA_SUB_PRIX)) {
                        String resourceName = routeDefinition.getId().substring(EUREKA_SUB_PRIX.length());
                        String location = routeDefinition.getPredicates().get(0).getArgs().get("pattern").replace("/**", API_URI);
                        resources.add(swaggerResource(resourceName, location));
                    }
                }
        );
        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }
}