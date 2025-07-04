package br.edu.utfpr.exemplo;

import br.edu.utfpr.exemplo.controller.VehicleController;
import br.edu.utfpr.exemplo.exceptions.NotFoundException;
import br.edu.utfpr.exemplo.model.User;
import br.edu.utfpr.exemplo.model.vo.VehicleVO;
import br.edu.utfpr.exemplo.service.VehicleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    @InjectMocks
    private VehicleController vehicleController;

    @Mock
    private VehicleService vehicleService;

    private final long vehicleId = 1;
    private final long userId = 1;

    @BeforeEach
    void setupAuthenticatedUser() {
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setEmail("user@example.com");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                mockUser, null, Collections.emptyList()
        );

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);
    }

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnVehicleWhenUserOwnsIt() throws Exception {
        VehicleVO vehicleVO = new VehicleVO();
        vehicleVO.setId(vehicleId);
        vehicleVO.setPlate("ABC1234");

        Mockito.when(vehicleService.show(vehicleId, userId)).thenReturn(vehicleVO);

        VehicleVO result = vehicleController.getVehicle(vehicleId);

        assertNotNull(result);
        assertEquals(vehicleId, result.getId());
        assertEquals("ABC1234", result.getPlate());
    }

    @Test
    void shouldThrowNotFoundWhenUserDoesNotOwnVehicle() throws Exception {
        Mockito.when(vehicleService.show(vehicleId, userId))
                .thenThrow(new NotFoundException("Veículo não encontrado!", 404));

        NotFoundException ex = assertThrows(NotFoundException.class, () -> {
            vehicleController.getVehicle(vehicleId);
        });

        assertEquals("Veículo não encontrado!", ex.getMessage());
        assertEquals(404, ex.getErrorCode());
    }
}
