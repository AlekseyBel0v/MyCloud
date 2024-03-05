//package com.belov.mycloud.configuration;
//
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//
//@Configuration
//@EnableWebSecurity
//public class CustomWebSecurityConfigurerAdapter {
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user1 = User.withUsername("user1")
//                .password(passwordEncoder().encode("user1Pass"))
//                .roles("USER")
//                .build();
//        UserDetails user2 = User.withUsername("user2")
//                .password(passwordEncoder().encode("user2Pass"))
//                .roles("USER")
//                .build();
//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder().encode("adminPass"))
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2, admin);
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)   // Disables CSRF protection
//                .authorizeRequests()
//                .requestMatchers("/admin/**")
//                .hasRole("ADMIN")                   // доступ только для админа
//                .requestMatchers("/anonymous*")
//                .anonymous()                        // доступ для всех анономиных пользователей по эндпоинту /anonymous*
//                .requestMatchers("/login*")
//                .permitAll()                        // доступ для любых пользователей по эндпоинту /login*
//                .anyRequest()
//                .authenticated()                    // доступ для любых пользователей
//                .and()
//                .formLogin(formLogin -> formLogin
//                        .loginPage("/login.html")
//                        .loginProcessingUrl("/perform_login")
//                        .defaultSuccessUrl("/homepage.html", true)
//                        .failureUrl("/login.html?error=true"))
////                        .failureHandler(authenticationFailureHandler()))
//                /**
//                 * Для того чтобы реализовать authenticationFailureHandler(), вам нужно создать класс, который реализует
//                 * интерфейс AuthenticationFailureHandler. Этот интерфейс содержит один метод onAuthenticationFailure(),
//                 * который будет вызываться при неудачной аутентификации пользователя. Вот пример простой реализации:
//                 *
//                 * java
//                 * Copy code
//                 * import org.springframework.security.core.AuthenticationException;
//                 * import org.springframework.security.web.authentication.AuthenticationFailureHandler;
//                 *
//                 * import javax.servlet.ServletException;
//                 * import javax.servlet.http.HttpServletRequest;
//                 * import javax.servlet.http.HttpServletResponse;
//                 * import java.io.IOException;
//                 *
//                 * public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//                 *
//                 *     @Override
//                 *     public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                 *     AuthenticationException exception) throws IOException, ServletException {
//                 *         // Здесь вы можете определить логику для обработки неудачной аутентификации.
//                 *         // Например, можно перенаправить пользователя на определенную страницу с сообщением об ошибке.
//                 *         response.sendRedirect("/login.html?error=true");
//                 *     }
//                 * }
//                 * После этого вы можете использовать этот обработчик в вашей конфигурации безопасности, как показано в вашем примере:
//                 *
//                 * java
//                 * Copy code
//                 * .failureHandler(authenticationFailureHandler())
//                 * В методе filterChain(HttpSecurity http) вашей конфигурации, где вы настраиваете форму входа, добавьте следующий бин:
//                 *
//                 * java
//                 * Copy code
//                 * @Bean
//                 * public AuthenticationFailureHandler authenticationFailureHandler() {
//                 *     return new CustomAuthenticationFailureHandler();
//                 * }
//                 * Теперь при неудачной попытке аутентификации пользователь будет перенаправлен на страницу
//                 * /login.html?error=true, как указано в реализации CustomAuthenticationFailureHandler. Вы можете
//                 * настроить эту логику под свои требования.
//                 */
//                .logout((logout) -> logout
//                        .logoutUrl("/perform_logout")
//                        .deleteCookies("JSESSIONID"));
////                        .logoutSuccessHandler(logoutSuccessHandler()));
//        /**
//         * Кратко пояснение loginPage loginProcessingUrl defaultSuccessUrl failureUrl logoutUrl
//         * loginPage() – the custom login page
//         * loginProcessingUrl() – the URL to submit the username and password to
//         * defaultSuccessUrl() – the landing page after a successful login
//         * failureUrl() – the landing page after an unsuccessful login
//         * logoutUrl() – the custom logout
//         */
//        return http.build();
//    }
//
//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}
