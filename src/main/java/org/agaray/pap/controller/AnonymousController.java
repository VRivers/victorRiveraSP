
package org.agaray.pap.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;


import javax.servlet.http.HttpSession;

import org.agaray.pap.domain.Pais;
import org.agaray.pap.domain.Persona;
import org.agaray.pap.exception.DangerException;
import org.agaray.pap.exception.InfoException;
import org.agaray.pap.helper.H;
import org.agaray.pap.helper.PRG;
import org.agaray.pap.repository.PaisRepository;
import org.agaray.pap.repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class AnonymousController {

	@Autowired
	PersonaRepository repoPersona;
	
	@Autowired
	PaisRepository repoPais;
	
	
	
	@Value("${app.uploadFolder}")
	private String UPLOADED_FOLDER;



	@GetMapping("/init")
	public String initGet(ModelMap m) throws DangerException {
		if (repoPersona.getByLoginname("Administrador") != null) {
			PRG.error("BD no vacía");
		}
		m.put("view", "/anonymous/init");
		return "/_t/frame";
	}
	
	
	@PostMapping("/init")
	public String initPost(@RequestParam("password") String password, ModelMap m) throws DangerException {
		if (repoPersona.getByLoginname("admin") != null) {
			PRG.error("Operación no válida. BD no vacía");
		}
		BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
		if (!bpe.matches(password, bpe.encode("admin"))) { // Password harcoded
			PRG.error("Contraseña incorrecta","/init");
		}
		
		repoPersona.deleteAll();
		repoPersona.save(new Persona("Administrador","admin",170,LocalDate.now(),null, null));
		return "redirect:/";
	}
	
	
	@GetMapping("/info")
	public String info(HttpSession s, ModelMap m) {

		String mensaje = s.getAttribute("_mensaje") != null ? (String) s.getAttribute("_mensaje")
				: "Pulsa para volver a home";
		String severity = s.getAttribute("_severity") != null ? (String) s.getAttribute("_severity") : "info";
		String link = s.getAttribute("_link") != null ? (String) s.getAttribute("_link") : "/";

		s.removeAttribute("_mensaje");
		s.removeAttribute("_severity");
		s.removeAttribute("_link");

		m.put("mensaje", mensaje);
		m.put("severity", severity);
		m.put("link", link);

		m.put("view", "/_t/info");
		return "/_t/frame";
	}

	@GetMapping("/")
	public String home(ModelMap m) {
		m.put("view", "/anonymous/home");
		return "/_t/frame";
	}
	
	
//===========================================================================
// MÉTODOS DE REGISTRO
	
	@GetMapping("/persona/c")
	public String cPersonaGet(ModelMap m) {
		m.put("paises", repoPais.findAll());
		m.put("view", "persona/c");
		return "/_t/frame";
	}
	

	

	@PostMapping("/persona/c")
	public void cPersonaPost (
		@RequestParam("loginname") String loginname,
		@RequestParam("password") String password,
		@RequestParam("altura") Integer altura,
		@RequestParam("fechaNacimiento") @DateTimeFormat(iso = ISO.DATE) LocalDate fechaNacimiento,
		@RequestParam("pais") Pais pais,
		@RequestParam("foto") MultipartFile foto
		)throws DangerException, InfoException 
	{
		try {
			
		
		
		//Guardado de la foto en carpeta local y extracción de formato de la imagen
		String extFoto = null;
		extFoto = (foto.getOriginalFilename().split("\\."))[1];
		
	
		Persona persona = new Persona (loginname,password,altura,fechaNacimiento, pais, extFoto); //TODO 
		repoPersona.save(persona);
		
		
		byte[] bytes = foto.getBytes();
	    Path path = Paths.get(UPLOADED_FOLDER + "persona-" + persona.getId() + "." + persona.getFoto());
		Files.write(path, bytes);
			
			
		
		}
		catch (Exception e) {
			PRG.error("LOGINNAME DUPLICADO");
		}
		PRG.info("Usuario '"+loginname+"' registrado con éxito", "/");
		
	}
	
//===========================================================================
	
	
	@GetMapping("/login")
	public String loginGet(ModelMap m, HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		m.put("view", "/anonymous/login");
		return "/_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(
			@RequestParam("loginname") String loginname, 
			@RequestParam("password") String password,
			ModelMap m, HttpSession s) 
			throws DangerException {
		H.isRolOK("anon", s);

		try {
			Persona persona = repoPersona.getByLoginname(loginname);
			if (!(new BCryptPasswordEncoder()).matches(password, persona.getPassword())) {
				throw new Exception("Contraseña Incorrecta");
			}
			s.setAttribute("persona", persona);
		} catch (Exception e) {
			PRG.error(e.getMessage(), "/login");
		}

		return "redirect:/";
	}

	@GetMapping("/logout")
	public String logout(HttpSession s) throws DangerException {
		H.isRolOK("auth", s);

		s.invalidate();
		return "redirect:/";
	}
}