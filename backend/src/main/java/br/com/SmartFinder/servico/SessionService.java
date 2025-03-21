package br.com.SmartFinder.servico;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.SmartFinder.dados.IDeviceRepository;
import br.com.SmartFinder.dados.IUserRepository;
import br.com.SmartFinder.modelos.LoginRequest;
import br.com.SmartFinder.modelos.User;

@Service
public class SessionService {
	
	private IUserRepository uIRepository;
	private IDeviceRepository dIRepository;
    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private User usuarioLogado;
    
	public SessionService(IUserRepository uIRepository) {
		
		
		this.uIRepository = uIRepository;
		this.usuarioLogado = null;
		
	}
	
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

    }

	public  User getUsuarioLogado() {
		return usuarioLogado;
	}

	public  void setUsuarioLogado(User usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
		
	}
    
    

}
