package br.com.WiseMonitor.servico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
/*import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.MockitoAnnotations;

import br.com.SmartFinder.dados.IUserRepository;
import br.com.SmartFinder.modelos.User;

import br.com.SmartFinder.modelos.UserDto;
import br.com.SmartFinder.modelos.LoginRequest;

public class ServiceManagerTest {
    
    @InjectMocks
    private UserService service;
    @Mock
    private UserMapper userMapper;
    //@Mock
  //  private DeviceMapper deviceMapper;
    @Mock
    private IUserRepository repositorioUI;
   /* @Mock
    private IDeviceRepository repositorioDI;
    @Mock
    private ResponseMessage response;

    @BeforeEach
    public void setUp(){

        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testRegisterUser(){

        UserRequestDto uDto = new UserDto("loginuser", "senhauser", "cpfuser12-12", "email@user.user");
        User u = new User(0L, "loginuser", "senhauser", "cpfuser12-12", "email@user.user");
       

        //Criando mocks dos métodos chamados no método de cadastro.
        Mockito.when(userMapper.dtoToUser(any(UserRequestDto.class))).thenReturn(u);
        Mockito.when(this.repositorioUI.save(u)).thenReturn(u);
        Mockito.when(this.userMapper.userToDtoResponse(u)).thenReturn(new LoginRequest(u.getLogin(),u.getEmail()));
        
        LoginRequest urDto =  (LoginRequest) this.service.registerUser(uDto).getBody(); 

        assertEquals(urDto.login(), uDto.login());
        assertEquals(urDto.senha(),uDto.senha());

        Mockito.verify(userMapper,times(1)).dtoToUser(uDto);
        Mockito.verify(repositorioUI,times(1)).save(u);
        Mockito.verify(userMapper,times(1)).userToDtoResponse(u);
        
        

    }

    @Test
    public void testListUsers(){

        User u = new User(0L, "loginteste", "senhateste", "cpfcpfcpf-1", "email@teste.teste");
        List<User> usersList = new ArrayList<>();

        usersList.add(u);

        Mockito.when(this.userMapper.userToDtoResponse(any(User.class)))
            .thenReturn(new LoginRequest("loginteste","email@teste.teste"));

        Mockito.when(this.repositorioUI.findAll())
            .thenReturn(usersList);
 
        
        ArrayList<LoginRequest> dtosList =  (ArrayList<LoginRequest>) this.service.listUsers().getBody();

        assertEquals(usersList.size(), dtosList.size());
        verify(this.repositorioUI,times(1)).findAll();

    }

    @Test
    public void testFindUserByIdWhenUserExists(){

        User u = new User(13L,"loginteste", "senhateste","cpfcpfcpf-0", "email@email.teste");
       
        Mockito.when(this.repositorioUI.countById(u.getId())).thenReturn(1L);
        Mockito.when(this.repositorioUI.findById(u.getId())).thenReturn(Optional.of(u));
        Mockito.when(this.userMapper.userToDtoResponse(any(User.class))).thenReturn(new LoginRequest(u.getLogin(),u.getEmail()));
    
        LoginRequest urDto = (LoginRequest)this.service.selectUserById(u.getId()).getBody();

        assertEquals(u.getLogin(), urDto.login());
        assertEquals(u.getEmail(), urDto.senha());

        Mockito.verify(this.repositorioUI,times(1)).countById(u.getId());
        Mockito.verify(this.repositorioUI,times(1)).findById(u.getId());
        Mockito.verify(this.userMapper,times(1)).userToDtoResponse(any(User.class));

    }

}
*/