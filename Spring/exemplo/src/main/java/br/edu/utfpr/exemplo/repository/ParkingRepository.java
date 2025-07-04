package br.edu.utfpr.exemplo.repository;

import br.edu.utfpr.exemplo.model.Parking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ParkingRepository extends JpaRepository<Parking, Long> {
    List<Parking> findByUnavailableUntilIsNullOrUnavailableUntilBefore(LocalDateTime now);
}
