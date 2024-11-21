package in.tech_camp.pictweet;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import in.tech_camp.pictweet.costom_user.CustomUserDetail;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors
                    .configurationSource(request -> {
                        var corsConfiguration = new org.springframework.web.cors.CorsConfiguration();
                        corsConfiguration.setAllowedOrigins(List.of("http://localhost:3000"));
                        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                        corsConfiguration.setAllowCredentials(true);
                        corsConfiguration.setAllowedHeaders(List.of("*"));
                        return corsConfiguration;
                    })
                )
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        //ここに記述されたGETリクエストは許可されます（ログイン不要です)
                        .requestMatchers("/css/**", "/images/**", "/", "/users/sign_up", "/users/login", "/tweets/{id:[0-9]+}","/users/{id:[0-9]+}","/tweets/search","/error").permitAll()
                        //ここに記述されたPOSTリクエストは許可されます(ログイン不要です)
                        .requestMatchers(HttpMethod.POST, "/user").permitAll()
                        .requestMatchers("/api/login", "/api/user", "/api/logout", "/api/tweets/**","/api/users/{id:[0-9]+}","/api/tweets/{id:[0-9]+}/comment").permitAll()
                        .anyRequest().authenticated())
                        //上記以外のリクエストは認証されたユーザーのみ許可されます(要ログイン)


                .formLogin(form -> form
                    .loginProcessingUrl("/api/login")
                    .usernameParameter("email")
                    .successHandler(authenticationSuccessHandler())
                    .failureHandler((request, response, exception) -> {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                            "{\"error\":\"Invalid credentials\"}"
                        );
                    })
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout")
                        //ログアウトボタンを押した際のパスを設定しています
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // ここでログアウト成功時の処理を行います。
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"success\":true}");
                            response.getWriter().flush();
                        })
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
    return (request, response, authentication) -> {
        // ユーザー情報を含むJSONレスポンスを返す
        CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(String.format(
            "{\"id\":%d,\"nickname\":\"%s\",\"email\":\"%s\"}",
            userDetails.getId(),
            userDetails.getNickname(),
            userDetails.getUsername()
        ));
        response.getWriter().flush();
    };
}

}