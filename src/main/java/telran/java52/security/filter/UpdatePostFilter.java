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
import lombok.RequiredArgsConstructor;
import telran.java52.post.dao.PostRepository;
import telran.java52.post.model.Post;

@Component
@Order(50)
@RequiredArgsConstructor
public class UpdatePostFilter implements Filter {

	final PostRepository postRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        if (checkEndPoint(request.getMethod(), request.getServletPath())) {
            String principal = request.getUserPrincipal().getName();
            String[] parts = request.getServletPath().split("/");
            String postId = parts[parts.length - 1];
            Post post = postRepository.findById(postId).orElse(null);
            if (post == null) {
                response.sendError(404, "Not found");
                return;
            }
            if (!principal.equals(post.getAuthor())) {
                response.sendError(403, "You do not have permission to access this resource");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean checkEndPoint(String method, String path) {
        return HttpMethod.PUT.matches(method) && path.matches("/forum/post/\\w+");

    }

}
