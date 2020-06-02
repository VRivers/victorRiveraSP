package org.agaray.pap.domain;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;




@Entity
public class Venta {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate fecha;
	
	@OneToOne(mappedBy = "ventaencurso")
	private Persona personaencurso;
	
	@OneToMany(mappedBy = "venta", cascade = CascadeType.PERSIST)
	private Collection<LineaDeVenta> lineasdeventa;
	

	
	
//======================


	public Collection<LineaDeVenta> getLineasdeventa() {
		return lineasdeventa;
	}


	public void setLineasdeventa(Collection<LineaDeVenta> lineasdeventa) {
		this.lineasdeventa = lineasdeventa;
	}


	public Venta() {
		super();
		this.lineasdeventa = new ArrayList<>();
	}


	public Venta(LocalDate fecha) {
		this.fecha=fecha;
		this.lineasdeventa = new ArrayList<>();

	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Persona getPersonaencurso() {
		return personaencurso;
	}


	public void setPersonaencurso(Persona personaencurso) {
		this.personaencurso = personaencurso;
	}


}



