package br.com.WiseMonitor.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.WiseMonitor.dados.IDeviceRepository;
import br.com.WiseMonitor.modelos.Device;
import br.com.WiseMonitor.modelos.DeviceDto;
import br.com.WiseMonitor.modelos.User;

@Service
public class DeviceService {

	private IDeviceRepository iDRepository;
	private DeviceMapper deviceMapper;
	private UserService userService;
	
	public DeviceService(IDeviceRepository iDRepository, UserService userService) {
		this.iDRepository = iDRepository;
		this.userService = userService;
	}
	
	public ResponseEntity<?> listDeviceByID(Long id){
		
		Device d = this.iDRepository.findById(id).orElse(null);
		
		if(d != null) {
			
			return new ResponseEntity<>(d,HttpStatus.ACCEPTED);
			
		}
		
		return new ResponseEntity<>("Não existe dispositivo com o id indicado",HttpStatus.BAD_REQUEST);
		
	}
	
	public ResponseEntity<?> listDevices(){
		
		return new ResponseEntity<>(this.iDRepository.findAll(),HttpStatus.OK);
		
	}
	
	public ResponseEntity<?> editDevice(Long id,DeviceDto d){
		
		if(d == null || d.nome().isEmpty() ){
			
			return new ResponseEntity<>("O nome não pode estar vazio",HttpStatus.BAD_REQUEST);
			
		}
		
		Device dispositivo = this.iDRepository.findById(id).orElse(null);

		if(dispositivo != null) {
			
			dispositivo.setNome(d.nome());
			
			return new ResponseEntity<>(this.iDRepository.save(dispositivo),HttpStatus.ACCEPTED);
			
		}
		
		return new ResponseEntity<>("Não há dispositivo com o id fornecido",HttpStatus.NOT_FOUND);
		
	}
	
	public ResponseEntity<?> createDevice(DeviceDto dispositivo) {
		
		if( dispositivo == null || dispositivo.nome().isEmpty() ) {
			
			return new ResponseEntity<>("O nome não pode estar vazio",HttpStatus.BAD_REQUEST);
			
		}
		
		if(this.iDRepository.existsByNome(dispositivo.nome())) {
			
			return new ResponseEntity<>("Já existe um dispositivo com esse nome",HttpStatus.BAD_REQUEST);
			
		}
		
		
		// recupera o usuário logado usando UserService
		ResponseEntity<?> loggedUserResponse = this.userService.getLoggedUser();
		if (!loggedUserResponse.getStatusCode().is2xxSuccessful() ||
			!(loggedUserResponse.getBody() instanceof User)  ) {
			return new ResponseEntity<>("Não há usuário autenticado para vincular o dispositivo", HttpStatus.UNAUTHORIZED);
		}

		User loggedUser = (User) loggedUserResponse.getBody();

		Device d = new Device();
		d.setNome(dispositivo.nome());
		d.setUser(loggedUser);

		if(loggedUser.getDispositivos() == null){

			List<Device> novaLista = new ArrayList<Device>();
			
			
			loggedUser.setDispositivos(novaLista);
		}

		loggedUser.getDispositivos().add(d);
		
		this.iDRepository.save(d);
		
		return new ResponseEntity<>(d,HttpStatus.CREATED);
	
		
	}
	
	public ResponseEntity<?> removeDevice(Long id){
		
		if(this.iDRepository.existsById(id)) {
			
			this.iDRepository.deleteById(id);
			return new ResponseEntity<>("Dispositivo removido com sucesso",HttpStatus.OK);
			
		}
		
		return new ResponseEntity<>("O dispositivo não foi encontrado",HttpStatus.NOT_FOUND);
		
	}
	
	
}
