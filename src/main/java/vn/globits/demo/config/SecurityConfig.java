package vn.globits.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // tắt CSRF cho REST API
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // cho phép tất cả request (dùng tạm khi test)

                        //.authorizeHttpRequests(auth -> auth
                            //    .requestMatchers("/api/auth/**").permitAll()  // cho phép login không cần xác thực
                              //  .anyRequest().authenticated()                 // các API khác phải login
                       // )
                       // .httpBasic();  // hoặc dùng JWT nếu có

                );

        return http.build();
    }
}
