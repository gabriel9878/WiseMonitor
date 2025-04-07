package br.com.WiseMonitor.servico;

import org.springframework.stereotype.Service;

import br.com.WiseMonitor.modelos.Device;
import br.com.WiseMonitor.modelos.DeviceRequestDto;


@Service
public class DeviceMapper{

    public Device dtoToDevice(DeviceRequestDto dto){

        return new Device(null,dto.nome());

    }

    public DeviceRequestDto deviceToDeviceRequestDto(Device d){

        return new DeviceRequestDto(d.getNome());

    }

}