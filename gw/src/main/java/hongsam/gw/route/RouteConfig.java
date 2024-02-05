package hongsam.gw.route;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator apiServerRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("api", r -> r.path("/api/**")
                        .uri("http://localhost:8081"))
                .build();
    }
}
