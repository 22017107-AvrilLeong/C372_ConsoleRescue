package e63c.avril.ga;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	@Bean
	public MemberDetailsService memberDetailsService() {
		return new MemberDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(memberDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
				
				//Admin URL
				.requestMatchers("/categories/add", "/categories/edit/*", "/categories/save", "/categories/delete/*",
						"/items/add", "/items/edit/*", "/items/save", "/items/delete/*", "/members", "/members/add",
						"/members/edit", "/members/save", "/members/delete")
				.hasRole("ADMIN")
				
				//User URL
				.requestMatchers("/cart", "/cart/add/{itemId}", "/cart/update/{id}", "/cart/remove/{id}")
				.hasRole("USER")
				
	            //URLs accessible by both USER and ADMIN
	            .requestMatchers("/account")
	            .hasAnyRole("USER", "ADMIN")
				
				//Public URL
				.requestMatchers("/", "/about", "/contact", "/bootstrap/*/*", "/css/*", "/css/*", "/images/*", "/images/*/*", "/js/*",
						"/webfonts/*", "/register", "/register/save", "/shop", "uploads/items/*/*", "/items/*", "/search")
				.permitAll()
				
				
				.anyRequest().authenticated())// Any other requests not specified earlier
				.formLogin((login) -> login.loginPage("/login").permitAll().defaultSuccessUrl("/")) // Goes to homepage
																									// upon login
				.logout((logout) -> logout.logoutSuccessUrl("/"))// Goes to homepage upon logout
				.exceptionHandling((exceptionHandling) -> exceptionHandling.accessDeniedPage("/403"));

		return http.build();
	}
}
