package br.com.SmartFinder.gerencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.SmartFinder.modelos.LoginRequest;
import br.com.SmartFinder.modelos.User;
import br.com.SmartFinder.servico.SessionService;
import jakarta.validation.Valid;

@RestController
public class SessionController {
	
	
	private SessionService sessionService;
	
	@GetMapping("/")
	public String loadInitialPage() {

		return "Bem vindo";

	}
	
	@PostMapping("/login")
	public ResponseEntity<?> initializeSession(@Valid @RequestBody LoginRequest u){
		
		return this.sessionService.initializeSession(u);
		
	}
	
	@GetMapping("/logoff")
	public ResponseEntity<?> finalizeSession(){
		
		return this.sessionService.finalizeSession();
		
		
	}

	@GetMapping("/exibicaoUsuarioAtivo")
	public ResponseEntity<?> selectLoggedUser() {

		return this.sessionService.selectLoggedUser();

	}

	@PutMapping("/salvaUsuarioAtivo")
	public ResponseEntity<?> saveLoggedUser(@Valid @RequestBody User u){

		return this.sessionService.saveLoggedUser(u);

	}
	
	
}
