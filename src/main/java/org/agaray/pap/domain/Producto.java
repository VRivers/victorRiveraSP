package org.agaray.pap.domain;


import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;



@Entity
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String nombre;
	
	private Integer stock;
	
	private Integer precio;

	private String foto;
	
	@OneToMany(mappedBy = "producto", cascade = CascadeType.PERSIST)
	private Collection<LineaDeVenta> lineasdeventa;
	
	@ManyToOne
	private Categoria categoria;
	


//======================

	public Producto() {
		super();
		this.lineasdeventa = new ArrayList<>();
		
	}
	
	public Producto(String nombre, Integer stock, Integer precio) {
		this.nombre = nombre;
		this.stock = stock;
		this.precio = precio;
		this.lineasdeventa = new ArrayList<>();
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


	public Integer getStock() {
		return stock;
	}


	public void setStock(Integer stock) {
		this.stock = stock;
	}


	public Integer getPrecio() {
		return precio;
	}


	public void setPrecio(Integer precio) {
		this.precio = precio;
	}


	public String getFoto() {
		return foto;
	}


	public void setFoto(String foto) {
		this.foto = foto;
	}


	public Categoria getCategoria() {
		return categoria;
	}


	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Collection<LineaDeVenta> getLineasDeVenta() {
		return lineasdeventa;
	}

	public void setLineasDeVenta(Collection<LineaDeVenta> lineasdeventa) {
		this.lineasdeventa = lineasdeventa;
	}
	
	

}

