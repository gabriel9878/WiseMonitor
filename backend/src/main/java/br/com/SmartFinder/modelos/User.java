package br.com.SmartFinder.modelos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;


@Data
@Entity
public class User {

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
		
	    @Column(name = "id")
	    private Long id;
		@NotEmpty
	    @Column(length = 200, nullable = false)
	    private String login;
		@NotEmpty
	    @Column(length = 300, nullable = false)
	    private String senha;

	    
	    @Column(length = 11, nullable = false)
	    private String cpf;

	    @Column(length = 254, nullable = false)
	    private String email;
	    
		@JsonManagedReference
	    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER)
	    private List<Device> dispositivos = new ArrayList<>();

		public User(){
			


		}

		
		public User(Long id, String login, String senha, String cpf, String email) {
			this.id = id;
			this.login = login;
			this.senha = senha;
			this.cpf = cpf;
			this.email = email;
			this.dispositivos = null;
		}

		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
		}

		public List<Device> getDispositivos() {
			return dispositivos;
		}

		public void setDispositivos(List<Device> dispositivo) {
			this.dispositivos = dispositivo;
		}

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
		
		
		public String getCpf() {
			return cpf;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}


		

}



