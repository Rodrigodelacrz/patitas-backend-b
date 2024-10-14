package pe.edu.cibertec.patitas_backend_b.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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


@RestController
@RequestMapping("/autenticacion")
public class AutenticacionController {

    @Autowired
    AutenticacionService autenticacionService;

    @Autowired
    AutenticacionServiceImpl autenticacionServiceimpl;


    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequestDTO) {

        try {

            Thread.sleep(Duration.ofSeconds(4));
            String[] datosUsuario = autenticacionService.validarUsuario(loginRequestDTO);
            System.out.println("Respuesta Backend: " + Arrays.toString(datosUsuario));

            if (datosUsuario == null) {
                return new LoginResponseDTO("01", "Error: Usuario no encontrado", "", "");
            }
            return new LoginResponseDTO("00", "", datosUsuario[0], datosUsuario[1]);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new LoginResponseDTO("99", "Error: Ocurrión un problema", "", "");

        }
    }


  //  @PostMapping("/logout")
 //   public LogoutResponseDTO logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {

    //    try {
      //      Thread.sleep(Duration.ofSeconds(5));
        //    Date fechaLogout = autenticacionService.cerrarSesionUsuario(logoutRequestDTO);
          //  System.out.println("Respuesta Backend: " + fechaLogout);

            //if (fechaLogout == null){
             //       return new LogoutResponseDTO( false,null,"Error: No se púdo registrar audiotria");
            //}
            //return new LogoutResponseDTO(true,fechaLogout,"");


        //} catch (Exception e) {
          //  System.out.println(e.getMessage());
       //     return new LogoutResponseDTO(false, null, "Error ocurrio un problema");
     //   }


    //}
    @PostMapping("/logout")
    public LogoutResponseDTO logout(@RequestBody LogoutRequestDTO logoutRequestDTO) {
        try {
            // Lógica para registrar el logout
            autenticacionServiceimpl.registrarLogout(logoutRequestDTO);

            // Aquí puedes realizar la lógica adicional que necesites, como devolver una respuesta de éxito
            return new LogoutResponseDTO(true, LocalDateTime.now(), "Logout exitoso");

        } catch (IOException e) {
            e.printStackTrace();
            return new LogoutResponseDTO(false, null, "Error: No se pudo registrar el logout.");
        } catch (Exception e) {
            e.printStackTrace();
            return new LogoutResponseDTO(false, null, "Error: Ocurrió un problema.");
        }
    }

}
