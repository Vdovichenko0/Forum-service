package telran.java52.security.filter;

import java.io.IOException;

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

@Component
@Order(30)
public class UpdateByOwnerFilter implements Filter {
	
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndpoint(request.getMethod(), request.getServletPath())) {
            String principal = request.getUserPrincipal().getName();
            String[] parts = request.getServletPath().split("/");
            String owner = parts[parts.length - 1];
            if (!principal.equalsIgnoreCase(owner)) {
                response.sendError(403, "Not authorized");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndpoint(String method, String path) {
        return (HttpMethod.PUT.matches(method) && path.matches("/account/user/\\w+"))
        		|| (HttpMethod.POST.matches(method) && path.matches("/forum/post/\\w+"))
                || (HttpMethod.PUT.matches(method) && path.matches("/forum/post/\\w+/comment/\\w+"));
    }
}