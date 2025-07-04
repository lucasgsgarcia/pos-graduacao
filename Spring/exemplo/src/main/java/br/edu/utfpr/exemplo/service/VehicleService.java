package br.edu.utfpr.exemplo.service;

import br.edu.utfpr.exemplo.exceptions.NotFoundException;
import br.edu.utfpr.exemplo.exceptions.authorization.UnauthorizedOperationException;
import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.Vehicle;
import br.edu.utfpr.exemplo.model.vo.VehicleVO;
import br.edu.utfpr.exemplo.repository.UserRepository;
import br.edu.utfpr.exemplo.repository.VehicleRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class VehicleService {

    @Autowired
    private VehicleRepository repository;

    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper;

    public VehicleVO show(long vehicleId, long userId) throws NotFoundException {
        Optional<Vehicle> alreadyExistingVehicle = repository.findById(vehicleId);
        if (alreadyExistingVehicle.isEmpty() || alreadyExistingVehicle.get().getUser().getId() != userId) {
            throw new NotFoundException("Veículo não encontrado!", 404);
        }

        return modelMapper.map(alreadyExistingVehicle, VehicleVO.class);
    }

    public VehicleVO createOrUpdateVehicle(VehicleVO vehicleVO, long userId)
            throws UnauthorizedOperationException, NotFoundException
    {
        Vehicle vehicleModel = modelMapper.map(vehicleVO, Vehicle.class);
        Vehicle savedVehicle;

        if (vehicleModel.getId() != null) {
            Vehicle existing = repository.findById(vehicleModel.getId())
                    .orElseThrow(() -> new NotFoundException("Veículo não encontrado.", 404));

            if (existing.getUser() == null || existing.getUser().getId() != userId) {
                throw new UnauthorizedOperationException("Este veículo não pode ser editado.", 401);
            }
            modelMapper.map(vehicleVO, existing);
            savedVehicle = repository.save(existing);
            return modelMapper.map(savedVehicle, VehicleVO.class);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado.", 404));
        vehicleModel.setUser(user);
        savedVehicle = repository.save(vehicleModel);
        return modelMapper.map(savedVehicle, VehicleVO.class);
    }

    public boolean deleteVehicle(long vehicleId, long userId) throws UnauthorizedOperationException, NotFoundException {
        Optional<Vehicle> alreadyExistingVehicle = repository.findById(vehicleId);
        if (alreadyExistingVehicle.isEmpty() || alreadyExistingVehicle.get().getUser().getId() != userId) {
            throw new NotFoundException("Veículo não encontrado!", 404);
        }
        repository.delete(alreadyExistingVehicle.get());
        return true;
    }
}
