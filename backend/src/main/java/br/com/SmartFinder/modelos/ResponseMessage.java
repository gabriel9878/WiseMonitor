package br.com.SmartFinder.modelos;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {

	
	private String mensagem;

	public String getMensagem() {	
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem; 
	}
	
	
}
