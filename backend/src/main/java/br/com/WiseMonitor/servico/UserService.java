package br.com.WiseMonitor.servico;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.WiseMonitor.dados.IDeviceRepository;
import br.com.WiseMonitor.dados.IUserRepository;
import br.com.WiseMonitor.gerencia.SessionController;
import br.com.WiseMonitor.modelos.Device;
import br.com.WiseMonitor.modelos.DeviceRequestDto;
import br.com.WiseMonitor.modelos.LoginRequest;
import br.com.WiseMonitor.modelos.ResponseMessage;
import br.com.WiseMonitor.modelos.User;
import br.com.WiseMonitor.modelos.UserDto;
import br.com.WiseMonitor.modelos.UserResponseDto;
import br.com.WiseMonitor.modelos.UserRole;
import jakarta.transaction.Transactional;

@Service
public class UserService {
	
	private UserMapper userMapper;
	IUserRepository uIRepository;
	private SessionService sessionService;
	private BCryptPasswordEncoder passwordEncoder;

	public UserService(UserMapper userMapper, BCryptPasswordEncoder passwordEncoder,
		SessionService sessionService, IUserRepository uIRepository) {

		this.userMapper = userMapper;
		this.uIRepository = uIRepository;
		this.sessionService = sessionService;
		this.passwordEncoder = passwordEncoder;
	}

	public ResponseEntity<?> getLoggedUser(){

		String login = (String) this.sessionService.getLoggedUserLogin().getBody();
		
		if(login != null){
			
			User loggedUser;
			loggedUser = this.uIRepository.findByLogin(login).orElse(null);
			return new ResponseEntity<>(loggedUser,HttpStatus.OK);

		}

		throw new UsernameNotFoundException("Não há usuário autenticado");

	}

	
	public ResponseEntity<?> findUserByLogin(String loginRequest) {

		User searchedUser = this.uIRepository.findByLogin(loginRequest).orElse(null);

		if (searchedUser != null) {

			// Usa SessionService: sempre retorna o login (String)
			String login = (String) this.sessionService.getLoggedUserLogin().getBody();
			User loggedUser = this.uIRepository.findByLogin(login).orElse(null);

			if (loggedUser != null &&
					(loggedUser.getRole() == UserRole.ADMIN || searchedUser.equals(loggedUser))) {

				UserResponseDto uDto = this.userMapper.userToUserResponseDto(searchedUser);

				return new ResponseEntity<>(uDto, HttpStatus.FOUND);

			}

			return new ResponseEntity<>("Você não possui permissão para visualizar esse usuário", HttpStatus.FORBIDDEN);

		}

		return new ResponseEntity<>("Não há usuário cadastrado com o id informado", HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<?> findUserById(Long id) {

		User searchedUser = this.uIRepository.findById(id).orElse(null);

		if (searchedUser != null) {

			// Usa SessionService: sempre retorna o login (String)
			String login = (String) this.sessionService.getLoggedUserLogin().getBody();
			User loggedUser = this.uIRepository.findByLogin(login).orElse(null);

			if (loggedUser != null &&
					(loggedUser.getRole() == UserRole.ADMIN || searchedUser.equals(loggedUser))) {

				UserResponseDto uDto = this.userMapper.userToUserResponseDto(searchedUser);

				return new ResponseEntity<>(uDto, HttpStatus.FOUND);

			}

			return new ResponseEntity<>("Você não possui permissão para visualizar esse usuário", HttpStatus.FORBIDDEN);

		}

		return new ResponseEntity<>("Não há usuário cadastrado com o id informado", HttpStatus.NOT_FOUND);

	}
	
	//dev
	/*
	public ResponseEntity<?> registerUser(UserDto u) {

		if (u.login().equals("") || u.senha().equals("")|| u.cpf().equals("") || u.email().equals("") || u.role().equals(null)) {

			
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		
		} else if(this.uIRepository.existsByLogin(u.login()) || this.uIRepository.existsByCpf(u.cpf()) ) {
			
	
			return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
			
		} else {
			// Codifica senha do usuário antes de salvar
			
			
			
			User user = this.userMapper.dtoToUser(u);
			user.setSenha(this.passwordEncoder.encode(user.getSenha()));
			
			return new ResponseEntity<>(this.uIRepository.save(user), HttpStatus.CREATED);

		}

	}*/

	public ResponseEntity<?> registerUser(UserDto uDto) {
		
		
		if (uDto.login() == null || uDto.senha() == null || uDto.cpf() == null || uDto.email() == null) {

			return new ResponseEntity<>("Todos os campos devem estar preenchidos", HttpStatus.BAD_REQUEST);

		}

		User tempUser = this.uIRepository.findByLogin(uDto.login()).orElse(null);

		if (tempUser == null) {

			if (this.uIRepository.existsByEmail(uDto.email())) {

				return new ResponseEntity<>("Usuário já cadastrado com esse email", HttpStatus.BAD_REQUEST);

			}

			User loggedUser = null;

			// autenticação recebe autenticação do armazenador de contexto de segurança
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

			if (authentication != null && authentication.isAuthenticated()) {
				
				Object principal = authentication.getPrincipal();
				// Em muitos cenários o principal é apenas o login (String)
				if (principal instanceof String login) {
					loggedUser = this.uIRepository.findByLogin(login).orElse(null);
				}
				// Se em algum fluxo for UserDetails, também tratamos
				else if (principal instanceof UserDetails) {

					UserDetails userDetails = (UserDetails) principal;
					String login = userDetails.getUsername();
					loggedUser = this.uIRepository.findByLogin(login).orElse(null);
				
				}
			}

			// Cria sempre um novo usuário a partir do DTO
			tempUser = this.userMapper.dtoToUser(uDto);

			// Define a role apenas com base no usuário logado
			if (loggedUser != null && loggedUser.getRole() == UserRole.ADMIN) {
				// Admin autenticado: se não houver uma role explícita no DTO, usa USER como padrão
				if (tempUser.getRole() == null) {
					tempUser.setRole(UserRole.USER);
				}
			} else {
				// Cadastro público ou usuário não-admin: sempre USER
				tempUser.setRole(UserRole.USER);
			}

			tempUser.setSenha(this.passwordEncoder.encode(tempUser.getSenha()));
			this.uIRepository.save(tempUser);

			UserResponseDto uResponseDto = this.userMapper.userToUserResponseDto(tempUser);

			return new ResponseEntity<>(uResponseDto, HttpStatus.CREATED);

		}

		return new ResponseEntity<>("Usuário já cadastrado com esse login", HttpStatus.BAD_REQUEST);

	}

	/*
	 * //método para testes e depuração
	 //public ResponseEntity<?> findUsers(){
	 * 
	 * 
	 * return new ResponseEntity<>(this.uIRepository.findAll(),HttpStatus.ACCEPTED);
	 * 
	 * }
	 */

	public ResponseEntity<?> findUsers() {
		// Usa SessionService: sempre retorna o login (String)
		String login = (String) this.sessionService.getLoggedUserLogin().getBody();
		User loggedUser = this.uIRepository.findByLogin(login).orElse(null);

		if (loggedUser != null && loggedUser.getRole() == UserRole.ADMIN) {

			// Mapeia lista dos usuarios do repositorio
			List<UserResponseDto> usersDtosList = this.uIRepository.findAll().stream()
				.map(u -> this.userMapper.userToUserResponseDto(u)).collect(Collectors.toList());

			return new ResponseEntity<>(usersDtosList, HttpStatus.ACCEPTED);

		}

		return new ResponseEntity<>("Você não tem permissão para visualizar outros usuários", HttpStatus.FORBIDDEN);

	}

	public ResponseEntity<?> editUser(UserDto userDto) {

		if (userDto.login() == null || userDto.email() == null || userDto.cpf() == null) {
			return new ResponseEntity<>("Todos os campos devem ser preenchidos", HttpStatus.BAD_REQUEST);
		}

		User editedUser = this.uIRepository.findByLogin(userDto.login()).orElse(null);

		if (editedUser != null) {

			String login = (String) this.sessionService.getLoggedUserLogin().getBody();
			User loggedUser = this.uIRepository.findByLogin(login).orElse(null);

			if (loggedUser != null && loggedUser.getRole() == UserRole.ADMIN) {
				// Admin pode editar qualquer usuário. Mantém a senha atual se nenhuma nova for informada.
				User mappedUser = this.userMapper.dtoToUser(userDto);
				if (userDto.senha() == null || userDto.senha().trim().isEmpty()) {
					mappedUser.setSenha(editedUser.getSenha());
				} else {
					mappedUser.setSenha(this.passwordEncoder.encode(userDto.senha()));
				}
				this.uIRepository.save(mappedUser);
				return new ResponseEntity<>(this.userMapper.userToUserResponseDto(mappedUser), HttpStatus.ACCEPTED);
			}

			if (loggedUser != null && loggedUser.equals(editedUser)) {
				editedUser.setEmail(userDto.email());
				editedUser.setCpf(userDto.cpf());
				// Usuário editando o próprio perfil: só altera senha se uma nova for informada.
				if (userDto.senha() != null && !userDto.senha().trim().isEmpty()) {
					editedUser.setSenha(this.passwordEncoder.encode(userDto.senha()));
				}
				this.uIRepository.save(editedUser);
				return new ResponseEntity<>(this.userMapper.userToUserResponseDto(editedUser), HttpStatus.ACCEPTED);
			}

			return new ResponseEntity<>("Você não possui permissão para editar esse usuário", HttpStatus.FORBIDDEN);
		}

		return new ResponseEntity<>("Não há usuário cadastrado com o login solicitado", HttpStatus.NOT_FOUND);
	}

	@Transactional
	public ResponseEntity<?> removeUser(Long id) {

		User removedUser = this.uIRepository.findById(id).orElse(null);

		if (removedUser != null) {

			// Usa SessionService: sempre retorna o login (String)
			String login = (String) this.sessionService.getLoggedUserLogin().getBody();
			User loggedUser = this.uIRepository.findByLogin(login).orElse(null);

			if (loggedUser != null && (loggedUser.getRole() == UserRole.ADMIN || loggedUser.equals(removedUser))) {

				this.uIRepository.delete(removedUser);
				UserResponseDto uResponseDto = this.userMapper.userToUserResponseDto(removedUser);
				return new ResponseEntity<>(uResponseDto, HttpStatus.OK);

			}

			return new ResponseEntity<>("Você não possui autorização para remover esse usuário", HttpStatus.FORBIDDEN);

		}

		return new ResponseEntity<>("Não há usuário cadastrado com o id fornecido", HttpStatus.NOT_FOUND);

	}

	public ResponseEntity<?> listUserDevices(Long id) {

		User searchedUser = this.uIRepository.findById(id).orElse(null);

		if (searchedUser != null) {

			// Usa SessionService: sempre retorna o login (String)
			String login = (String) this.sessionService.getLoggedUserLogin().getBody();
			User loggedUser = this.uIRepository.findByLogin(login).orElse(null);

			if (loggedUser != null && (loggedUser.getRole() == UserRole.ADMIN || loggedUser.equals(searchedUser))) {

				List<String> devicesNames = searchedUser.getDispositivos().stream().map(d -> d.getNome())
						.collect(Collectors.toList());

				return new ResponseEntity<>(devicesNames, HttpStatus.OK);
			}

			return new ResponseEntity<>("Você não possui permissão para visualizar os dispositivos desse usuário",
					HttpStatus.FORBIDDEN);

		}

		return new ResponseEntity<>("Não há usuário cadastrado com o id fornecido", HttpStatus.NOT_FOUND);

	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exp) {

		HashMap<String, String> erros = new HashMap<>();

		exp.getBindingResult().getAllErrors().forEach(e -> {

			String nome = ((FieldError) e).getField();
			String mensagem = e.getDefaultMessage();
			erros.put(nome, mensagem);

		});

		return new ResponseEntity<>(erros, HttpStatus.BAD_REQUEST);

	}

	public IUserRepository getUIRepository() {

		return this.uIRepository;

	}

	/*
	 * public ResponseEntity<?> selectDeviceById(Long id) {
	 * 
	 * if (this.repositorioDI.countById(id) == 0) {
	 * 
	 * this.response.setMensagem("Não há dispositivo com o id informado"); return
	 * new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	 * 
	 * }
	 * 
	 * Device d = this.repositorioDI.findById(id).orElse(null);
	 * 
	 * return new ResponseEntity<>(deviceMapper.deviceToDeviceRequestDto(d),
	 * HttpStatus.OK);
	 * 
	 * }
	 * 
	 * 
	 * @Transactional public ResponseEntity<?> registerDevice(DeviceRequestDto dDto)
	 * {
	 * 
	 * if (dDto.nome().equals("")) {
	 * 
	 * this.response.setMensagem("O nome não pode estar em branco"); return new
	 * ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	 * 
	 * }
	 * 
	 * Device d = deviceMapper.dtoToDevice(dDto);
	 * 
	 * if (this.repositorioDI.existsByNome(dDto.nome())) {
	 * 
	 * this.response.setMensagem("Dispositivo já cadastrado"); return new
	 * ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	 * 
	 * }
	 * 
	 * d.setUser(this.usuarioLogado); d.setId(0L);
	 * 
	 * 
	 * 
	 * this.repositorioDI.save(d);
	 * 
	 * d = this.repositorioDI.findByNome(d.getNome()).orElse(null);
	 * this.usuarioLogado.getDispositivos().add(d);
	 * this.repositorioUI.save(this.usuarioLogado);
	 * 
	 * System.out.println(d.getId());
	 * 
	 * return new ResponseEntity<>(d, HttpStatus.CREATED);
	 * 
	 * }
	 * 
	 * public ResponseEntity<?> listDevices() {
	 * 
	 * //return new ResponseEntity<>(this.repositorioDI.findAll().stream().map(d ->
	 * deviceMapper.deviceToDtoReponse(d)), HttpStatus.OK); return new
	 * ResponseEntity<>(this.repositorioDI.findAll(), HttpStatus.OK);
	 * 
	 * }
	 * 
	 * public ResponseEntity<?> editDevice(DeviceRequestDto dDto) {
	 * 
	 * if (dDto.nome().equals("")) {
	 * 
	 * this.response.setMensagem("O nome não pode estar em branco"); return new
	 * ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
	 * 
	 * }
	 * 
	 * if (this.repositorioDI.countByNome(dDto.nome()) == 0) {
	 * 
	 * this.response.setMensagem("Não há dispositivos com o id informado"); return
	 * new ResponseEntity<>(this.response, HttpStatus.NOT_FOUND); }
	 * 
	 * Device d = this.repositorioDI.findByNome(dDto.nome()).orElse(null);
	 * 
	 * 
	 * if (this.repositorioDI.countByNome(dDto.nome()) != 0) {
	 * 
	 * this.response.setMensagem("Esse nome de dispositivo já está em uso"); return
	 * new ResponseEntity<>(this.response, HttpStatus.NOT_FOUND);
	 * 
	 * }
	 * 
	 * d.setNome(dDto.nome());
	 * 
	 * this.repositorioDI.save(d);
	 * 
	 * return new ResponseEntity<>(d, HttpStatus.OK);
	 * 
	 * }
	 * 
	 * @Transactional public ResponseEntity<?> removeDevice(Long id) {
	 * 
	 * if (this.repositorioDI.countById(id) == 0) {
	 * 
	 * this.response.setMensagem("Não há dispositivo com o id informado"); return
	 * new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST); }
	 * 
	 * Optional<Device> obj = this.repositorioDI.findById(id);
	 * 
	 * Device d = obj.get();
	 * 
	 * this.usuarioLogado.getDispositivos().remove(d);
	 * 
	 * d.setUser(null);
	 * 
	 * this.repositorioDI.delete(d);
	 * 
	 * System.out.println(d.getId());
	 * 
	 * return new ResponseEntity<>(d, HttpStatus.OK);
	 * 
	 * }
	 */

}
