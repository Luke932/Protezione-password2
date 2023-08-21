package luke.SpringSecurity.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import luke.SpringSecurity.exceptions.UnauthorizedException;
import luke.SpringSecurity.users.User;
import luke.SpringSecurity.users.UsersService;

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class JWTAuthFilter extends OncePerRequestFilter {

	@Autowired
	JWTTools jwt;

	@Autowired
	UsersService srv;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader = request.getHeader("Authorization");

		if (authHeader == null || !authHeader.startsWith("Bearer"))
			throw new UnauthorizedException("PER FAVORE PASSA IL TOKEN NELL'AUTHORIZATION HEADER");
		String token = authHeader.substring(7);
		System.out.println(token);

		jwt.verifyToken(token);

		String id = jwt.extractSubject(token);
		User currentUser = srv.findById(UUID.fromString(id));

		UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(currentUser, null,
				currentUser.getAuthorities());

		SecurityContextHolder.getContext().setAuthentication(authtoken);

		response.setContentType("application/json");

		filterChain.doFilter(request, response);

	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		System.out.println(request.getServletPath());
		return new AntPathMatcher().match("/auth/**", request.getServletPath());
	}
}
