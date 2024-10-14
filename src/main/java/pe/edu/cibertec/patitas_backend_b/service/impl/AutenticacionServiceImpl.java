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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class AutenticacionServiceImpl implements AutenticacionService {

    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public String[] validarUsuario(LoginRequestDTO loginRequestDTO) throws IOException {

        String[] datosUsuario = null;
        Resource resource = resourceLoader.getResource("classpath:usuarios.txt");

        try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {

            String linea;
            while ((linea = br.readLine()) != null) {

                String[] datos = linea.split(";");
                if (loginRequestDTO.tipoDocumento().equals(datos[0]) &&
                        loginRequestDTO.numeroDocumento().equals(datos[1]) &&
                        loginRequestDTO.password().equals(datos[2])) {

                    datosUsuario = new String[2];
                    datosUsuario[0] = datos[3]; // Recuperar nombre
                    datosUsuario[1] = datos[4]; // Recuperar email

                }

            }

        } catch (IOException e) {
            datosUsuario = null;
            throw new IOException(e);
        }

        return datosUsuario;
    }

    @Override
    public Date cerrarSesionUsuario(LogoutRequestDTO logoutRequestDTO) throws IOException {

        Date fechaLogout = null;
        Resource resource = resourceLoader.getResource("classpath:auditoria.txt");
        Path rutaArchivo = Paths.get(resource.getURI());

        try (BufferedWriter bw = Files.newBufferedWriter(rutaArchivo, StandardOpenOption.APPEND)){

            //Definir fecha
            fechaLogout = new Date();

            // preparar linea
            StringBuilder sb = new StringBuilder();
            sb.append(logoutRequestDTO.tipoDocumento());
            sb.append(";");
            sb.append(logoutRequestDTO.numeroDocumento());
            sb.append(";");
            sb.append(new Date());

            //Escribir linea
            bw.write(sb.toString());
            bw.newLine();
            System.out.println(sb.toString());
        } catch (IOException e){
            fechaLogout = null;
            throw new IOException(e);

        }
        return fechaLogout;
    }






    private static final String LOGOUT_FILE_PATH = "classpath:auditoria.txt"; // Ruta del archivo

    public void registrarLogout(LogoutRequestDTO logoutRequestDTO) throws IOException {
        // Obtener la fecha y hora actual
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String logEntry = String.format("%s - Tipo Documento: %s, Número Documento: %s%n",
                fechaHoraActual.format(formatter),
                logoutRequestDTO.tipoDocumento(),
                logoutRequestDTO.numeroDocumento());

        // Obtener el recurso y escribir en el archivo
        Resource resource = resourceLoader.getResource(LOGOUT_FILE_PATH);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resource.getFile(), true))) {
            writer.write(logEntry);
        }
    }


}
