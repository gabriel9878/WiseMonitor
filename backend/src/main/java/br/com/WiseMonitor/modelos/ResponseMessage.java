package br.com.WiseMonitor.modelos;

import org.springframework.stereotype.Component;

@Component
public class ResponseMessage {

	
	private String mensagem;

	
	public ResponseMessage(){

		this.mensagem = "";

	}


	public String getMensagem() {	
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem; 
	}
	
	
}
