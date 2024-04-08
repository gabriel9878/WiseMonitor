package br.com.SmartFinder.modelos;

import jakarta.persistence.Column;

public class UserRequest {

	
    @Column(length = 200, nullable = false)
    private String login;
	
    @Column(length = 300, nullable = false)
    private String senha;

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}
