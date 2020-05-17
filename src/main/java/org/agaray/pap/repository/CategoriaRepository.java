package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	public Categoria getByNombre(String nombre);
	public List<Categoria> findAllByOrderByNombreAsc();

}
