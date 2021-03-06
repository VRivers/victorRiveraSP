package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.Pais;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaisRepository extends JpaRepository<Pais, Long>{
	public Pais getByNombre(String nombre);
	public List<Pais> findAllByOrderByNombreAsc();

}
