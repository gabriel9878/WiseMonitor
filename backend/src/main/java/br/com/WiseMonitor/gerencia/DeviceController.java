package br.com.WiseMonitor.gerencia;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import br.com.WiseMonitor.modelos.Device;
import br.com.WiseMonitor.modelos.DeviceDto;
import br.com.WiseMonitor.servico.DeviceService;
import jakarta.validation.Valid;


@RestController
@CrossOrigin(origins = "*")
public class DeviceController {

	private DeviceService deviceService;
	
	public DeviceController(DeviceService deviceService) {
		
		this.deviceService = deviceService;
		
	}
	
	@GetMapping("/exibicaoDispositivo/{id}")
	public ResponseEntity<?> listDeviceByID(@Valid @PathVariable Long id){
		
		
		return this.deviceService.listDeviceByID(id);
		
		
	}

	@GetMapping("/exibicaoDispositivos")
	public ResponseEntity<?> listDevices(){
		
		return this.deviceService.listDevices();
		
	}
	
	@PostMapping("/cadastroDispositivo")
	public ResponseEntity<?> createDevice(@Valid @RequestBody DeviceDto dto){
		
		return this.deviceService.createDevice(dto);
	
		
		
	}
	
	@PutMapping("/edicaoDispositivo/{id}")
	public ResponseEntity<?> editDevice(@PathVariable Long id,@Valid @RequestBody DeviceDto d){
		
		
		return this.deviceService.editDevice(id,d);
		
	}
	
	@DeleteMapping("/exclusaoDispositivo/{id}")
	public ResponseEntity<?> removeDevice(@PathVariable Long id){
		
		return this.deviceService.removeDevice(id);
		
	}
	
	
}
