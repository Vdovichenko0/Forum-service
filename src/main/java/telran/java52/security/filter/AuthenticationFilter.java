package telran.java52.security.filter;

import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import telran.java52.accounting.dao.UserAccountRepository;
import telran.java52.accounting.model.UserAccount;

@Component
@RequiredArgsConstructor
@Order(10)
public class AuthenticationFilter implements Filter {

	// Репозиторий для доступа к учетным записям пользователей
	final UserAccountRepository userAccountRepository;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// Логирование пути, метода и заголовка запроса
		System.out.println(request.getServletPath());
		System.out.println(request.getMethod());
		System.out.println(request.getHeader("Authorization"));

		// Проверка, требуется ли аутентификация для текущего эндпоинта
		if (checkEndpoint(request.getMethod(), request.getServletPath())) {
			// Если требуется, выполняем проверку учетных данных
			try {
				// Получение учетных данных из заголовка Authorization
				String[] credentials = getCredentials(request.getHeader("Authorization"));

				// Поиск пользователя по логину
				UserAccount userAccount = userAccountRepository.findById(credentials[0])
						.orElseThrow(RuntimeException::new);

				// Проверка пароля
				if (!BCrypt.checkpw(credentials[1], userAccount.getPassword())) {
					throw new RuntimeException();
				}

				// Оборачивание запроса для установки пользовательского Principal
				request = new WrappedRequest(request, userAccount.getLogin());
			} catch (Exception e) {
				// Если проверка не прошла, отправляем ошибку 401
				response.sendError(401);
				return;
			}
		}

		// Продолжение обработки запроса
		chain.doFilter(request, response);
	}

	// Проверка, требуется ли аутентификация для указанного метода и пути
	private boolean checkEndpoint(String method, String path) {
		// Если это не POST запрос на регистрацию, то требуется аутентификация
		return !(HttpMethod.POST.matches(method) && path.matches("/account/register"));
	}

	// Извлечение учетных данных из заголовка Authorization
	private String[] getCredentials(String header) {
		// Получение токена из заголовка
		String token = header.split(" ")[1];
		// Декодирование токена из Base64
		String decode = new String(Base64.getDecoder().decode(token));
		// Разделение логина и пароля
		return decode.split(":");
	}

	// Класс для оборачивания запроса и предоставления пользовательского Principal
	private class WrappedRequest extends HttpServletRequestWrapper {
		private String login;

		public WrappedRequest(HttpServletRequest request, String login) {
			super(request);
			this.login = login;
		}

		@Override
		public Principal getUserPrincipal() {
			// Возвращаем пользовательский Principal
			return () -> login;
		}
	}
}