package org.agaray.pap.controller;

import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Categoria;
import org.agaray.pap.domain.Pais;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.CategoriaRepository;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
	
	@Autowired 
	private PersonaRepository personaRepository;
	
	@Autowired 
	private PaisRepository paisRepository;
	
	@Autowired 
	private CategoriaRepository categoriaRepository;
	
	
// =====================================OPERACIONES DE "PAIS" ==================================================
	@GetMapping("/pais/c")
	public String cPaisGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/pais/c");
		return "/_t/frame";
		}
	
	
	@PostMapping("/pais/c")
	public void cPaisPost(
		@RequestParam("nombre") String nombre,
		HttpSession s
		) throws DangerException, InfoException {
		H.isRolOK("admin", s); 
		try {
		
		Pais pais= new Pais(nombre);
		
		paisRepository.save(pais);
		}
		catch (Exception e) {
			PRG.error("Pais ya existente", "/pais/c");
		}
		PRG.info("Pais "+nombre+" creado", "/pais/r");
			
		}
	
	
	@GetMapping("/pais/r")
	public String rPaisGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s); 
		m.put("paises", paisRepository.findAllByOrderByNombreAsc());
		m.put("view", "/pais/r");
		return "/_t/frame";
			}
	
		
	@GetMapping("/pais/u")
	public String uPaisGet(
			@RequestParam("id") Long idPais,
			@RequestParam("nombre") String nombre,
			ModelMap m, 
			HttpSession s) throws DangerException {
		
			H.isRolOK("admin", s); 
			m.put("idPais", idPais );
			m.put("nombre", nombre );
			m.put("view", "/pais/u");
			return "/_t/frame";
	
			}
	@PostMapping("/pais/u")
	public void uPaisPost(
		@RequestParam("id") Long idPais,
		@RequestParam("nombre") String nombre,
		HttpSession s
		) throws DangerException, InfoException {
		H.isRolOK("admin", s); 
		try {
		
		Pais pais=  (Pais) paisRepository.getOne(idPais);
		pais.setNombre(nombre);
		
		paisRepository.save(pais);
		}
		catch (Exception e) {
			PRG.error("Pais ya existente", "/pais/r");
		}
		PRG.info("Pais "+nombre+" actualizado", "/pais/r");
			
		}
	
	@PostMapping("/pais/d")
	public void dPaisPost(
		@RequestParam("id") Long idPais,
		HttpSession s
		) throws DangerException, InfoException {
		H.isRolOK("admin", s); 
		try {
		
		Pais pais=  (Pais) paisRepository.getOne(idPais);

		paisRepository.delete(pais);
		}
		catch (Exception e) {
			PRG.error("El pais no ha sido borrado", "/pais/r");
		}
		PRG.info("Pais Borrado", "/pais/r");
			
		}
// =============================================================================================================

// =====================================OPERACIONES DE "CATEGORIA" ==================================================
	@GetMapping("/categoria/c")
	public String cCategoriaGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("admin", s);
		m.put("view", "/categoria/c");
		return "/_t/frame";
		}
	
	
	@PostMapping("/categoria/c")
	public void cCategoriaPost(
		@RequestParam("nombre") String nombre,
		HttpSession s
		) throws DangerException, InfoException {
		H.isRolOK("admin", s); 
		try {
		
		Categoria categoria= new Categoria(nombre);
		
		categoriaRepository.save(categoria);
		}
		catch (Exception e) {
			PRG.error("Categoria ya existente", "/categoria/c");
		}
		PRG.info("Categoria "+nombre+" creado", "/categoria/r");
			
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
			m.put("idCategoria", idCategoria );
			m.put("nombre", nombre );
			m.put("view", "/pais/u");
			return "/_t/frame";
	
			}
	
	@PostMapping("/categoria/u")
	public void uCategoriaPost(
		@RequestParam("id") Long idCategoria,
		@RequestParam("nombre") String nombre,
		HttpSession s
		) throws DangerException, InfoException {
		H.isRolOK("admin", s); 
		try {
		
		Categoria categoria=  (Categoria) categoriaRepository.getOne(idCategoria);
		categoria.setNombre(nombre);
		
		categoriaRepository.save(categoria);
		}
		catch (Exception e) {
			PRG.error("Categoria ya existente", "/categoria/r");
		}
		PRG.info("Categoria "+nombre+" actualizado", "/categoria/r");
			
		}
	
	@PostMapping("/categoria/d")
	public void dCategoriaPost(
		@RequestParam("id") Long idCategoria,
		HttpSession s
		) throws DangerException, InfoException {
		H.isRolOK("admin", s); 
		try {
		
		Categoria categoria=  (Categoria) categoriaRepository.getOne(idCategoria);

		categoriaRepository.delete(categoria);
		}
		catch (Exception e) {
			PRG.error("La categoria no ha sido borrada", "/pais/r");
		}
		PRG.info("Categoria Borrada", "/pais/r");
			
		}
// ===========================================================================================================









}