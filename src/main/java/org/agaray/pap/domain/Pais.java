package org.agaray.pap.domain;


import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;




@Entity
public class Pais {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;


	@Column(unique = true)
	private String nombre;
	
	@OneToMany(mappedBy = "pais", cascade = CascadeType.PERSIST)
	private Collection<Persona> personas;

	
//======================

	public Pais() {
		super();
		this.personas = new ArrayList<>();
	}


	public Pais(String nombre) {
		this.nombre=nombre;
		this.personas = new ArrayList<>();
	
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public Collection<Persona> getPersonas() {
		return personas;
	}

	public void setPersonas(Collection<Persona> personas) {
		this.personas = personas;
	}


	
}



