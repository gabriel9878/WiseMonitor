package br.com.WiseMonitor.modelos;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;

import lombok.Data;




@Entity
public class User implements UserDetails{

		/**
	 * 
	 */
	
	
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
	    
	    @Column(length = 10,nullable = true)
	    private UserRole role;
	    
		@JsonManagedReference
	    @OneToMany(mappedBy = "user",fetch = FetchType.EAGER,cascade = {CascadeType.REMOVE,CascadeType.MERGE,CascadeType.PERSIST})
	    private List<Device> dispositivos = new ArrayList<>();
		
		
		public User() {
			
		}

		public User(Long id, @NotEmpty String login, @NotEmpty String senha, String cpf, String email,UserRole role) {
			
			super();
			this.id = id;
			this.login = login;
			this.senha = senha;
			this.cpf = cpf;
			this.email = email;
			this.role = role;
			
		}

		public UserRole getRole() {
			return role;
		}
		
		public void setRole(UserRole role) {
			this.role = role;
		}

		
		public Long getId() {
			return id;
		}
		
		public void setId(Long id) {
			this.id = id;
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

		public List<Device> getDispositivos() {
			return dispositivos;
		}

		public void setDispositivos(List<Device> dispositivos) {
			this.dispositivos = dispositivos;
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			
			if(this.role == UserRole.ADMIN) {
				
				return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"),new SimpleGrantedAuthority("ROLE_USER"));
				
			}
			
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}

		@Override
		public String getPassword() {
			// TODO Auto-generated method stub
			return this.getSenha();
		}

		@Override
		public String getUsername() {
			// TODO Auto-generated method stub
			return this.login;
		}

		@Override
		public boolean isAccountNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isEnabled() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean equals(Object obj) {
		    if (this == obj)
		        return true;
		    if (obj == null)
		        return false;
		    if (getClass() != obj.getClass())
		        return false;
		    User other = (User) obj;
		    return Objects.equals(id, other.id);
		}

		@Override
		public int hashCode() {
		    return Objects.hash(id);
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", login=" + login + ", senha=" + senha + ", cpf=" + cpf + ", email=" + email
					+ ", role=" + role + ", dispositivos=" + dispositivos + "]";
		}

		
		
		

}



