import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/login", "/oauth2/**").permitAll()  // Không yêu cầu xác thực cho các endpoint này
                    .anyRequest().authenticated()  // Các endpoint còn lại yêu cầu xác thực
            }
            .oauth2Login {
                it.successHandler { request, response, authentication ->
                    // Đảm bảo session được tạo sau khi người dùng đăng nhập thành công
                    val user = authentication.principal as OidcUser
                    val session = request.getSession(true)
                    println("Session ID: ${session.id}")  // Log Session ID
                    session.setAttribute("user", user)  // Lưu thông tin người dùng vào session
                    println("Đăng nhập thành công với email: ${user.email}")
                    response.sendRedirect("/login/success")  // Chuyển hướng sau khi đăng nhập thành công
                }
                it.failureHandler { request, response, exception ->
                    // Log chi tiết khi đăng nhập thất bại
                    println("Lỗi đăng nhập: ${exception.message}")
                    exception.printStackTrace()  // Log stack trace để dễ debug
                    response.sendRedirect("/login?error")  // Chuyển hướng khi đăng nhập thất bại
                }
            }
        return http.build()
    }
}

@RestController
class GoogleLoginController {

    @GetMapping("/login/success")
    fun loginSuccess(@AuthenticationPrincipal user: OidcUser): String {
        return "Đăng nhập thành công! Email: ${user.email}"
    }
}
