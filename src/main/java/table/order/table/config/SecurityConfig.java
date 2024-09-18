package table.order.table.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow unauthenticated access to login and register endpoints
                        .requestMatchers("/auth/register", "/auth/login").permitAll()

                        // Allow USERS and ADMINS to perform GET requests on /menus
                        .requestMatchers(HttpMethod.GET, "/menus/**").hasAnyRole("USER", "ADMIN")

                        // Only ADMINS can perform POST, PUT, DELETE on /menus
                        .requestMatchers(HttpMethod.POST, "/menus/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/menus/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/menus/**").hasRole("ADMIN")

                        // USER can perform all CRUD operations on /orders, including GET, while ADMIN can only GET orders
                        .requestMatchers(HttpMethod.GET, "/orders/**").hasAnyRole("USER", "ADMIN")  // Both can GET orders
                        .requestMatchers(HttpMethod.POST, "/orders/**").hasRole("USER")
                        .requestMatchers(HttpMethod.PUT, "/orders/**").hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, "/orders/**").hasRole("USER")

                        // USER role can access /carts and /order_items
                        .requestMatchers("/carts/**").hasRole("USER")
                        .requestMatchers("/order_items/**").hasRole("USER")

                        // All other requests need authentication
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())  // Use form-based login instead of httpBasic
                .httpBasic(withDefaults())  // Enable Basic Authentication
                .logout(logout -> logout.permitAll());
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}
