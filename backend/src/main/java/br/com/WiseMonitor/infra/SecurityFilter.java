package br.com.WiseMonitor.infra;

import java.io.IOException;

import org.springframework.context.annotation.Lazy;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.WiseMonitor.dados.IUserRepository;
import br.com.WiseMonitor.modelos.User;
import br.com.WiseMonitor.servico.TokenService;
import br.com.WiseMonitor.servico.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {

	private TokenService tokenService;
	private UserService userService;
	
	public SecurityFilter(TokenService tokenService,@Lazy UserService userService) {
		
		
		this.tokenService = tokenService;
		this.userService = userService;
		
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
			throws ServletException, IOException {

		var token = this.recoverToken(request);
		
		if(token != null) {
			
			var login = this.tokenService.validateToken(token);
			
			
			User u = (User) this.userService.getUIRepository().findByLogin(login).orElse(null);
			
			try {
				
			var auth = new UsernamePasswordAuthenticationToken(u,null,u.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(auth);
			
			}catch(NullPointerException exc) {
				
				throw new NullPointerException();
				
			}
			
		}
		
		filterChain.doFilter(request, response);		
		
	}

	private String recoverToken(HttpServletRequest request) {
	
		String authHeader = request.getHeader("Authorization");
		
		if(authHeader == null) {
			
			return null;
			
		}
	
		return authHeader.startsWith("Bearer ") ? authHeader.substring(7) : null;


	}



	
}
