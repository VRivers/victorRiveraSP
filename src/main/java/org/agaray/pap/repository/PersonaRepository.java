package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long>{
	public Persona getByLoginname(String loginname);
	public List<Persona> findAllByOrderByAlturaAsc();
	public List<Persona> findAllByOrderByAlturaDesc();
	public List<Persona> findAllByOrderByFnacDesc();
	public List<Persona> findAllByOrderByLoginnameAsc();
}
