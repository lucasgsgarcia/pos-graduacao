package br.edu.utfpr.exemplo.controller;

import br.edu.utfpr.exemplo.controller.requests.ParkVehicleRequest;
import br.edu.utfpr.exemplo.exceptions.NotFoundException;
import br.edu.utfpr.exemplo.exceptions.parking.UnavailableParkingException;
import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.vo.ParkingVO;
import br.edu.utfpr.exemplo.service.ParkingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("api/parking")
public class ParkingController {

    @Autowired
    private ParkingService parkingService;

    @GetMapping
    public List<ParkingVO> GetParking(@RequestParam(required = false) LocalDateTime date) {
        LocalDateTime resolvedDate = Optional.ofNullable(date).orElse(LocalDateTime.now());
        return parkingService.getAvailableParkings(Optional.of(resolvedDate));
    }

    @PostMapping
    public ParkingVO ParkCar(@RequestBody ParkVehicleRequest requestBody) throws NotFoundException, UnavailableParkingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        parkingService.ParkVehicle(requestBody.parkingId, requestBody.vehicleId, requestBody.startDate, requestBody.endDate, user.getId());
        return parkingService.GetParking(requestBody.parkingId);
    }

    @DeleteMapping("{park}")
    public ParkingVO RemoveParking(@PathVariable("park") long parkingId, @RequestParam long vehicleId) throws NotFoundException, UnavailableParkingException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return parkingService.RemoveParking(parkingId, vehicleId, user.getId());
    }
}
