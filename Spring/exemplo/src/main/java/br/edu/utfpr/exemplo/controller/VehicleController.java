package br.edu.utfpr.exemplo.controller;

import br.edu.utfpr.exemplo.exceptions.NotFoundException;
import br.edu.utfpr.exemplo.exceptions.authorization.UnauthorizedOperationException;
import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.vo.VehicleVO;
import br.edu.utfpr.exemplo.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/vehicle")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @GetMapping("/{vehicleId}")
    public VehicleVO getVehicle(@PathVariable Long vehicleId) throws NotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return vehicleService.show(vehicleId, user.getId());
    }

    @PostMapping
    public VehicleVO createOrUpdateVehicle(@RequestBody VehicleVO vehicle) throws UnauthorizedOperationException, NotFoundException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return vehicleService.createOrUpdateVehicle(vehicle, user.getId());
    }

    public void deleteVehicle(@PathVariable Long vehicleId) throws NotFoundException, UnauthorizedOperationException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        vehicleService.deleteVehicle(vehicleId, user.getId()); // ALTERAR DPS
    }
}
