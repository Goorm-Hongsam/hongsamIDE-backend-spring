//package hongsam.api.config;
//
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//public class CorsConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 모든 요청에 대해 CORS 설정 적용
//                .allowedOrigins("https://main.hong-sam.online", "https://ide.hong-sam.online") // 허용할 도메인 지정
//                .allowedMethods("GET", "POST", "PATCH", "DELETE", "HEAD", "OPTIONS","PUT") // 허용할 HTTP 메서드 지정
//                .allowedHeaders("*") // 허용할 헤더 지정
//                .allowCredentials(true);
//    }
//
//}
