import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @GetMapping("/user")
    fun getUser(@AuthenticationPrincipal principal: OAuth2User): Map<String, String?> {
        // Lấy thông tin người dùng từ OAuth2User
        val name = principal.getAttribute<String>("name")
        val email = principal.getAttribute<String>("email")

        // Trả về thông tin người dùng dưới dạng JSON
        return mapOf("name" to name, "email" to email)
    }
}
