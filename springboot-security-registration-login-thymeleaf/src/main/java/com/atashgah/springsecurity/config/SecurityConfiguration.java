package com.atashgah.springsecurity.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	 @Autowired
	 private UserDetailsService userDetailsService;
	 
	 @Bean
	    public  BCryptPasswordEncoder passwordEncoder(){
	        return new BCryptPasswordEncoder();
	    }
	    

	    
	@Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		 http.csrf().disable()
         .authorizeRequests((authorize) ->
                 authorize.requestMatchers("/register/**").permitAll()
                         .requestMatchers("/index").permitAll()
                         .requestMatchers("/users").hasAnyRole("ADMIN","USER")
         ).formLogin(
                 form -> form
                         .loginPage("/login")
                         .loginProcessingUrl("/login")
                         .defaultSuccessUrl("/")
                         .permitAll()
         ).logout(
                 logout -> logout
                         .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                         .logoutSuccessUrl("/login?logout")
                         .permitAll()
         );
	        return http.build();
	    }

	    @Autowired
	    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	        auth
	                .userDetailsService(userDetailsService)
	                .passwordEncoder(passwordEncoder());
	    }
}
