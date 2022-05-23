package com.nicico.ibs.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends  WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.authorizeRequests()
			.antMatchers(
					"/actuator/**",).permitAll()
				.anyRequest().authenticated().and().httpBasic()

			.and()
			.logout()
			.logoutUrl("/logout")
		;
		http.headers().frameOptions().sameOrigin();
	}
//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
//		UserDetails user =
//				User.withDefaultPasswordEncoder()
//						.username("username")
//						.password("password")
//						.roles("USER")
//						.build();
//
//		return new InMemoryUserDetailsManager(user);
//	}
//	@Override
//	public void configure(AuthenticationManagerBuilder auth) throws Exception {
//
//		auth.inMemoryAuthentication()
//				.withUser("kermit").password("kermit").roles("ADMIN", "MANAGER" ,"USER")
//				.and()
//				.withUser("gonzo").password("gonzo").roles("MANAGER", "USER")
//				.and()
//				.withUser("fozzie").password("fozzie").roles("USER")
//				.and()
//				.withUser("abbas").password("abbas").roles("ADMIN", "MANAGER" ,"USER");
//
//	}
public void configureGlobal(@Qualifier("systemUserService") UserDetailsService userDetailsService, AuthenticationManagerBuilder auth) throws Exception {

	auth.userDetailsService(userDetailsService)
	;
}

//	@Override
//	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
//		if (authentication != null) {
//			log.info("SecurityConfig: successful user logout=[{}]", authentication.getName());
//		} else {
//			log.info("SecurityConfig: successful user logout, no authentication");
//		}
//	}
}
