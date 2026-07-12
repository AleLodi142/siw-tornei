package siw_tornei.configuration;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import siw_tornei.model.Utente;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DataSource dataSource;

    public SecurityConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public UserDetailsService userDetailsService() {

        JdbcUserDetailsManager manager =
                new JdbcUserDetailsManager(dataSource);

        manager.setUsersByUsernameQuery("""
                SELECT username, password, true AS enabled
                FROM utente
                WHERE username = ?
                """);

        manager.setAuthoritiesByUsernameQuery("""
                SELECT username, ruolo
                FROM utente
                WHERE username = ?
                """);

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity httpSecurity) throws Exception {

        /*
         * Il token CSRF viene scritto nel cookie XSRF-TOKEN.
         *
         * HttpOnly è false perché React/Axios deve poter leggere
         * questo cookie e copiarne il valore nell'header
         * X-XSRF-TOKEN.
         */
        CookieCsrfTokenRepository csrfTokenRepository =
                CookieCsrfTokenRepository.withHttpOnlyFalse();

        csrfTokenRepository.setCookiePath("/");

        /*
         * Permette di inviare nell'header il valore letto
         * direttamente dal cookie XSRF-TOKEN.
         */
        CsrfTokenRequestAttributeHandler csrfRequestHandler =
                new CsrfTokenRequestAttributeHandler();

        httpSecurity.cors(cors -> cors
                .configurationSource(corsConfigurationSource()));

        httpSecurity.csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository)
                .csrfTokenRequestHandler(csrfRequestHandler));

        httpSecurity.authorizeHttpRequests(authorize -> {

            /*
             * Endpoint REST pubblici.
             *
             * Anche gli utenti anonimi possono leggere i commenti
             * e controllare il proprio stato di autenticazione.
             */
            authorize.requestMatchers(
                    HttpMethod.GET,
                    "/api/partite/*/commenti",
                    "/api/auth/me"
            ).permitAll();

            /*
             * Operazioni sui commenti che richiedono autenticazione.
             */
            authorize.requestMatchers(
                    HttpMethod.POST,
                    "/api/partite/*/commenti"
            ).authenticated();

            authorize.requestMatchers(
                    HttpMethod.PUT,
                    "/api/commenti/*"
            ).authenticated();

            authorize.requestMatchers(
                    HttpMethod.DELETE,
                    "/api/commenti/*"
            ).authenticated();

            /*
             * Pagine Thymeleaf pubbliche già esistenti.
             */
            authorize.requestMatchers(
                    HttpMethod.GET,
                    "/",
                    "/login",
                    "/register",
                    "/tornei/**",
                    "/squadre/**",
                    "/giocatori/**",
                    "/partite/**",
                    "/arbitri/**",
                    "/css/**",
                    "/images/**",
                    "/react-commenti/**",
                    "/favicon.ico"
            ).permitAll();

            authorize.requestMatchers(
                    HttpMethod.POST,
                    "/register",
                    "/login"
            ).permitAll();

            authorize.requestMatchers("/admin/**")
                    .hasAuthority(Utente.ADMIN_ROLE);

            authorize.requestMatchers("/user/**")
                    .hasAnyAuthority(
                            Utente.USER_ROLE,
                            Utente.ADMIN_ROLE
                    );

            authorize.anyRequest().authenticated();
        });

        httpSecurity.exceptionHandling(exception -> exception

        .defaultAuthenticationEntryPointFor(
                this::gestisciApiNonAutenticata,
                request ->
                        request.getRequestURI()
                                .startsWith("/api/")
        )

                .accessDeniedHandler(
                        this::gestisciAccessoNegato)
        );

        httpSecurity.formLogin(form -> {
            form.loginPage("/login").permitAll();
            form.defaultSuccessUrl("/success", true);
            form.failureUrl("/login?error=true");
        });

        httpSecurity.logout(logout -> {
            logout.logoutUrl("/logout");
            logout.logoutSuccessUrl("/");
            logout.invalidateHttpSession(true);
            logout.deleteCookies("JSESSIONID", "XSRF-TOKEN");
            logout.clearAuthentication(true);
            logout.permitAll();
        });

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        /*
         * Non possiamo usare "*" perché stiamo abilitando
         * l'invio dei cookie.
         */
        configuration.setAllowedOrigins(
                List.of("http://localhost:5173"));

        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "DELETE",
                        "OPTIONS"
                ));

        configuration.setAllowedHeaders(
                List.of(
                        "Content-Type",
                        "Accept",
                        "X-XSRF-TOKEN"
                ));

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration(
                "/**",
                configuration);

        return source;
    }

    private void gestisciApiNonAutenticata(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().write(
                """
                {
                  "messaggio": "Devi effettuare il login per eseguire questa operazione"
                }
                """
        );
    }

    private void gestisciAccessoNegato(
            HttpServletRequest request,
            HttpServletResponse response,
            Exception exception) throws IOException {

        if (request.getRequestURI().startsWith("/api/")) {

            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            response.getWriter().write(
                    """
                    {
                      "messaggio": "Operazione non consentita oppure token CSRF mancante o non valido"
                    }
                    """
            );

            return;
        }

        response.sendError(
                HttpServletResponse.SC_FORBIDDEN);
    }
}