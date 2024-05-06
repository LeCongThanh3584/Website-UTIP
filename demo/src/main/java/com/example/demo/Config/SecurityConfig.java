package com.example.demo.Config;

import com.example.demo.Enums.ERole;
import com.example.demo.Oauth2.CustomOauth2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomOauth2Service customOauth2Service;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/*").permitAll()
                        .requestMatchers("/dash-board/**", "/admin/**").hasAuthority(ERole.ADMIN.name())
                        .requestMatchers("/student/**").hasAuthority(ERole.STUDENT.name())
                        .requestMatchers("/lecturer/**").hasAuthority(ERole.LECTURER.name())
                        .requestMatchers("/user/**").hasAuthority(ERole.USER.name())
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login  //login với db
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(((request, response, authentication) -> {
                            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                            if (authorities.contains(new SimpleGrantedAuthority(ERole.ADMIN.name()))) {
                                response.sendRedirect("/dash-board/home");
                            } else if (authorities.contains(new SimpleGrantedAuthority(ERole.STUDENT.name()))) {
                                response.sendRedirect("/student/infor");
                            } else if (authorities.contains(new SimpleGrantedAuthority(ERole.LECTURER.name()))) {
                                response.sendRedirect("/lecturer/infor");
                            }
                            else {
                                // Mặc định nếu không có vai trò nào được xác định
                                response.sendRedirect("/user/home");
                            }
                        }))
                        .failureHandler(((request, response, exception) -> {
                            String errorMessage;
                            if (exception instanceof DisabledException) {
                                errorMessage = "Tài khoản đã bị khoá. Vui lòng liên hệ với quản trị viên.";
                            } else {
                                errorMessage = "Sai tên Email hoặc mật khẩu.";
                            }
                            request.getSession().setAttribute("errorMessage", errorMessage);
                            response.sendRedirect("/login?error");
                        }))
                )
                .oauth2Login(oauth2 -> oauth2  //Login oauth2
                        .loginPage("/login")
                        .userInfoEndpoint(userInfor -> userInfor
                                .userService(customOauth2Service)
                        )
                        .defaultSuccessUrl("/user/home")
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                )
                .build();
    }

    @Bean
    public WebSecurityCustomizer securityCustomizer() {
        return web -> web.ignoring().requestMatchers("/css/**");
    }

}
