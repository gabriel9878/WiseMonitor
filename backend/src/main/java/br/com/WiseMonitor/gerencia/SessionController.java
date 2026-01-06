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
	
	@PostMapping("/login")
	public ResponseEntity<?> initializeSession(@Valid @RequestBody LoginRequest u){
		
		return this.sessionService.initializeSession(u);
		
	}
	
	@GetMapping("/exibicaoLoginUsuarioAtivo")
	public ResponseEntity<?> getLoggedUserLogin() {

		return this.sessionService.getLoggedUserLogin();

	}

}
