package org.agaray.pap.repository;

import java.util.List;

import org.agaray.pap.domain.LineaDeVenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LineaDeVentaRepository extends JpaRepository<LineaDeVenta, Long>{
	public LineaDeVenta getByCantidad(Integer cantidad);
	public List<LineaDeVenta> findAllByOrderByCantidadAsc();

}
