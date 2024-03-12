package hongsam.gw.route.config;
import hongsam.gw.auth.filter.GraderAuthorizationFilter;
import hongsam.gw.auth.filter.JwtAuthorizationFilter;
import hongsam.gw.route.enums.ErrorMessages;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RouteConfig {
    @Value("${url.api-server}")
    private String apiURI;

    @Value("${url.lambda.run}")
    private String runURI;

    @Value("${url.lambda.save}")
    private String saveURI;

    @Value("${url.lambda.get}")
    private String getURI;

    @Bean
    public RouteLocator apiServerRouter(RouteLocatorBuilder builder, GraderAuthorizationFilter graderFilter,
                                        JwtAuthorizationFilter jwtFilter) {
        return builder.routes()
                .route(r -> r.path("/api/members/**")
                        .uri(apiURI))
                .route(r -> r.path("/api/question/**")
                        .uri(apiURI))
                .route(r -> r.path("/api/login-check/**")
                        .uri(apiURI))
                .route("api", r -> r.path("/api/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(JwtAuthorizationFilter.Config.builder()
                                        .missingMessage(ErrorMessages.MISSING.getMessage())
                                        .invalidMessage(ErrorMessages.INVALID.getMessage())
                                        .forbidMessage(ErrorMessages.FORBID.getMessage())
                                        .build())))
                        .uri(apiURI))
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
                        .uri(runURI))
                .route(r -> r.path("/grader/save/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(JwtAuthorizationFilter.Config.builder()
                                        .missingMessage(ErrorMessages.MISSING.getMessage())
                                        .invalidMessage(ErrorMessages.INVALID.getMessage())
                                        .forbidMessage(ErrorMessages.FORBID.getMessage())
                                        .build()))
                                .filter(graderFilter.apply(GraderAuthorizationFilter.Config.builder()
                                        .errorMessage(ErrorMessages.FORBID.getMessage())
                                        .build())))
                        .uri(saveURI))
                .route(r -> r.path("/grader/get/**")
                        .filters(f -> f
                                .filter(jwtFilter.apply(JwtAuthorizationFilter.Config.builder()
                                        .missingMessage(ErrorMessages.MISSING.getMessage())
                                        .invalidMessage(ErrorMessages.INVALID.getMessage())
                                        .forbidMessage(ErrorMessages.FORBID.getMessage())
                                        .build()))
                                .filter(graderFilter.apply(GraderAuthorizationFilter.Config.builder()
                                        .errorMessage(ErrorMessages.FORBID.getMessage())
                                        .build())))
                        .uri(getURI))
                .build();
    }
}
