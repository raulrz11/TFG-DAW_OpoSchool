package com.example.tfgoposchool.auth.services.auth;

import com.example.tfgoposchool.auth.dto.JwtAuthDto;
import com.example.tfgoposchool.auth.dto.SignIn;
import com.example.tfgoposchool.auth.exceptions.EmailAlreadyExists;
import com.example.tfgoposchool.auth.repository.AuthUsersRepository;
import com.example.tfgoposchool.auth.services.jwt.JwtServiceImpl;
import com.example.tfgoposchool.dtos.alumnos.AlumnoCreateDto;
import com.example.tfgoposchool.exceptions.UsuarioNotFoundException;
import com.example.tfgoposchool.mappers.alumnos.AlumnosMapper;
import com.example.tfgoposchool.mappers.subscripciones.SubscripcionesMapper;
import com.example.tfgoposchool.mappers.usuarios.UsuariosMapper;
import com.example.tfgoposchool.models.Alumno;
import com.example.tfgoposchool.models.subscripciones.Subscripcion;
import com.example.tfgoposchool.models.usuarios.Usuario;
import com.example.tfgoposchool.repositories.alumnos.AlumnosRepository;
import com.example.tfgoposchool.repositories.subscripciones.SubscripcionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements AuthService{
    private final AuthUsersRepository authUsersRepository;
    private final JwtServiceImpl jwtService;
    private final AuthenticationManager authenticationManager;
    private final AlumnosRepository alumnosRepository;
    private final AlumnosMapper mapper;
    private final UsuariosMapper usuariosMapper;
    private final SubscripcionRepository subscripcionRepository;
    private final SubscripcionesMapper subscripcionesMapper;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AuthServiceImpl(AuthUsersRepository authUsersRepository, JwtServiceImpl jwtService, AuthenticationManager authenticationManager, AlumnosRepository alumnosRepository, AlumnosMapper mapper, UsuariosMapper usuariosMapper, SubscripcionRepository subscripcionRepository, SubscripcionesMapper subscripcionesMapper, JavaMailSender javaMailSender) {
        this.authUsersRepository = authUsersRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.alumnosRepository = alumnosRepository;
        this.mapper = mapper;
        this.usuariosMapper = usuariosMapper;
        this.subscripcionRepository = subscripcionRepository;
        this.subscripcionesMapper = subscripcionesMapper;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public JwtAuthDto signIn(SignIn signIn) {
        Usuario authUser = authUsersRepository.findByUsername(signIn.getUsername())
                .orElseThrow(() -> new UsuarioNotFoundException("El usuario " + signIn.getUsername() + " no existe"));
         try {
             authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                     signIn.getUsername(), signIn.getPassword()));//se usa username porq es el q hemos especificado en el getUsername de Usuario
             var jwt = jwtService.generateToken(authUser);
             return JwtAuthDto.builder().token(jwt).build();
         }catch (AuthenticationException e){
             throw new UsuarioNotFoundException("El nombre de usuario o contrasena no coinciden");
         }
    }

    @Override
    public List<String> signUp(AlumnoCreateDto signUp) {
        if (!authUsersRepository.existsByEmail(signUp.getEmail())){
            Alumno alumnoMapped = mapper.toEntity(signUp);
            Usuario newUsuario = usuariosMapper.newUsuarioFromAlumno(alumnoMapped);
            authUsersRepository.save(newUsuario);

            //Creacion de la subscripcion
            Subscripcion newSubscripcion = subscripcionesMapper.toEntity(signUp);
            subscripcionRepository.save(newSubscripcion);

            //Agregamos relaciones y guardamos alumno
            newSubscripcion.setAlumno(alumnoMapped);
            alumnoMapped.setUsuario(newUsuario);
            alumnoMapped.setSubscripciones(List.of(newSubscripcion));

            alumnosRepository.save(alumnoMapped);

            enviarCorreo(signUp.getEmail(), newUsuario.getUsername(), UsuariosMapper.USER_PASSWORD);

            return List.of(newUsuario.getUsername(), UsuariosMapper.USER_PASSWORD);
        }else {
            throw new EmailAlreadyExists("El email ya existe");
        }
    }

    private void enviarCorreo(String email, String username, String password) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Bienvenido a la Plataforma");
            message.setText(
                    "Hola,\n\n" +
                            "Tu cuenta ha sido creada con éxito. Aquí están tus credenciales de inicio de sesión:\n\n" +
                            "Nombre de usuario: " + username + "\n" +
                            "Contraseña: " + password + "\n\n" +
                            "Por favor, guarda esta información en un lugar seguro.\n\n" +
                            "!Disfruta del aula virtual!,\nEl equipo de OpoSchool."
            );

            javaMailSender.send(message);
            System.out.println("Correo enviado correctamente.");
        } catch (Exception e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
        }
    }


    public boolean validateRegisterForm(AlumnoCreateDto dto){
        if (!authUsersRepository.existsByEmail(dto.getEmail())){
            return true;
        }else {
            throw new EmailAlreadyExists("El email ya existe");
        }
    }
}
