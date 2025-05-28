package br.edu.utfpr.exemplo.controller;

import br.edu.utfpr.exemplo.model.vo.VehicleVO;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/parking")
public class ParkingController {

    private int[] parking = new int[10];
    private String[] parkingPlates = new String[10];

    public ParkingController() {
        for (int i = 0; i < 10; i++) {
            parking[i] = i;
            parkingPlates[i] = null;
        }
    }

    @GetMapping
    public Map<Integer, Optional<String>> GetParking() {
        Map<Integer, Optional<String>> parkingWithPlates = new HashMap<>();
        for(Integer park : parking) {
            parkingWithPlates.put(park, Optional.ofNullable(parkingPlates[park]));
        }
        return parkingWithPlates;
    }

    @PostMapping("{park}")
    public Map<Integer, Optional<String>> ParkCar(@PathVariable("park") int park, @RequestBody VehicleVO vehicle){
        boolean isParked = Arrays.stream(parkingPlates).anyMatch(plate -> plate != null &&
                plate.equalsIgnoreCase(vehicle.getPlate()));
        boolean hasCarInPark = parkingPlates[park] != null;
        if(isParked || hasCarInPark){
            return GetParking();
        }

        parkingPlates[park] = vehicle.getPlate();
        return GetParking();
    }

    @DeleteMapping("{park}")
    public Map<Integer, Optional<String>> RemoveParking(@PathVariable("park") int park) {
        boolean hasCarInPark = parkingPlates[park] != null;
        if(!hasCarInPark){
            return GetParking();
        }

        parkingPlates[park] = null;
        return GetParking();
    }
}
