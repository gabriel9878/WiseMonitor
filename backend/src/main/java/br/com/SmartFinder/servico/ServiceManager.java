package br.com.SmartFinder.servico;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.SmartFinder.dados.IDeviceRepository;
import br.com.SmartFinder.dados.IUserRepository;
import br.com.SmartFinder.modelos.Device;
import br.com.SmartFinder.modelos.DeviceDto;
import br.com.SmartFinder.modelos.ResponseMessage;
import br.com.SmartFinder.modelos.User;
import br.com.SmartFinder.modelos.UserDto;
import br.com.SmartFinder.modelos.UserResponseDto;

@Service
public class ServiceManager {

    private final ResponseMessage response;

    private final IUserRepository repositorioUI;
    private final IDeviceRepository repositorioDI;

    private final BCryptPasswordEncoder passwordEncoder;
    private User usuarioLogado;

	
    public ServiceManager(ResponseMessage response, IUserRepository repositorioUI, IDeviceRepository repositorioDI) {

        this.response = response;
        this.repositorioUI = repositorioUI;
        this.repositorioDI = repositorioDI;
		this.usuarioLogado = null;
        this.passwordEncoder = new BCryptPasswordEncoder();

    }

    public ResponseEntity<?> initializeSession(UserDto uDto) {

        if (uDto.login().isEmpty() || uDto.senha().isEmpty()) {
            this.response.setMensagem("É necessário preencher todos os campos para efetuar login");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
        } else {

            Optional<User> optionalUser = this.repositorioUI.findByLogin(uDto.login());

            if (optionalUser.isEmpty()) {

                this.response.setMensagem("Não foi encontrado usuário com os dados informados");
                return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

            }

            User userRep = optionalUser.get();

            if (!this.passwordEncoder.matches(uDto.senha(), userRep.getSenha())) {

                this.response.setMensagem("Senha incorreta, tente novamente");
                return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

            } else {

                return new ResponseEntity<>(this.usuarioLogado = userRep, HttpStatus.ACCEPTED);

            }

        }

    }

    public ResponseEntity<?> finalizeSession() {

        return new ResponseEntity<>(this.usuarioLogado = null, HttpStatus.OK);

    }

    public ResponseEntity<?> selectUserById(Long id) {

        if (this.repositorioUI.countById(id) == 0) {

            this.response.setMensagem("Não há usuário com o id informado");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
        } else {
            
            User u = repositorioUI.findById(id).orElse(null);

            return new ResponseEntity<>(new UserResponseDto(u.getLogin(),u.getEmail()),HttpStatus.OK);

        }

    }

    public ResponseEntity<?> selectLoggedUser() {

        if (!this.usuarioLogado.getLogin().equals("") && !this.usuarioLogado.getSenha().equals("")
                && !this.usuarioLogado.getEmail().equals("") && !this.usuarioLogado.getCpf().equals("")) {

                    

            return new ResponseEntity<>(new UserResponseDto(this.usuarioLogado.getLogin(),this.usuarioLogado.getEmail()), HttpStatus.OK);

        } else {

            this.response.setMensagem("Não há usuário logado no sistema");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        }

    }

    public ResponseEntity<?> registerUser(UserDto uDto) {

        if (uDto.login().equals("") || uDto.senha().equals("") || uDto.cpf().equals("") || uDto.email().equals("")) {

            this.response.setMensagem("É necessário preencher todos os campos do usuário");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else if (this.repositorioUI.existsByLogin(uDto.login()) || this.repositorioUI.existsByCpf(uDto.cpf())) {

            this.response.setMensagem("Usuário já cadastrado com esse login ou cpf");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else {
            
            User u = new User(uDto.id(),uDto.login(),uDto.senha(),uDto.cpf(),uDto.email());

            u.setSenha(passwordEncoder.encode(u.getSenha()));
            return new ResponseEntity<>(this.repositorioUI.save(u), HttpStatus.CREATED);

        }

    }

    public ResponseEntity<?> listUsers() {

        return new ResponseEntity<>(this.repositorioUI.findAll().stream().map(x -> new UserResponseDto(x.getLogin(),x.getEmail())), HttpStatus.OK);

    }

    public ResponseEntity<?> editUser(UserDto userDto) {

        if (userDto.login().equals("") || userDto.senha().equals("") || userDto.cpf().equals("") || userDto.email().equals("")) {

            this.response.setMensagem("É necessário preencher todos os campos do usuário");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else if (!this.repositorioUI.existsByLogin(userDto.login())) {
            this.response.setMensagem("Não foi encontrado usuário com os dados informados");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
        } else {

            User u = new User(userDto.id(),userDto.login(),userDto.senha(),userDto.cpf(),userDto.email());
            u.setSenha(this.passwordEncoder.encode(u.getSenha()));
            return new ResponseEntity<>(this.repositorioUI.save(u), HttpStatus.OK);

        }

    }

    public ResponseEntity<?> deleteUser(Long id) {

        if (this.repositorioUI.countById(id) == 0) {

            this.response.setMensagem("Não há usuários com o id informado");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
        } else {

            Optional<User> obj = this.repositorioUI.findById(id);

            User u = obj.get();

            this.repositorioUI.delete(u);

            this.response.setMensagem("Usuario removido com sucesso");

            return new ResponseEntity<>(this.response.getMensagem(), HttpStatus.OK);

        }

    }

    public ResponseEntity<?> selectDeviceById(Long id) {

        if (this.repositorioDI.countById(id) == 0) {

            this.response.setMensagem("Não há dispositivo com o id informado");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else {
            Device d = this.repositorioDI.findById(id).orElse(null);
            return new ResponseEntity<>(new DeviceDto(d.getNome(),d.getId(),d.getUser().getId()), HttpStatus.OK);
        }

    }

    public ResponseEntity<?> registerDevice(DeviceDto dDto) {

        if (dDto.nome().equals("")) {

            this.response.setMensagem("O nome não pode estar em branco");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else if (this.repositorioDI.existsById(dDto.id())) {

            this.response.setMensagem("Dispositivo já cadastrado");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else {
            
            Device d = new Device(dDto.id(),dDto.nome());

            d.setUser(this.usuarioLogado);
            this.usuarioLogado.getDispositivos().add(d);
            return new ResponseEntity<>(this.repositorioDI.save(d), HttpStatus.CREATED);

        }

    }

    public ResponseEntity<?> listDevices() {

        return new ResponseEntity<>(this.repositorioDI.findAll().stream().map(x -> new DeviceDto(x.getNome(),x.getId(),x.getUser().getId())), HttpStatus.OK);

    }

    public ResponseEntity<?> editDevice(DeviceDto dDto) {

        if (this.repositorioDI.countById(dDto.id()) == 0) {

            this.response.setMensagem("Não há dispositivos com o id informado");
            return new ResponseEntity<>(this.response, HttpStatus.NOT_FOUND);
        } else if (dDto.nome().equals("")) {

            this.response.setMensagem("O nome não pode estar em branco");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);

        } else {

            Device d = new Device(dDto.id(),dDto.nome());

            User u = this.repositorioUI.findById(dDto.id()).orElse(null);
            d.setUser(u);

            return new ResponseEntity<>(this.repositorioDI.save(d), HttpStatus.OK);

        }

    }

    public ResponseEntity<?> deleteDevice(Long id) {

        if (this.repositorioDI.countById(id) == 0) {

            this.response.setMensagem("Não há dispositivo com o id informado");
            return new ResponseEntity<>(this.response, HttpStatus.BAD_REQUEST);
        } else {

            Optional<Device> obj = this.repositorioDI.findById(id);

            Device d = obj.get();
            
            this.usuarioLogado.getDispositivos().remove(d);
            this.repositorioDI.delete(d);
            this.response.setMensagem("O dispositivo foi removido com sucesso");
            return new ResponseEntity<>(this.response.getMensagem(), HttpStatus.OK);

        }

    }


}
