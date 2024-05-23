package telran.java52.security.filter;

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

import java.io.IOException;
import java.security.Principal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Order(30)
@RequiredArgsConstructor
public class UserActionsFilter implements Filter {
    final UserAccountRepository userAccountRepository;

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        try {
            // Проверяем, если запрос - это удаление пользователя
            if (checkForDeleteUser(request.getMethod(), request.getServletPath())) {
                if (!handleDeleteUser(request, response)) {
                    return;
                }
            // Проверяем, если запрос - это обновление пользователя
            } else if (checkForUpdateUser(request.getMethod(), request.getServletPath())) {
                if (!handleUpdateUser(request, response)) {
                    return;
                }
            }
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException();
        }
        // Продолжаем обработку запроса
        chain.doFilter(request, response);
    }

    // Обрабатываем запрос на удаление пользователя
    private boolean handleDeleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal userPrincipal = request.getUserPrincipal();
        String login = userPrincipal.getName();
        UserAccount userAccount = userAccountRepository.findById(login).orElseThrow(UserNotFoundException::new);
        String targetUser = extractUserName(request.getServletPath());
        // Проверяем, что пользователь удаляет себя или является администратором
        if (!(login.equals(targetUser) || userAccount.getRoles().contains(Role.ADMINISTRATOR))) {
            response.sendError(403); // Доступ запрещен
            return false;
        }
        return true;
    }

    // Обрабатываем запрос на обновление пользователя
    private boolean handleUpdateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Principal userPrincipal = request.getUserPrincipal();
        String login = userPrincipal.getName();
        String targetUser = login;
        // Если это не изменение пароля, извлекаем имя пользователя из пути
        if (!isPasswordChange(request.getServletPath())) {
            targetUser = extractUserName(request.getServletPath());
        }
        // Проверяем, что пользователь обновляет себя
        if (!login.equals(targetUser)) {
            response.sendError(403); // Доступ запрещен
            return false;
        }
        return true;
    }
    
    // Извлекаем имя пользователя из пути
    private String extractUserName(String path) {
        Pattern pattern = Pattern.compile("^/account/user/([^/]+)$");
        Matcher matcher = pattern.matcher(path);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    // Проверяем, является ли запрос на обновление пользователя
    private boolean checkForUpdateUser(String method, String path) {
        return HttpMethod.PUT.matches(method) && (path.matches("^/account/user/[^/]+$") || isPasswordChange(path));
    }

    // Проверяем, является ли запрос изменением пароля
    private boolean isPasswordChange(String path) {
        return path.matches("^/account/password$");
    }

    // Проверяем, является ли запрос на удаление пользователя
    private boolean checkForDeleteUser(String method, String path) {
        return HttpMethod.DELETE.matches(method) && path.matches("^/account/user/[^/]+$");
    }
}
