package luke.SpringSecurity.security;

import java.io.IOException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorsFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		response.setHeader("Access-Control-Allow-Methods", "*");
		response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Allow-Max-Age", "3600");

		if (request.getMethod().equalsIgnoreCase("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
		} else {
			filterChain.doFilter(request, response);
		}
	}

}