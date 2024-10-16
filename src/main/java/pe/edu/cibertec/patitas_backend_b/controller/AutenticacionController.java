package pe.edu.cibertec.patitas_backend_b.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.cibertec.patitas_backend_b.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LoginResponseDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutResponseDTO;
import pe.edu.cibertec.patitas_backend_b.service.AutenticacionService;
import pe.edu.cibertec.patitas_backend_b.service.impl.AutenticacionServiceImpl;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/autenticacion")
@CrossOrigin(origins = "http://localhost:5173")
public class AutenticacionController {

    @Autowired
    private AutenticacionService autenticacionService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            String[] datosUsuario = autenticacionService.validarUsuario(loginRequestDTO);
            if (datosUsuario == null) {
                return new LoginResponseDTO("01", "Usuario no encontrado", "", "");
            }
            return new LoginResponseDTO("00", "", datosUsuario[0], datosUsuario[1]);
        } catch (Exception e) {
            return new LoginResponseDTO("99", "Ocurrió un problema", "", "");
        }
    }

    @PostMapping("/logout")
    public Mono<LogoutResponseDTO> logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        return autenticacionService.cerrarSesionUsuario(logoutRequestDTO)
                .map(fechaLogout -> {
                    if (fechaLogout == null) {
                        return new LogoutResponseDTO(false, null, "Error: No se pudo registrar auditoría");
                    }
                    return new LogoutResponseDTO(true, fechaLogout, "");
                })
                .onErrorResume(e -> {
                    return Mono.just(new LogoutResponseDTO(false, null, "Error: Ocurrió un problema"));
                });
    }
}