package br.edu.utfpr.exemplo.service;

import br.edu.utfpr.exemplo.exceptions.NotFoundException;
import br.edu.utfpr.exemplo.exceptions.parking.UnavailableParkingException;
import br.edu.utfpr.exemplo.model.Parking;
import br.edu.utfpr.exemplo.model.Vehicle;
import br.edu.utfpr.exemplo.model.vo.ParkingVO;
import br.edu.utfpr.exemplo.repository.ParkingRepository;
import br.edu.utfpr.exemplo.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ParkingVO GetParking(long parkingId) throws UnavailableParkingException {
        Parking parking = parkingRepository.findById(parkingId)
                .orElseThrow(() -> new UnavailableParkingException("Estacionamento não encontrado.", 404));

        return modelMapper.map(parking, ParkingVO.class);
    }

    public Parking GetParkingModel(long parkingId) throws UnavailableParkingException {
        return parkingRepository.findById(parkingId)
                .orElseThrow(() -> new UnavailableParkingException("Estacionamento não encontrado.", 404));
    }

    public List<ParkingVO> getAvailableParkings(Optional<LocalDateTime> parkDate) {
        LocalDateTime date = parkDate.orElse(LocalDateTime.now());

        List<Parking> parkings = Optional.ofNullable(
                parkingRepository.findByUnavailableUntilIsNullOrUnavailableUntilBefore(date)
        ).orElse(Collections.emptyList());

        return parkings.stream()
                .map(parking -> modelMapper.map(parking, ParkingVO.class))
                .toList();
    }

    @Transactional
    public void ParkVehicle(long parkingId, long vehicleId, LocalDateTime startDate, LocalDateTime endDate, long userId) throws UnavailableParkingException, NotFoundException {
        Parking parking = GetParkingModel(parkingId);

        if (parking.getUnavailableUntil() != null && parking.getUnavailableUntil().isAfter(LocalDateTime.now())) {
            throw new UnavailableParkingException("Estacionamento indisponível.", 400);
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado.", 404));

        if (vehicle.getUser() == null || vehicle.getUser().getId() != userId) {
            throw new NotFoundException("Veículo não encontrado.", 404);
        }

        parking.setUnavailableUntil(endDate);
        parking.getVehicles().add(vehicle);
        parkingRepository.save(parking);
    }

    @Transactional
    public ParkingVO RemoveParking(long parkingId, long vehicleId, long userId) throws UnavailableParkingException, NotFoundException {
        Parking parking = GetParkingModel(parkingId);

        if (parking.getUnavailableUntil() != null && parking.getUnavailableUntil().isAfter(LocalDateTime.now())) {
            throw new UnavailableParkingException("Estacionamento indisponível.", 401);
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new UnavailableParkingException("Veículo não encontrado.", 404));

        if (vehicle.getUser() == null || vehicle.getUser().getId() != userId) {
            throw new NotFoundException("Veículo não encontrado.", 404);
        }

        parking.getVehicles().remove(vehicle);
        parkingRepository.save(parking);
        return modelMapper.map(parking, ParkingVO.class);
    }
}
