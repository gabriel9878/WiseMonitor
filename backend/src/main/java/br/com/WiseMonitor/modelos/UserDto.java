package br.com.WiseMonitor.modelos;

public record UserDto(

    
    String login,
    
    String senha,
   
    String cpf,
    
    String email,

    UserRole role
    
){}

 