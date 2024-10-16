package pe.edu.cibertec.patitas_backend_b.service;

import pe.edu.cibertec.patitas_backend_b.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutRequestDTO;

import java.io.IOException;
import java.util.Date;
import reactor.core.publisher.Mono;

public interface AutenticacionService {

    String[] validarUsuario(LoginRequestDTO loginRequestDTO) throws IOException;

    Mono<Date> cerrarSesionUsuario(LogoutRequestDTO logoutRequestDTO);
}