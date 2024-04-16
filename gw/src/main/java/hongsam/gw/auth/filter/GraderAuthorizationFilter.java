package hongsam.gw.auth.filter;

import hongsam.gw.auth.util.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.PathContainer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class GraderAuthorizationFilter extends AbstractGatewayFilterFactory<GraderAuthorizationFilter.Config> {

    private final JwtUtils jwtUtils;

    @Autowired
    public GraderAuthorizationFilter(JwtUtils jwtUtils) {
        super(Config.class);
        this.jwtUtils = jwtUtils;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();

            String token = jwtUtils.extractToken(request);
            String requestUuid = getRequestUuid(request);
            String userUuid = jwtUtils.getUuid(token);

            if (!requestUuid.equals(userUuid)) {
                return onError(response, config.errorMessage, HttpStatus.FORBIDDEN);
            }

            return chain.filter(exchange);
        };
    }

    private static String getRequestUuid(ServerHttpRequest request) {
        PathContainer pathContainer = request.getPath().subPath(5);
        String value = pathContainer.value();
        return value;
    }

    private Mono<Void> onError(ServerHttpResponse response, String message, HttpStatus status) {
        response.setStatusCode(status);
        DataBuffer buffer = response.bufferFactory().wrap(message.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(buffer));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class Config {
        private String errorMessage;
    }
}
