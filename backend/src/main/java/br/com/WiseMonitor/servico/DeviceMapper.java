package br.com.WiseMonitor.servico;

import org.springframework.stereotype.Service;

import br.com.WiseMonitor.modelos.Device;
import br.com.WiseMonitor.modelos.DeviceDto;


@Service
public class DeviceMapper{

    public Device dtoToDevice(DeviceDto dto){

        return new Device(null,dto.nome());

    }

    public DeviceDto deviceToDeviceRequestDto(Device d){

        return new DeviceDto(d.getNome());

    }

}