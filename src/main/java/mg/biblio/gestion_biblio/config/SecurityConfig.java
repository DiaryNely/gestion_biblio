package mg.biblio.gestion_biblio.config;

import mg.biblio.gestion_biblio.repository.UtilisateurRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Très important ! Votre script SQL utilise crypt('...', gen_salt('bf'))
        // qui correspond à l'algorithme BCrypt.
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(UtilisateurRepository utilisateurRepository) {
        // C'est ici qu'on dit à Spring Security comment chercher un utilisateur.
        return username -> utilisateurRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + username));
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz
                        // On autorise l'accès aux ressources statiques, à la page de login
                        .requestMatchers("/css/**", "/js/**", "/login").permitAll()
                        // NOUVEAU : Seuls les utilisateurs avec le rôle 'ADMIN' peuvent accéder à
                        // /admin/**
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Toutes les autres requêtes nécessitent d'être authentifié (ROLE_USER ou
                        // ROLE_ADMIN)
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        // IMPORTANT : Nous supprimons defaultSuccessUrl pour gérer la redirection
                        // nous-mêmes
                        // .defaultSuccessUrl("/", true) // <-- SUPPRIMEZ ou commentez cette ligne
                        .successHandler(new CustomAuthenticationSuccessHandler()) // <-- NOUVEAU : On ajoute un
                                                                                  // gestionnaire de succès
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                // Ajout pour la console H2 si vous l'utilisez, sinon facultatif
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

        return http.build();
    }
}