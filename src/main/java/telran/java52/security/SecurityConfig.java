package telran.java52.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import lombok.RequiredArgsConstructor;
import telran.java52.accounting.model.Role;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	final CustomWebSecurity webSecurity;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.httpBasic(Customizer.withDefaults());
		// add option for not only GET
		// cross site request forgery
		http.csrf(csrf -> csrf.disable());
		//по деф стэйтлес, сейчас мы включили и есть аутен по сессии 
		//обычно передаем токент или basic так что отключаем
//		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		http.authorizeHttpRequests(authorize -> authorize
				// выстраиваем цепочку
				// по этому запросу есть доступ
				// так же все после /forum/posts
				.requestMatchers("/account/register", "/forum/posts/**").permitAll()
				// тут не нужно добавлять ROLE_ spring сам знает и сам добавляет
				// доступ только у админа
				.requestMatchers("/account/user/{login}/role/{role}").hasRole(Role.ADMINISTRATOR.name())
				// какой метод какой эндпоинт
				// забираем данные с адресной строки
				// разрешаем только владельцу
				.requestMatchers(HttpMethod.PUT, "/account/user/{login}")
					.access(new WebExpressionAuthorizationManager("#login == authentication.name"))
				// удаление юзера доступ=владелец+админ
				.requestMatchers(HttpMethod.DELETE, "/account/user/{login}")
				.access(new WebExpressionAuthorizationManager(
						"#login == authentication.name or hasRole('ADMINISTRATOR')"))
					//add post
				.requestMatchers(HttpMethod.POST, "/forum/post/{author}")
					.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
					//add comment 
				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}/comment/{author}")
					.access(new WebExpressionAuthorizationManager("#author == authentication.name"))
						//updatePost
				//по деф у нас бин и пишем @ с мал буквы или если указали value @Service("webService") старый способ 
				//внизу новый 
				//boolean не может быть bean 
				//userName = authentication.name
				//getVariables - map 
				.requestMatchers(HttpMethod.PUT, "/forum/post/{id}")
					.access((authentication, context) 
							-> new AuthorizationDecision(webSecurity.checkPostAuthor
									(context.getVariables().get("id"), authentication.get().getName())))
						//delete post
				//collection - getAuthorities
				.requestMatchers(HttpMethod.DELETE, "/forum/post/{id}")
					.access((authentication, context)-> {
						boolean checkAuthor = webSecurity.checkPostAuthor(context.getVariables().get("id"), authentication.get().getName());
						boolean checkModerator =  context.getRequest().isUserInRole("MODERATOR");
						return new AuthorizationDecision(checkAuthor || checkAuthor);
					})
							
				.anyRequest().authenticated()// все запросы только аутен
//				.anyRequest().permitAll()//все запросы доступны
		);

//		http.addFilter(filter); можно добавлять свои фильтры
		return http.build();
	}
}
