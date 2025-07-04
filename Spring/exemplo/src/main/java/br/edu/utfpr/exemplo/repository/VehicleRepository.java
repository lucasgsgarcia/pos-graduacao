package br.edu.utfpr.exemplo.repository;

import br.edu.utfpr.exemplo.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository  extends JpaRepository<Vehicle, Long> {
}
