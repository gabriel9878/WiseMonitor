package br.com.WiseMonitor.gerencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.WiseMonitor.modelos.LoginRequest;
import br.com.WiseMonitor.modelos.User;
import br.com.WiseMonitor.servico.SessionService;
import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins = "*")
public class SessionController {
	
	private SessionService sessionService;
	
	public SessionController(SessionService sessionService) {
		super();
		this.sessionService = sessionService;
	}

	@GetMapping("/")
	public String loadInitialPage() {

		return "Bem vindo";

	}
	
	@PostMapping("/login")
	public ResponseEntity<?> initializeSession(@Valid @RequestBody LoginRequest u){
		
		return this.sessionService.initializeSession(u);
		
	}
	/*
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
	
	*/
}
