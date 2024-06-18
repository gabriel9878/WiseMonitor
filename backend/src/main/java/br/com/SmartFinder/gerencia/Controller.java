package br.com.SmartFinder.gerencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.SmartFinder.modelos.DeviceDto;
import br.com.SmartFinder.modelos.UserDto;
import br.com.SmartFinder.servico.ServiceManager;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/")
public class Controller {

	
	private final ServiceManager service;


	public Controller(ServiceManager service) {
		this.service = service;
	}

	@GetMapping("/")
	public String loadInitialPage() {

		return "";

	}
	
	@PostMapping("/login")
	public ResponseEntity<?> initializeSession(@RequestBody UserDto u){
		
		return this.service.initializeSession(u);
		
		
	}
	
	@GetMapping("/logoff")
	public ResponseEntity<?> finalizeSession(){
		
		return this.service.finalizeSession();
		
		
	}

	@GetMapping("/exibicaoUsuarioAtivo")
	public ResponseEntity<?> selectLoggedUser() {

		return this.service.selectLoggedUser();

	}

	@GetMapping("/exibicaoUsuario-{id}")
	public ResponseEntity<?> selectUserById(@PathVariable Long id) {

		return this.service.selectUserById(id);

	}


	@PostMapping("/cadastroUsuario")
	public ResponseEntity<?> registerUser(@RequestBody UserDto u) {

		return this.service.registerUser(u);

	}

	@GetMapping("/exibicaoUsuarios")
	public ResponseEntity<?> listUsers() {

		return this.service.listUsers();

	}

	@PutMapping("/edicaoUsuario")
	public ResponseEntity<?> editUser(@RequestBody UserDto u) {

		return this.service.editUser(u);

	}


	@DeleteMapping("/exclusaoUsuario-{id}")
	public ResponseEntity<?> removeUser(@PathVariable Long id) {

		return this.service.deleteUser(id);

	}
	
	@GetMapping("/exibicaoDispositivo-{id}")
	public ResponseEntity<?> selectDeviceById(@PathVariable Long id) {

		return this.service.selectDeviceById(id);

	}
	
	
	@PostMapping("/cadastroDispositivo")
	public ResponseEntity<?> registerDevice(@RequestBody DeviceDto d) {

		return this.service.registerDevice(d);

	}

	@GetMapping("/exibicaoDispositivos")
	public ResponseEntity<?> listDevices() {

		return this.service.listDevices();

	}

	@PutMapping("/edicaoDispositivo")
	public ResponseEntity<?> editDevice(@RequestBody DeviceDto d) {
			
		return this.service.editDevice(d);

	}

	
	@DeleteMapping("/exclusaoDispositivo-{id}")
	public ResponseEntity<?> removeDevice(@PathVariable Long id) {

		return this.service.deleteDevice(id);

	}

}