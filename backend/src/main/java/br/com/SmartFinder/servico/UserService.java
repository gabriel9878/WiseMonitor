package br.com.SmartFinder.servico;

import java.util.HashMap;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import br.com.SmartFinder.dados.IDeviceRepository;
import br.com.SmartFinder.dados.IUserRepository;
import br.com.SmartFinder.gerencia.SessionController;
import br.com.SmartFinder.modelos.Device;
import br.com.SmartFinder.modelos.DeviceRequestDto;
import br.com.SmartFinder.modelos.LoginRequest;
import br.com.SmartFinder.modelos.ResponseMessage;
import br.com.SmartFinder.modelos.User;
import br.com.SmartFinder.modelos.UserRequestDto;
import jakarta.transaction.Transactional;

@Service
public class UserService {

	private ResponseMessage response;
	private UserMapper userMapper;
	private DeviceMapper deviceMapper;
	private IDeviceRepository dIRepository;
	private IUserRepository uIRepository;
	private SessionService sessionService;

	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(ResponseMessage response, UserMapper userMapper, DeviceMapper deviceMapper,
			IDeviceRepository dIRepository, IUserRepository uIRepository, SessionService sessionService) {
		
		super();
		this.response = response;
		this.userMapper = userMapper;
		this.deviceMapper = deviceMapper;
		this.dIRepository = dIRepository;
		this.uIRepository = uIRepository;
		this.sessionService = sessionService;
		
	}

	public ResponseEntity<?> selectUserById(Long id) {

		if (this.uIRepository.countById(id) == 0) {

			this.response.setMensagem("Não há usuário com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		}

		User u = uIRepository.findById(id).orElse(null);

		return new ResponseEntity<>(userMapper.userToDtoResponse(u), HttpStatus.OK);

	}

	public ResponseEntity<?> registerUser(UserRequestDto uDto) {

		if (uDto.login().isEmpty() || uDto.senha().isEmpty() || uDto.cpf().isEmpty() || uDto.email().isEmpty()) {
			this.response.setMensagem("É necessário preencher todos os campos para efetuar o cadastro");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
		}

		if (this.uIRepository.existsByLogin(uDto.login()) || this.uIRepository.existsByCpf(uDto.cpf())) {

			this.response.setMensagem("Usuário já cadastrado com esse login ou cpf");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
		}

		// Mapeamento,registro no repositorio e resposta para o usuário
		User u = userMapper.dtoToUser(uDto);
		u.setSenha(passwordEncoder.encode(u.getSenha()));
		this.uIRepository.save(u);
		u = this.uIRepository.findByLogin(u.getLogin()).orElse(null);

		return new ResponseEntity<>(u, HttpStatus.CREATED);

	}

	public ResponseEntity<?> listUsers() {

		// Mapeia e retorna uma lista dos usuarios do repositorio
		// return new ResponseEntity<>(this.repositorioUI.findAll().stream().map(u ->
		// userMapper.userToDtoResponse(u)).collect(Collectors.toList()),
		// HttpStatus.OK);
		// método para testes e depuração, apagar em produção
		return new ResponseEntity<>(this.uIRepository.findAll(), HttpStatus.OK);
	}

	public ResponseEntity<?> editUser(UserRequestDto userDto) {

		if (!this.uIRepository.existsByLogin(userDto.login())) {
			this.response.setMensagem("Não foi encontrado usuário com os dados informados");
			return new ResponseEntity<>(this.response, HttpStatus.NOT_FOUND);
		}

		User u = this.uIRepository.findByLogin(userDto.login()).orElse(null);

		u.setLogin(userDto.login());
		u.setSenha(this.passwordEncoder.encode(userDto.senha()));
		u.setCpf(userDto.cpf());
		u.setEmail(userDto.email());

		this.uIRepository.save(u);

		u = this.uIRepository.findByLogin(u.getLogin()).orElse(null);
		return new ResponseEntity<>(u, HttpStatus.OK);

	}

	@Transactional
	public ResponseEntity<?> removeUser(Long id) {

		if (this.uIRepository.countById(id) == 0) {

			this.response.setMensagem("Não há usuários com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		}

		Optional<User> obj = this.uIRepository.findById(id);

		User u = obj.get();

		u.getDispositivos().forEach(a -> {

			this.dIRepository.delete(a);

		});

		if (this.sessionService.getUsuarioLogado() != null && u.equals(this.sessionService.getUsuarioLogado())) {

			this.sessionService.setUsuarioLogado(null);

		}

		this.uIRepository.delete(u);

		return new ResponseEntity<>(u, HttpStatus.OK);

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

	public ResponseEntity<?> listUserDevices(Long id) {

		if (this.uIRepository.countById(id) == 0) {

			this.response.setMensagem("Não há usuários com o id informado");
			return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

		}

		Optional<User> obj = this.uIRepository.findById(id);

		User u = obj.get();

		return new ResponseEntity<>(u.getDispositivos(), HttpStatus.OK);

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

}
