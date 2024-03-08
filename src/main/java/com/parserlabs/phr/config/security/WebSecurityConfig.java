package com.parserlabs.phr.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

	@Value("${prop.swagger.enabled:false}")
	private boolean enableSwagger;

	@Value("${spring.profiles.active}")
	private String environment;

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

	@Autowired
	private ApiAccessRequestFilter apiAccessRequestFilter;

	@Autowired
	private UserDetailsService userDetailsService;

	
	private static final String[] AUTH_WHITELIST = {
	        // -- Swagger UI v2
	        "/v2/api-docs",
	        "/swagger-resources",
	        "/swagger-resources/**",
	        "/configuration/ui",
	        "/configuration/**",
	        "/configuration/security",
	        "/swagger-ui.html",
	        "/webjars/**",
	        // -- Swagger UI v3 (OpenAPI)
	        "/v3/api-docs/**",
	        "/swagger-ui/**",
	        "/swagger-ui/",
	        "/swagger-ui"
	        };
	        // other public ENDPOINTS of your API may be appended to this array
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/swagger-ui/**")
				.addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/")
				.resourceChain(false);
	}

	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		return super.userDetailsService();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// configure AuthenticationManager so that it knows from where to load
		// user for matching credentials
		// Use BCryptPasswordEncoder
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers(HttpMethod.OPTIONS, "/**");
		web.ignoring().mvcMatchers(AUTH_WHITELIST);
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().and().authorizeRequests().antMatchers("/v1/**") 		// We don't need CSRF for this example
				.authenticated()
				.and()
				.addFilterBefore(apiAccessRequestFilter, UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests().antMatchers(InsecureUrls.INSECURE_URLS.toArray(new String[1])).permitAll()	//dont authenticate this particular request
				.anyRequest().authenticated().and()	//all other requests need to be authenticated
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
				.and().sessionManagement()	// make sure we use stateless session; session won't be used to
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS)// store user's state.
				.and().csrf().disable();				


		// Enforce HTTPS in stage and prod.
		//httpSecurity.requiresChannel().requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null).requiresSecure();
		httpSecurity.headers().cacheControl();
		httpSecurity.headers().contentTypeOptions();
		httpSecurity.headers().xssProtection();
		httpSecurity.headers()
				.addHeaderWriter(new StaticHeadersWriter("Cache-Control",
						"no-cache, no-store, must-revalidate, pre-check=0, post-check=0"))
				.addHeaderWriter(new StaticHeadersWriter("X-Custom-Security-Header",
						"default-src 'none'; img-src 'self'; script-src 'self' style-src 'self'"))
				.addHeaderWriter(new StaticHeadersWriter("Content-Security-Policy",
						"frame-src 'self' https://www.google.com; frame-ancestors 'self' https://www.google.com; object-src 'none';"))
				.addHeaderWriter(new StaticHeadersWriter("X-WebKit-CSP", "default-src 'self'"));

		// Add a filter to validate the tokens with every request
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}

}