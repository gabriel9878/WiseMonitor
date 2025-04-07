package br.com.WiseMonitor.gerencia;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.WiseMonitor.modelos.DeviceRequestDto;
import br.com.WiseMonitor.modelos.LoginRequest;
import br.com.WiseMonitor.modelos.User;
import br.com.WiseMonitor.modelos.UserDto;
import br.com.WiseMonitor.servico.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@CrossOrigin(origins = "*")

public class UserController {
	
	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping("/exibicaoUsuario/{id}")
	public ResponseEntity<?> findUserById(@PathVariable Long id) {

		return this.service.findUserById(id);

	}


	@PostMapping("/cadastroUsuario")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto u) {

		return this.service.registerUser(u);

	}

	@GetMapping("/exibicaoUsuarios")
	public ResponseEntity<?> findUsers() {

		return this.service.findUsers();

	}

	@PutMapping("/edicaoUsuario")
	public ResponseEntity<?> editUser(@Valid @RequestBody UserDto u) {

		return this.service.editUser(u);

	}


	@DeleteMapping("/exclusaoUsuario/{id}")
	public ResponseEntity<?> removeUser(@PathVariable Long id) {

		return this.service.removeUser(id);

	}
	
	@GetMapping("/exibicaoDispositivosUsuario/{id}")
	public ResponseEntity<?> listUserDevices(@PathVariable Long id){

		return this.service.listUserDevices(id);

	}

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {

        HashMap<String, String> erros = new HashMap<>();

        exp.getBindingResult().getAllErrors().forEach(e -> {

            var nome = ((FieldError) e).getField(); 
            var mensagem = e.getDefaultMessage();
            erros.put(nome, mensagem);

        });

        return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);

    }



}