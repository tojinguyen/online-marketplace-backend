package com.learning.onlinemarketplace.userservice.security;

import com.learning.onlinemarketplace.userservice.service.AuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Autowired
    private AuthService authService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauthUser = token.getPrincipal();

        // Lấy thông tin cần thiết từ OAuth2User
        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");

        // Xử lý (tạo mới người dùng nếu chưa tồn tại)
        authService.ProcessOAuthPostLogin(email, name);

        // Tiếp tục redirect theo mặc định (ở đây chuyển đến /home)
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
