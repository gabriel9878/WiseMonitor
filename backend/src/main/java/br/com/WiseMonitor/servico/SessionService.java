package br.com.WiseMonitor.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Service;


import br.com.WiseMonitor.modelos.LoginRequest;
import br.com.WiseMonitor.modelos.User;

@Service
public class SessionService{
	
    private AuthenticationManager authenticationManager;
    private UserService userService;
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

	/*
	public ResponseEntity<?> initializeSession(LoginRequest uDto) {

        if (uDto.login().isEmpty() || uDto.senha().isEmpty()) {
        	
        
            return new ResponseEntity<>("É necessário preencher todos os campos para efetuar login", HttpStatus.BAD_REQUEST);
            
        }

        Optional<User> optionalUser = this.uIRepository.findByLogin(uDto.login());

        if (optionalUser.isEmpty()) {

            
            return new ResponseEntity<>("Não foi encontrado usuário com os dados informados", HttpStatus.NOT_FOUND);

        }

        User userRep = optionalUser.get();

        if (!this.passwordEncoder.matches(uDto.senha(), userRep.getSenha())) {

            
            return new ResponseEntity<>("Senha incorreta, tente novamente", HttpStatus.BAD_REQUEST);

        }

        this.setUsuarioLogado(userRep);

        return new ResponseEntity<>(this.getUsuarioLogado(), HttpStatus.ACCEPTED);

    }

    public ResponseEntity<?> finalizeSession() {

        this.setUsuarioLogado(null);
        
        return new ResponseEntity<>("Sessão finalizada com sucesso", HttpStatus.ACCEPTED);

    }

    public ResponseEntity<?> saveLoggedUser(User u){

        if(this.getUsuarioLogado() == null){

           
            return new ResponseEntity<>("Não há usuário logado no sistema", HttpStatus.BAD_REQUEST);

        }

        if(!this.uIRepository.existsById(u.getId())){

            
            return new ResponseEntity<>("Usuario fornecido não possui o id compatível", HttpStatus.BAD_REQUEST);

        }

        
        
        if(!u.getDispositivos().isEmpty()){
            
        u.getDispositivos().forEach(d ->{ 
            
        this.dIRepository.save(d);
        
        });
        
        }
        
        this.setUsuarioLogado(u);
        this.getUsuarioLogado().setSenha(this.passwordEncoder.encode(u.getSenha()));
        this.uIRepository.save(this.getUsuarioLogado());
        return new ResponseEntity<>(u,HttpStatus.OK);

    }

    public ResponseEntity<?> selectLoggedUser() {

        if (this.getUsuarioLogado() != null) {

            return new ResponseEntity<>(this.getUsuarioLogado(), HttpStatus.OK);

        }

        return new ResponseEntity<>("Não há usuário logado no sistema", HttpStatus.BAD_REQUEST);

    }*/


	



}