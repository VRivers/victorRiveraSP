package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long>{
	public Venta getByFecha(String fecha);
	public List<Venta> findAllByOrderByFechaAsc();

}
