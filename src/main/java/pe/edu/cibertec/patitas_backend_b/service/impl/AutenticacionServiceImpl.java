package pe.edu.cibertec.patitas_backend_b.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import pe.edu.cibertec.patitas_backend_b.dto.LoginRequestDTO;
import pe.edu.cibertec.patitas_backend_b.dto.LogoutRequestDTO;
import pe.edu.cibertec.patitas_backend_b.service.AutenticacionService;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    @Autowired
    private ResourceLoader resourceLoader;

    @Override
    public String[] validarUsuario(LoginRequestDTO loginRequestDTO) {
        String[] datosUsuario = null;
        try {
            Resource resource = resourceLoader.getResource("classpath:usuarios.txt");
            BufferedReader reader = new BufferedReader(new FileReader(resource.getFile()));
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] datos = linea.split(";");
                if (loginRequestDTO.tipoDocumento().equals(datos[0]) &&
                        loginRequestDTO.numeroDocumento().equals(datos[1]) &&
                        loginRequestDTO.password().equals(datos[2])) {
                    datosUsuario = new String[]{datos[3], datos[4]};
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datosUsuario;
    }


    @Override
    public Mono<Date> cerrarSesionUsuario(LogoutRequestDTO logoutRequestDTO) {
        Date fechaLogout = new Date();

        return Mono.fromCallable(() -> {
            Path path = Paths.get("D:/Clases/patitas-backend-b/auditoria_test.txt");
            try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                StringBuilder sb = new StringBuilder()
                        .append(logoutRequestDTO.tipoDocumento())
                        .append(";")
                        .append(logoutRequestDTO.numeroDocumento())
                        .append(";")
                        .append(fechaLogout);

                writer.write(sb.toString());
                writer.newLine();
                System.out.println("Registro de auditoría agregado: " + sb.toString());

            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al escribir en auditoria.txt", e);
            }
            return fechaLogout;
        });
    }
}