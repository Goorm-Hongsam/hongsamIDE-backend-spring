package hongsam.gw.route.config;

import hongsam.gw.auth.filter.GraderAuthorizationFilter;
import hongsam.gw.auth.filter.JwtAuthorizationFilter;
import hongsam.gw.route.enums.ErrorMessages;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator apiServerRouter(RouteLocatorBuilder builder, GraderAuthorizationFilter graderFilter,
                                        JwtAuthorizationFilter jwtFilter) {
        return builder.routes()
                .route("api", r -> r.path("/api/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(JwtAuthorizationFilter.Config.builder()
                                        .missingMessage(ErrorMessages.MISSING.getMessage())
                                        .invalidMessage(ErrorMessages.INVALID.getMessage())
                                        .forbidMessage(ErrorMessages.FORBID.getMessage())
                                        .build())))
                        .uri("http://localhost:8081"))
                .route(r -> r.path("/grader/run/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(JwtAuthorizationFilter.Config.builder()
                                        .missingMessage(ErrorMessages.MISSING.getMessage())
                                        .invalidMessage(ErrorMessages.INVALID.getMessage())
                                        .forbidMessage(ErrorMessages.FORBID.getMessage())
                                        .build()))
                                .filter(graderFilter.apply(GraderAuthorizationFilter.Config.builder()
                                        .errorMessage(ErrorMessages.FORBID.getMessage())
                                        .build())))
                        .uri("https://3c72iejleyws5amdllgsxtqvd40apdhe.lambda-url.ap-northeast-2.on.aws/"))
                .build();
    }


}
