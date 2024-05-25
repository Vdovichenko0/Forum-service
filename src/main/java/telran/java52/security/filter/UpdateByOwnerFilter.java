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
import telran.java52.security.model.User;

@Component
@Order(30)
public class UpdateByOwnerFilter implements Filter {
	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Check if the request matches specific endpoints
		if (checkEndpoint(request.getMethod(), request.getServletPath())) {
//			String principal = request.getUserPrincipal().getName(); // Get the name of the logged-in user
			Principal principal = request.getUserPrincipal();
			User user = (User) principal; // Преобразуем Principal в User
			String userName = user.getName(); // Извлекаем имя пользователя
			String[] parts = request.getServletPath().split("/"); // Split the path by "/"
			String owner = parts[parts.length - 1]; // Get the last part of the path
			// If the logged-in user is not the owner, send a 403 error
			if (!userName.equalsIgnoreCase(owner)) {
				response.sendError(403, "Not authorized");
				return;
			}
		}

		chain.doFilter(request, response); // Continue the filter chain
	}

	// Method to check if the request method and path match specific criteria
	private boolean checkEndpoint(String method, String path) {
		return (HttpMethod.PUT.matches(method) && path.matches("/account/user/\\w+")) // Update user account
				// Add post or add comment
				|| (HttpMethod.POST.matches(method) && path.matches("/forum/post/\\w+")
						|| (HttpMethod.PUT.matches(method) && path.matches("/forum/post/\\w+/comment/\\w+")));
	}
}