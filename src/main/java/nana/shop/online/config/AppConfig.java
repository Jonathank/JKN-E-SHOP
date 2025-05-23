
package nana.shop.online.config;

import java.util.Collections;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {
    
    @Value("${server.port}")
    private  int appPort;

    @PostConstruct
    public void init() {
        System.out.println("🔍 APP_PORT is: " + appPort);
        if (appPort == 0) {
            System.out.println("❌ The APP_PORT value is not injected properly");
        }
    }

    @Bean
   WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer() {
        return factory -> factory.setPort(appPort);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                    "/JKN-Online/api/Shop/auth/**",
                    "/JKN-Online/api/Shop/products/**",
                    "/JKN-Online/api/Shop/products/*/reviews",
                    "/JKN-Online/api/Shop/category/**",
                    "/JKN-Online/api/Shop/deals/**",
                    "/JKN-Online/api/Shop/home/**",
                    "/JKN-Online/api/Shop/coupon/validate/**"
                ).permitAll()
                .requestMatchers(
                    "/JKN-Online/api/Shop/cart/**",
                    "/JKN-Online/api/Shop/orders/**",
                    "/JKN-Online/api/Shop/wishlist/**",
                    "/JKN-Online/api/Shop/payment/**",
                    "/JKN-Online/api/Shop/user/**",
                    "/JKN-Online/api/Shop/address/**",
                    "/JKN-Online/api/Shop/reviews/**",
                    "/JKN-Online/api/Shop/transaction/**"
                ).authenticated()
                .requestMatchers("/JKN-Online/api/Shop/seller/**").hasRole("SELLER")
                .requestMatchers("/JKN-Online/api/Shop/admin/**").hasRole("ADMIN")
                .anyRequest().denyAll()
            )
            .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }

//@Bean
//SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    // Configure session management to be stateless
//    http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        // Configure authorization rules
//        .authorizeHttpRequests(authorize -> authorize
//            // Allow access to the signup endpoint
//            .requestMatchers("/JKN-Online/api/Shop/auth/**").permitAll()
//            .requestMatchers("/JKN-Online/api/Shop/seller/**").permitAll()
//            // Require authentication for any request matching the pattern "/JKN-Online/api/Shop/**"
//            .requestMatchers("/JKN-Online/api/Shop/**").authenticated()
//            // Allow all requests to the pattern "/JKN-Online/api/Shop/products/*/reviews"
//            .requestMatchers("/JKN-Online/api/Shop/products/*/reviews").permitAll()
//            // Allow all other requests
//            .anyRequest().permitAll())
//        // Add a custom JWT token validator filter before the basic authentication filter
//        .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//        // Disable CSRF protection
//        .csrf(csrf -> csrf.disable())
//        // Enable CORS with a custom configuration source
//        .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//    // Build and return the SecurityFilterChain
//    return http.build();
//}



    /**
     * @return
     */

private CorsConfigurationSource corsConfigurationSource() {
    return new CorsConfigurationSource() {
        @Override
        public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
            // Create a new CORS configuration
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            // Allow all origins
            corsConfiguration.setAllowedOrigins(Collections.singletonList("*"));
            // Allow all HTTP methods (GET, POST, etc.)
            corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
            // Allow all headers
            corsConfiguration.setAllowedHeaders(Collections.singletonList("*"));
            // Allow credentials (cookies, authorization headers, etc.)
            corsConfiguration.setAllowCredentials(true);
            // Expose the "Authorization" header to the client
            corsConfiguration.setExposedHeaders(Collections.singletonList("Authorization"));
            // Set the maximum age for the CORS configuration to 3600 seconds (1 hour)
            corsConfiguration.setMaxAge(3600L);
            // Return the configured CORS settings
            return corsConfiguration;
        }
    };
}

@Bean
PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}

@Bean
 RestTemplate restTemplate() {
    return new RestTemplate();
}

@Bean
 static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
    PropertySourcesPlaceholderConfigurer configurer = new PropertySourcesPlaceholderConfigurer();
    Properties properties = new Properties();
    properties.setProperty("api.prefix", "/JKN-Online/api/Shop");
    configurer.setProperties(properties);
    return configurer;
}

//@Bean
//public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        .authorizeHttpRequests(auth -> auth
//            // Public access for signup, login, and product browsing
//            .requestMatchers("/auth/**", "/products/**", "/products/*/reviews").permitAll()
//
//            // CUSTOMER endpoints
//            .requestMatchers("/cart/**", "/orders/**", "/profile/**").hasRole("CUSTOMER")
//
//            // SELLER endpoints - manage their own products
//            .requestMatchers("/seller/products/**").hasRole("SELLER")
//            .requestMatchers("/seller/orders/**").hasRole("SELLER")
//
//            // ADMIN endpoints - full control
//            .requestMatchers("/admin/**").hasRole("ADMIN")
//
//            // Any other requests must be authenticated
//            .anyRequest().authenticated()
//        )
//        // Custom JWT token validator filter
//        .addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
//        // Disable CSRF protection and enable CORS
//        .csrf(csrf -> csrf.disable())
//        .cors(cors -> cors.configurationSource(corsConfigurationSource()));
//
//    return http.build();
//}


}
