package dasturlash.uz.config;

import dasturlash.uz.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    public static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/api/v1/*/lang"
    };

    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication - Foydalanuvchining identifikatsiya qilish.
        // Ya'ni berilgan login va parolli user bor yoki yo'qligini aniqlash.
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);

        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization - Foydalanuvchining tizimdagi huquqlarini tekshirish.
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(AUTH_WHITELIST).permitAll()
                    .requestMatchers("/api/v1/region",
                            "/api/v1/region/",
                            "/api/v1/region/*",
                            "/api/v1/category",
                            "/api/v1/category/",
                            "/api/v1/category/*",
                            "/api/v1/section",
                            "/api/v1/section/",
                            "/api/v1/section/*",
                            "/api/v1/sms/pagination",
                            "/api/v1/profile",
                            "/api/v1/profile/admin/*",
                            "/api/v1/profile/pagination",
                            "/api/v1/profile/filter",
                            "/api/v1/profile/get/*"
                            ).hasRole("ADMIN")
                    .requestMatchers(HttpMethod.DELETE, "/api/v1/profile/*").hasRole("ADMIN")
                    .requestMatchers("/api/v1/profile/own/*", "/api/v1/profile/password/*").hasRole("USER")
                    .anyRequest()
                    .authenticated();
        }).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

//        http.csrf(Customizer.withDefaults()); // csrf yoqilgan
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults()); // cors yoqilgan

        return http.build();
    }

}