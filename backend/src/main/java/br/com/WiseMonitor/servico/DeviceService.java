package br.com.WiseMonitor.servico;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.WiseMonitor.dados.IDeviceRepository;
import br.com.WiseMonitor.modelos.Device;

@Service
public class DeviceService {

	private IDeviceRepository iDRepository;
	private DeviceMapper deviceMapper;
	
	public DeviceService(IDeviceRepository iDRepository) {
		
		
		this.iDRepository = iDRepository;
		
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
	
	public ResponseEntity<?> editDevice(Device d){
		
		if(d == null || d.getNome().isEmpty() ){
			
			return new ResponseEntity<>("O nome não pode estar vazio",HttpStatus.BAD_REQUEST);
			
		}
	
		if(iDRepository.existsById(d.getId())) {
			
			
			return new ResponseEntity<>(this.iDRepository.save(d),HttpStatus.ACCEPTED);
			
		}
		
		
		return new ResponseEntity<>("Não há dispositivo com o id fornecido",HttpStatus.NOT_FOUND);
		
	}
	
	public ResponseEntity<?> createDevice(String nome) {
		
		if( nome == null || nome.isEmpty() ) {
			
			return new ResponseEntity<>("O nome não pode estar vazio",HttpStatus.BAD_REQUEST);
			
		}
		
		if(this.iDRepository.existsByNome(nome)) {
			
			return new ResponseEntity<>("Já existe um dispositivo com esse nome",HttpStatus.BAD_REQUEST);
			
		}
		
		
		Device d = new Device();
		d.setNome(nome);
		
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
