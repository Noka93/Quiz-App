//package com.remidiousE.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.web.SecurityFilterChain;
//
//import static org.springframework.security.config.Customizer.withDefaults;
//
//@Configuration
//public class SpringSecurityConfiguration {
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.authorizeRequests(auth -> auth.anyRequest().authenticated());
//        http.httpBasic(withDefaults());
//        http.csrf(AbstractHttpConfigurer::disable);
//        return http.build();
//    }
//
//}
