package br.com.SmartFinder.servico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.SmartFinder.dados.IDeviceRepository;
import br.com.SmartFinder.dados.IUserRepository;
import br.com.SmartFinder.modelos.Device;
import br.com.SmartFinder.modelos.ResponseMessage;
import br.com.SmartFinder.modelos.User;
import br.com.SmartFinder.modelos.UserRequest;

@Service
public class ServiceManager {

	@Autowired
	private ResponseMessage response;
	@Autowired
	private IUserRepository repositorioUI;
	@Autowired
	private IDeviceRepository repositorioDI;
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	private User usuarioLogado = new User();

	public ResponseEntity<?> initializeSession(UserRequest ur) {
		
	    if (ur.getLogin().isEmpty() || ur.getSenha().isEmpty()) {
	        this.response.setMensagem("É necessário preencher todos os campos para efetuar login");
	        return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	    }

	    Optional<User> optionalUser = this.repositorioUI.findByLogin(ur.getLogin());
	    
	    if (optionalUser.isEmpty()) {
	    	
	        this.response.setMensagem("Não foi encontrado usuário com os dados informados");
	        return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	        
	    }

	    User userRep = optionalUser.get();

	    if (!this.passwordEncoder.matches(ur.getSenha(), userRep.getSenha())) {
	    	
	        this.response.setMensagem("As senhas não coincidem");
	        return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	    
	    }
	    
	    else {
	    	
	    usuarioLogado = userRep;

	    this.response.setMensagem("Sessão inicializada com sucesso");
	    return new ResponseEntity<>(this.response, HttpStatus.ACCEPTED);
	    
	    }

	  
	}
	
	public ResponseEntity<?> finalizeSession(){
		
		this.usuarioLogado = null;
		this.response.setMensagem("Sessão finalizada");
		return new ResponseEntity<>(this.response,HttpStatus.OK);
		
		
	}
	
	

	public ResponseEntity<?> selectUserById(Long id) {

		if (this.repositorioUI.countById(id) == 0) {

			this.response.setMensagem("Não há usuário com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
		}

		else {

			return new ResponseEntity<>(this.repositorioUI.findById(id), HttpStatus.OK);

		}

	}

	public ResponseEntity<?> selectLoggedUser() {

		if (!this.usuarioLogado.getLogin().equals("") && !this.usuarioLogado.getSenha().equals("")
				&& !this.usuarioLogado.getEmail().equals("") && !this.usuarioLogado.getCpf().equals("")) {

			return new ResponseEntity<>(this.usuarioLogado, HttpStatus.OK);

		} else {

			this.response.setMensagem("Não há usuário logado no sistema");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		}

	}

	public ResponseEntity<?> registerUser(User u) {

		if (u.getLogin().equals("") || u.getSenha().equals("")|| u.getCpf().equals("") || u.getEmail().equals("")) {

			this.response.setMensagem("É necessário preencher todos os campos do usuário");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		
		}else if(this.repositorioUI.existsByLogin(u.getLogin()) || this.repositorioUI.existsByCpf(u.getCpf()) ) {
			
			this.response.setMensagem("Usuário já cadastrado com esse login ou cpf");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
			
		} else {
			// Codifica senha do usuário antes de salvar
			u.setSenha(passwordEncoder.encode(u.getSenha()));
			return new ResponseEntity<>(this.repositorioUI.save(u), HttpStatus.CREATED);

		}

	}

	public ResponseEntity<?> listUsers() {

		return new ResponseEntity<>(this.repositorioUI.findAll(), HttpStatus.OK);

	}

	public ResponseEntity<?> editUser(User u) {
		
		if (u.getLogin().equals("") || u.getSenha().equals("")|| u.getCpf().equals("") || u.getEmail().equals("")) {

			this.response.setMensagem("É necessário preencher todos os campos do usuário");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		
		}
	    
		else if (!this.repositorioUI.existsByLogin(u.getLogin())) {
	        this.response.setMensagem("Não foi encontrado usuário com os dados informados");
	        return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	    }

	  
	    else {

			u.setSenha(this.passwordEncoder.encode(u.getSenha()));
			return new ResponseEntity<>(this.repositorioUI.save(u), HttpStatus.OK);

		}

	}

	public ResponseEntity<?> deleteUser(Long id) {

		if (this.repositorioUI.countById(id) == 0) {

			this.response.setMensagem("Não há usuários com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
		}

		else {

			Optional<User> obj = this.repositorioUI.findById(id);

			User u = new User();

			u = obj.get();

			this.repositorioUI.delete(u);

			this.response.setMensagem("Usuario removido com sucesso");

			return new ResponseEntity<>(this.response.getMensagem(), HttpStatus.OK);

		}

	}

	public ResponseEntity<?> selectDeviceById(Long id) {

		if (this.repositorioDI.countById(id) == 0) {

			this.response.setMensagem("Não há dispositivo com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
		}

		else {

			return new ResponseEntity<>(this.repositorioDI.findById(id), HttpStatus.OK);

		}

	}

	public ResponseEntity<?> registerDevice(Device d) {

		if (d.getNome().equals("")) {

			this.response.setMensagem("O nome não pode estar em branco");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		}else if (this.repositorioDI.existsById(d.getId())) {
			
			this.response.setMensagem("Dispositivo já cadastrado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
			
		}

		else {
			d.setUser(this.usuarioLogado);
			this.usuarioLogado.getDispositivos().add(d);
			return new ResponseEntity<>(this.repositorioDI.save(d), HttpStatus.CREATED);

		}

	}

	public ResponseEntity<?> listDevices() {

		return new ResponseEntity<>(this.repositorioDI.findAll(), HttpStatus.OK);

	}

	public ResponseEntity<?> editDevice(Device d) {

		if (this.repositorioDI.countById(d.getId()) == 0) {

			this.response.setMensagem("Não há dispositivos com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.NOT_FOUND);
		} else if (d.getNome().equals("")) {

			this.response.setMensagem("O nome não pode estar em branco");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		} else {

			return new ResponseEntity<>(this.repositorioDI.save(d), HttpStatus.OK);

		}

	}

	public ResponseEntity<?> deleteDevice(Long id) {

		if (this.repositorioDI.countById(id) == 0) {

			this.response.setMensagem("Não há dispositivo com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
		}

		else {

			Optional<Device> obj = this.repositorioDI.findById(id);

			Device d = new Device();
			d = obj.orElse(null);

			this.usuarioLogado.getDispositivos().remove(d);
			this.repositorioDI.delete(d);
			this.response.setMensagem("O dispositivo foi removido com sucesso");
			return new ResponseEntity<>(this.response.getMensagem(), HttpStatus.OK);

		}

	}


}