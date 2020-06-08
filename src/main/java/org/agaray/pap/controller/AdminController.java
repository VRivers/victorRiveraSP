package org.agaray.pap.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Categoria;
import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Persona;
import org.agaray.pap.domain.Producto;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.CategoriaRepository;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.agaray.pap.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class AdminController {
	
	@Autowired
	private PersonaRepository personaRepository;

	@Autowired
	private PaisRepository paisRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Autowired
	private ProductoRepository productoRepository;
	
	@Value("${app.uploadFolderProducto}")
	private String UPLOADED_FOLDER;
	
	


// ===================================== AJAX ==================================================

	@ResponseBody
	@PostMapping(value="/producto/cAJAX", produces="text/plain")
	public String cAJAX(
			@RequestParam String nombreProducto
			) throws JsonProcessingException{
		
		HashMap<String, Integer> nombre = new HashMap<>();
		
			if (productoRepository.getByNombre(nombreProducto) != null) {
				nombre.put("coincide", 1);
			}
			else {
				nombre.put("coincide", 0);
			}
					
		return new ObjectMapper().writeValueAsString(nombre);
		
	}
	
	
// ================================ OPERACIONES DE "PERSONA" ============================================
	
	@GetMapping("/persona/r")
	public String rPersonaGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("personas", personaRepository.findAllByOrderByLoginnameAsc());
		m.put("view", "/persona/r");
		return "/_t/frame";
	}

	@GetMapping("/persona/u")
	public String uPersonaGet(
			@RequestParam("id") Long idPersona, 
			@RequestParam("loginname") String loginname,
			@RequestParam("idPais") Long idPais,
			@RequestParam("nombrePais") String nombrePais,
			@RequestParam("altura") Integer altura, 
			@RequestParam("fechaNacimiento") @DateTimeFormat(iso = ISO.DATE) LocalDate fechaNacimiento, 
			ModelMap m, HttpSession s)
			throws DangerException {

		H.isRolOK("admin", s);
		m.put("idPersona", idPersona);
		m.put("loginname", loginname);
		m.put("altura", altura);
		m.put("idPais", idPais);
		m.put("nombrePais", nombrePais);
		m.put("fechaNacimiento", fechaNacimiento);
		m.put("paises", paisRepository.findAll());
		m.put("view", "/persona/u");
		return "/_t/frame";

	}

	@PostMapping("/persona/u")
	public void uPersonaPost(
			@RequestParam("id") Long idPersona, 
			@RequestParam("loginname") String loginname,
			@RequestParam("altura") Integer altura, 
			@RequestParam("fechaNacimiento") @DateTimeFormat(iso = ISO.DATE) LocalDate fechaNacimiento,
			@RequestParam("idPais") Long idPais,
			ModelMap m, HttpSession s)
			throws DangerException, InfoException {
			H.isRolOK("admin", s);
			
		try {

			Persona persona = (Persona) personaRepository.getOne(idPersona);
			Pais pais = (Pais) paisRepository.getOne(idPais);
			persona.setLoginname(loginname);
			persona.setAltura(altura);
			persona.setFnac(fechaNacimiento);
			pais.getPersonas().add(persona);
			persona.setPais(pais);

			personaRepository.save(persona);
			
		} catch (Exception e) {
			PRG.error("Persona ya existente", "/producto/r");
		}
		PRG.info("Persona con loginname " + loginname + " actualizada", "/persona/r");

	}

	@PostMapping("/persona/d")
	public String dPersonaPost(
			@RequestParam("id") Long idPersona, 
			HttpSession s)
			throws DangerException, InfoException {
			H.isRolOK("admin", s);
			
		try {

			Persona persona = (Persona) personaRepository.getOne(idPersona);

			personaRepository.delete(persona);
			
		} catch (Exception e) {
			PRG.error("La persona no ha sido borrado", "/persona/r");
		}
		return "redirect:/persona/r";

	}
	

	
// =====================================OPERACIONES DE "PAIS" ==================================================
	@GetMapping("/pais/c")
	public String cPaisGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/pais/c");
		return "/_t/frame";
	}

	@PostMapping("/pais/c")
	public void cPaisPost(@RequestParam("nombre") String nombre, HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Pais pais = new Pais(nombre);

			paisRepository.save(pais);
		} catch (Exception e) {
			PRG.error("Pais ya existente", "/pais/c");
		}
		PRG.info("Pais " + nombre + " creado", "/pais/r");

	}

	@GetMapping("/pais/r")
	public String rPaisGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("paises", paisRepository.findAllByOrderByNombreAsc());
		m.put("view", "/pais/r");
		return "/_t/frame";
	}

	@GetMapping("/pais/u")
	public String uPaisGet(@RequestParam("id") Long idPais, @RequestParam("nombre") String nombre, ModelMap m,
			HttpSession s) throws DangerException {

		H.isRolOK("admin", s);
		m.put("idPais", idPais);
		m.put("nombre", nombre);
		m.put("view", "/pais/u");
		return "/_t/frame";

	}

	@PostMapping("/pais/u")
	public void uPaisPost(@RequestParam("id") Long idPais, @RequestParam("nombre") String nombre, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Pais pais = (Pais) paisRepository.getOne(idPais);
			pais.setNombre(nombre);

			paisRepository.save(pais);
		} catch (Exception e) {
			PRG.error("Pais ya existente", "/pais/r");
		}
		PRG.info("Pais " + nombre + " actualizado", "/pais/r");

	}

	@PostMapping("/pais/d")
	public String dPaisPost(@RequestParam("id") Long idPais, HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Pais pais = (Pais) paisRepository.getOne(idPais);

			paisRepository.delete(pais);
		} catch (Exception e) {
			PRG.error("El pais no ha sido borrado", "/pais/r");
		}
		return "redirect:/pais/r";

	}
// ==================================================================================================================

// =====================================OPERACIONES DE "CATEGORIA" ==================================================
	@GetMapping("/categoria/c")
	public String cCategoriaGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/categoria/c");
		return "/_t/frame";
	}

	@PostMapping("/categoria/c")
	public void cCategoriaPost(@RequestParam("nombre") String nombre, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Categoria categoria = new Categoria(nombre);

			categoriaRepository.save(categoria);
			
		} catch (Exception e) {
			PRG.error("Categoria ya existente", "/categoria/c");
		}
		PRG.info("Categoria " + nombre + " creado", "/categoria/r");

	}

	@GetMapping("/categoria/r")
	public String rCategoriaGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("categorias", categoriaRepository.findAllByOrderByNombreAsc());
		m.put("view", "/categoria/r");
		return "/_t/frame";
	}

	@GetMapping("/categoria/u")
	public String uCategoriaGet(
			@RequestParam("id") Long idCategoria, 
			@RequestParam("nombre") String nombre, 
			ModelMap m,
			HttpSession s) throws DangerException {

		H.isRolOK("admin", s);
		m.put("idCategoria", idCategoria);
		m.put("nombre", nombre);
		m.put("view", "/categoria/u");
		return "/_t/frame";

	}

	@PostMapping("/categoria/u")
	public void uCategoriaPost(
			@RequestParam("id") Long idCategoria, 
			@RequestParam("nombre") String nombre,
			HttpSession s) throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Categoria categoria = (Categoria) categoriaRepository.getOne(idCategoria);
			categoria.setNombre(nombre);

			categoriaRepository.save(categoria);
		} catch (Exception e) {
			PRG.error("Categoria ya existente", "/categoria/r");
		}
		PRG.info("Categoria " + nombre + " actualizado", "/categoria/r");

	}

	@PostMapping("/categoria/d")
	public String dCategoriaPost(@RequestParam("id") Long idCategoria, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Categoria categoria = (Categoria) categoriaRepository.getOne(idCategoria);

			categoriaRepository.delete(categoria);
		} catch (Exception e) {
			PRG.error("La categoria no ha sido borrada", "/categoria/r");
		}
		return "redirect:/categoria/r";

	}
// ==================================================================================================================

// ===================================== OPERACIONES DE "PRODUCTO" ==================================================

	@GetMapping("/producto/c")
	public String cProductoGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("categorias", categoriaRepository.findAll());
		m.put("view", "/producto/c");
		return "/_t/frame";
	}

	@PostMapping("/producto/c")
	public void cProductoPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("stock") Integer stock,
			@RequestParam("precio") Integer precio, 
			@RequestParam("categoria") Long idCategoria, 
			@RequestParam(value ="foto", required = false) MultipartFile foto,
			HttpSession s)
			throws DangerException, InfoException {
			H.isRolOK("admin", s);

		try {

			Producto producto = new Producto(nombre, stock, precio);
			
			
			//Extracción de formato de la imagen
			String extFoto = null;
			if (!foto.isEmpty()) {
				extFoto = (foto.getOriginalFilename().split("\\."))[1];
				producto.setFoto(extFoto);
				
			}
			else {
				producto.setFoto(extFoto);
			}

			// Comprobación de categoria
			try {
				Categoria categoria = categoriaRepository.getOne(idCategoria);
				categoria.getProductos().add(producto);
				producto.setCategoria(categoria);
			} catch (Exception e) {
				throw new Exception("Categoría no especificada");
			}
			
			//Comprobación de guardado del producto
			try {
				productoRepository.save(producto);
			}

			catch (Exception e) {
				throw new Exception("Producto ya existente");
			}
			
			if (producto.getFoto() != null) {
				byte[] bytes = foto.getBytes();
			    Path path = Paths.get(UPLOADED_FOLDER + "producto-" + producto.getId() + "." + producto.getFoto());
				Files.write(path, bytes);
			}
			
		} 
		
		catch (Exception e) {
			PRG.error(e.getMessage(), "/producto/c");
		}
		
		PRG.info("Producto " + nombre + " creado", "/producto/r");

	}

	@GetMapping("/producto/r")
	public String rProductoGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("productos", productoRepository.findAllByOrderByCategoriaNombreAscNombreAsc());
		m.put("view", "/producto/r");
		return "/_t/frame";
	}

	@GetMapping("/producto/u")
	public String uProductoGet(
			@RequestParam("id") Long idProducto, 
			@RequestParam("nombre") String nombre,
			@RequestParam("stock") Integer stock, 
			@RequestParam("precio") Integer precio, 
			@RequestParam("idCategoria") Long idCategoria,
			@RequestParam("nombreCategoria") String nombreCategoria,
			ModelMap m, HttpSession s)
			throws DangerException {

		H.isRolOK("admin", s);
		m.put("idProducto", idProducto);
		m.put("nombre", nombre);
		m.put("stock", stock);
		m.put("precio", precio);
		m.put("idCategoria", idCategoria);
		m.put("nombreCategoria", nombreCategoria);
		m.put("categorias", categoriaRepository.findAll());
		m.put("view", "/producto/u");
		return "/_t/frame";

	}

	@PostMapping("/producto/u")
	public void uProductoPost(
			@RequestParam("id") Long idProducto, 
			@RequestParam("nombre") String nombre,
			@RequestParam("stock") Integer stock, 
			@RequestParam("precio") Integer precio, 
			@RequestParam("idCategoria") Long idCategoria, 
			ModelMap m, HttpSession s)
			throws DangerException, InfoException {
		
		H.isRolOK("admin", s);
		try {

			Producto producto = (Producto) productoRepository.getOne(idProducto);
			Categoria categoria = (Categoria) categoriaRepository.getOne(idCategoria);
			producto.setNombre(nombre);
			producto.setStock(stock);
			producto.setPrecio(precio);
			categoria.getProductos().add(producto);
			producto.setCategoria(categoria);

			productoRepository.save(producto);
		} catch (Exception e) {
			PRG.error("Producto ya existente", "/producto/r");
		}
		PRG.info("Producto " + nombre + " actualizado", "/producto/r");

	}

	@PostMapping("/producto/d")
	public String dProductoPost(@RequestParam("id") Long idProducto, HttpSession s)
			throws DangerException, InfoException {
		H.isRolOK("admin", s);
		try {

			Producto producto = (Producto) productoRepository.getOne(idProducto);

			productoRepository.delete(producto);
		} catch (Exception e) {
			PRG.error("El producto no ha sido borrado", "/producto/r");
		}
		return "redirect:/producto/r";

	}
// ==================================================================================================================



}