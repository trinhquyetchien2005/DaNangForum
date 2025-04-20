package com.example.DaNangForum.config//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.Customizer
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
//import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
//import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
//import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
//import org.springframework.security.web.SecurityFilterChain
//
////import org.springframework.context.annotation.Bean
////import org.springframework.context.annotation.Configuration
////import org.springframework.security.config.Customizer
////import org.springframework.security.config.annotation.web.builders.HttpSecurity
////import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
////import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
////import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
////import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer
////import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
////import org.springframework.security.config.http.SessionCreationPolicy
////import org.springframework.security.web.SecurityFilterChain
////import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler
////
////
////@Configuration
////@EnableWebSecurity
////class SecurityConfig {
////
////    @Bean
////    @Throws(Exception::class)
////    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
////        http
////            .authorizeHttpRequests { auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
////                auth.requestMatchers("/").permitAll()
////                    .anyRequest().authenticated()
////            }
////            .oauth2Login { oauth2: OAuth2LoginConfigurer<HttpSecurity?> ->
////                oauth2
////                    .failureHandler(SimpleUrlAuthenticationFailureHandler("/login?error"))
////            }
////            .sessionManagement { session: SessionManagementConfigurer<HttpSecurity?> ->
////                session
////                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
////            } // Giữ session
////
////        return http.build()
////    }
////
////
////}
//@Configuration
//@EnableWebSecurity
//class SecurityConfig {
//    @Bean
//    @Throws(Exception::class)
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//            .authorizeHttpRequests { auth: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
//                auth.anyRequest().permitAll()
//            } // Cho phép tất cả request
//            .csrf { csrf: CsrfConfigurer<HttpSecurity> -> csrf.disable() } // Tắt CSRF (nếu không cần)
//
//        return http.build()
//    }
//}