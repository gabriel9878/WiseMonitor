package br.com.WiseMonitor.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;

import br.com.WiseMonitor.modelos.LoginRequest;
import br.com.WiseMonitor.modelos.User;

@Service
public class SessionService{
	
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private TokenService tokenService;

	public SessionService(@Lazy AuthenticationManager authenticationManager,UserDetailsService userDetailsService,
			TokenService tokenService) {
		
		this.userDetailsService =  userDetailsService;
		this.authenticationManager = authenticationManager;
		this.tokenService = tokenService;
		
	}

	public ResponseEntity<?> initializeSession(LoginRequest request){
		
		/*
        if (uDto.login().isEmpty() || uDto.login() == null || uDto.senha().isEmpty()) {
        	
            
            return new ResponseEntity<>("É necessário preencher todos os campos para efetuar login", HttpStatus.BAD_REQUEST);
            
        }*/
		
		//Transforma loginRequest em token de autenticação UsernamePassword
		var tokenUsernamePassword = new UsernamePasswordAuthenticationToken(request.login(),request.senha());
		
		//Realiza autenticação com o token passado como parametro e retorna o objeto autenticado.
		var auth= this.authenticationManager.authenticate(tokenUsernamePassword);
		
		//Gera token com base nos dados do usuario autorizado
		var token = tokenService.generateToken((User)auth.getPrincipal());
		
		return new ResponseEntity<>(token,HttpStatus.OK);
		
	}


    public ResponseEntity<?> getLoggedUserLogin() {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && auth.isAuthenticated()){

            if(auth.getPrincipal() instanceof String texto){

                return new ResponseEntity<>(texto,HttpStatus.OK);
                
            }

            if(auth.getPrincipal() instanceof UserDetails user){

                return new ResponseEntity<>(user.getUsername(),HttpStatus.OK);

            }

        }

        throw new UsernameNotFoundException("Não há usuário autenticado");

    }

}