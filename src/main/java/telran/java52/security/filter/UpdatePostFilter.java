package telran.java52.security.filter;

import java.io.IOException;
import java.security.Principal;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.java52.post.dao.PostRepository;
import telran.java52.post.model.Post;
import telran.java52.security.model.User;

@Component
@RequiredArgsConstructor
@Order(50)
public class UpdatePostFilter implements Filter {

	final PostRepository postRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Проверка, если запрос совпадает с определенными путями
		if (checkEndpoint(request.getMethod(), request.getServletPath())) {
//			String principal = request.getUserPrincipal().getName(); // Получаем имя пользователя из Principal
			Principal principal = request.getUserPrincipal();
			User user = (User) principal; // Преобразуем Principal в User
			String author = user.getName(); // Извлекаем имя пользователя

			String[] parts = request.getServletPath().split("/");
			String postId = parts[parts.length - 1];
			Post post = postRepository.findById(postId).orElse(null);

			if (post == null) {
				response.sendError(404, "Post not found");
				return;
			}
			if (!author.equals(post.getAuthor())) {
				response.sendError(403, "Not have permission");
				return;
			}
		}
		chain.doFilter(request, response); // Продолжаем цепочку фильтров
	}

	private boolean checkEndpoint(String method, String path) {
		return (HttpMethod.PUT.matches(method) && path.matches("/forum/post/\\w+"));
	}
}
