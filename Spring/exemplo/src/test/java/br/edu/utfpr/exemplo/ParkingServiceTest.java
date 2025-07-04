package br.edu.utfpr.exemplo;

import br.edu.utfpr.exemplo.exceptions.NotFoundException;
import br.edu.utfpr.exemplo.exceptions.parking.UnavailableParkingException;
import br.edu.utfpr.exemplo.model.Parking;
import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.Vehicle;
import br.edu.utfpr.exemplo.model.vo.ParkingVO;
import br.edu.utfpr.exemplo.repository.ParkingRepository;
import br.edu.utfpr.exemplo.repository.VehicleRepository;
import br.edu.utfpr.exemplo.service.ParkingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ParkingServiceTest {

    @InjectMocks
    private ParkingService parkingService;

    @Mock
    private ParkingRepository parkingRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private ModelMapper modelMapper;

    @Test
    void shouldReturnAvailableParkings() {
        LocalDateTime now = LocalDateTime.now();
        List<Parking> parkings = List.of(new Parking());

        when(parkingRepository.findByUnavailableUntilIsNullOrUnavailableUntilBefore(now)).thenReturn(parkings);
        when(modelMapper.map(any(Parking.class), eq(ParkingVO.class))).thenReturn(new ParkingVO());

        List<ParkingVO> result = parkingService.getAvailableParkings(Optional.of(now));

        assertEquals(1, result.size());
        verify(parkingRepository).findByUnavailableUntilIsNullOrUnavailableUntilBefore(now);
    }

    @Test
    void shouldReturnEmptyWhenNoAvailableParkings() {
        LocalDateTime now = LocalDateTime.now();
        when(parkingRepository.findByUnavailableUntilIsNullOrUnavailableUntilBefore(now)).thenReturn(null);

        List<ParkingVO> result = parkingService.getAvailableParkings(Optional.of(now));

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldParkVehicleSuccessfully() throws Exception {
        long parkingId = 1L, vehicleId = 2L, userId = 3L;
        LocalDateTime start = LocalDateTime.now(), end = start.plusHours(1);

        Parking parking = new Parking();
        parking.setUnavailableUntil(null);
        parking.setVehicles(new ArrayList<>());

        Vehicle vehicle = new Vehicle();
        vehicle.setUser(new User());
        vehicle.getUser().setId(userId);

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        parkingService.ParkVehicle(parkingId, vehicleId, start, end, userId);

        assertEquals(end, parking.getUnavailableUntil());
        assertTrue(parking.getVehicles().contains(vehicle));
        verify(parkingRepository).save(parking);
    }

    @Test
    void shouldThrowWhenParkingUnavailable() {
        long parkingId = 1L;
        Parking parking = new Parking();
        parking.setUnavailableUntil(LocalDateTime.now().plusHours(1));

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));

        assertThrows(UnavailableParkingException.class, () ->
                parkingService.ParkVehicle(parkingId, 0, LocalDateTime.now(), LocalDateTime.now(), 0));
    }

    @Test
    void shouldThrowWhenVehicleNotFoundOrWrongUser() {
        long parkingId = 1L, vehicleId = 2L;

        Parking parking = new Parking();
        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
                parkingService.ParkVehicle(parkingId, vehicleId, LocalDateTime.now(), LocalDateTime.now(), 0));
    }

    @Test
    void shouldRemoveVehicleSuccessfully() throws Exception {
        long parkingId = 1L, vehicleId = 2L, userId = 3L;

        Parking parking = new Parking();
        parking.setUnavailableUntil(null);
        parking.setVehicles(new ArrayList<>());

        Vehicle vehicle = new Vehicle();
        vehicle.setUser(new User());
        vehicle.getUser().setId(userId);
        parking.getVehicles().add(vehicle);

        ParkingVO expectedVO = new ParkingVO();

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));
        when(modelMapper.map(parking, ParkingVO.class)).thenReturn(expectedVO);

        ParkingVO result = parkingService.RemoveParking(parkingId, vehicleId, userId);

        assertEquals(expectedVO, result);
        assertFalse(parking.getVehicles().contains(vehicle));
        verify(parkingRepository).save(parking);
    }

    @Test
    void shouldThrowOnRemoveIfVehicleInvalidOrUserMismatch() {
        long parkingId = 1L, vehicleId = 2L, userId = 3L;

        Parking parking = new Parking();
        parking.setUnavailableUntil(null);

        Vehicle vehicle = new Vehicle();
        vehicle.setUser(new User());
        vehicle.getUser().setId(999L); // outro usuário

        when(parkingRepository.findById(parkingId)).thenReturn(Optional.of(parking));
        when(vehicleRepository.findById(vehicleId)).thenReturn(Optional.of(vehicle));

        assertThrows(NotFoundException.class, () ->
                parkingService.RemoveParking(parkingId, vehicleId, userId));
    }
}
