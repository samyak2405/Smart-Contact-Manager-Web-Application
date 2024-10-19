package com.javahunter.scm.config;

import com.javahunter.scm.entity.User;
import com.javahunter.scm.enums.Providers;
import com.javahunter.scm.helper.AppConstants;
import com.javahunter.scm.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        AuthenticationSuccessHandler.super.onAuthenticationSuccess(request, response, chain, authentication);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuthAuthenticationSuccessHandler onAuthenticationSuccess");

//        log.info(user.getName());
//        user.getAttributes().forEach((key, value) -> {
//            log.info(key + " = " + value);
//        });
//        log.info(user.getAuthorities().toString());
        var oAuth2AuthenticationToken = (OAuth2AuthenticationToken)authentication;
        String client = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();
        log.info("Login via: {}",client);

        DefaultOAuth2User user = (DefaultOAuth2User)authentication.getPrincipal();
          if(client.equals("github")){
              saveGithubUser(user);
          }else if(client.equals("google")){
              saveGoogleUser(user);
          }else{
              log.info("Unknown client");
          }

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

    private void saveGithubUser(DefaultOAuth2User user) {
        User saveUser = new User();
        saveUser.setUserId(UUID.randomUUID().toString());
        saveUser.setRoleList(List.of(AppConstants.ROLE_USER));
        saveUser.setEmailVerified(true);
        saveUser.setEnabled(true);
        String email = user.getAttribute("email")!=null?user.getAttribute("email"):user.getAttribute("login")+"@gmail.com";
        User isPresentUser = userRepository.findByEmail(email).orElse(null);
        saveUser.setEmail(email);
        saveUser.setProfilePic(user.getAttribute("avatar_url"));
        saveUser.setName(user.getAttribute("login"));
        saveUser.setProviderUserId(user.getName());
        saveUser.setProvider(Providers.GITHUB);
        if(isPresentUser == null) {
            log.info("Saving user: {}",saveUser);
            userRepository.save(saveUser);
        }
    }

    private void saveGoogleUser(DefaultOAuth2User user) {
        String email = user.getAttribute("email");
        String name = user.getAttribute("name");
        String picture = user.getAttribute("picture");

        User saveUser = new User();
        saveUser.setUserId(UUID.randomUUID().toString());
        saveUser.setEmail(email);
        saveUser.setName(name);
        saveUser.setProfilePic(picture);
        saveUser.setPassword("password");
        saveUser.setProvider(Providers.GOOGLE);
        saveUser.setEnabled(true);
        saveUser.setEmailVerified(true);
        saveUser.setProviderUserId(user.getName());
        saveUser.setRoleList(List.of(AppConstants.ROLE_USER));
        saveUser.setAbout("This account created using google...");

        User isPresentUser = userRepository.findByEmail(email).orElse(null);
        if(isPresentUser == null) {
            log.info("Saving user: {}",saveUser);
            userRepository.save(saveUser);
        }
    }
}
