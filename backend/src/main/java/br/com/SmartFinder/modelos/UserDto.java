package br.com.SmartFinder.modelos;

public record UserDto(

    Long id,
    String login,
    String senha,
    String cpf,
    String email


){}
