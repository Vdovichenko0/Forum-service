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
import telran.java52.accounting.dao.UserAccountRepository;
import telran.java52.accounting.dto.exceptions.UserNotFoundException;
import telran.java52.accounting.model.Role;
import telran.java52.accounting.model.UserAccount;

@Component
@RequiredArgsConstructor
@Order(20)
public class AdminManagingRolesFilter implements Filter {

    // Репозиторий для доступа к учетным записям пользователей
    final UserAccountRepository userAccountRepository;
    
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        // Проверяем, является ли текущий запрос изменением роли пользователя
        if (isChangeRoleEndpoint(request.getMethod(), request.getServletPath())) {
            try {
                // Получаем объект Principal для текущего пользователя
                Principal userPrincipal = request.getUserPrincipal();
                String login = userPrincipal.getName();
                
                // Ищем учетную запись пользователя в репозитории
                UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
                
                // Проверяем, является ли пользователь администратором
                if (!userAccount.getRoles().contains(Role.ADMINISTRATOR)) {
                    System.out.println("user not admin " + login);
                    response.sendError(403); // Доступ запрещен
                    return;
                }
            } catch (UserNotFoundException e) {
                throw new UserNotFoundException();
            } 
        }
        // Продолжаем обработку запроса
        chain.doFilter(request, response);
    }
    
    // Проверяем, является ли запрос изменением роли пользователя
    private boolean isChangeRoleEndpoint(String method, String path) {
        return (HttpMethod.PUT.matches(method) || HttpMethod.DELETE.matches(method)) 
                && path.matches("/account/user/[^/]+/role/[^/]+$");
    }

}
