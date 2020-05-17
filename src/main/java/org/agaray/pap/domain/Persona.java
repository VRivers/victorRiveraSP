package org.agaray.pap.domain;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(unique = true)
	private String loginname;
	
	@ManyToOne
	private Pais pais;

	private String password;
	
	private Integer altura;
	
	private LocalDate fnac;
	
	
	private String foto;

	
//======================

	public Persona() {
		super();
	}


	public Persona(String loginnamePersona, String passwordPersona, Integer altura, LocalDate fnac, Pais pais, String foto) {
		this.loginname = loginnamePersona;
		this.altura = altura;
		this.fnac=fnac;
		this.password = (new BCryptPasswordEncoder()).encode(passwordPersona);
		this.pais=pais;
		this.foto=foto;
	}



	public Integer getAltura() {
		return altura;
	}


	public void setAltura(Integer altura) {
		this.altura = altura;
	}


	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = (new BCryptPasswordEncoder()).encode(password);
	}

	public LocalDate getFnac() {
		return fnac;
	}

	public void setFnac(LocalDate fnac) {
		this.fnac = fnac;
	}



	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	public Pais getPais() {
		return pais;
	}


	public void setPais(Pais pais) {
		this.pais = pais;
	}

	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}

}
