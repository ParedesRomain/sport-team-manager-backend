package RomainParedes.sport_team_manager;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenGenerator tokenGenerator;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager,
                          TokenGenerator tokenGenerator,
                          UtilisateurRepository utilisateurRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.tokenGenerator = tokenGenerator;
        this.utilisateurRepository = utilisateurRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public static class LoginRequest {
        public String username;
        public String password;
    }

    public static class RegisterRequest {
        public String username;
        public String password;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> login(@RequestBody LoginRequest requete) {
        if (requete.username == null || requete.password == null) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requete.username, requete.password));

            String jwt = tokenGenerator.generateJwtToken(authentication);
            UserDetails utilisateur = (UserDetails) authentication.getPrincipal();
            List<String> roles = utilisateur.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            return ResponseEntity.ok(new JwtDTO(jwt, utilisateur.getUsername(), roles));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest requete) {
        if (requete.username == null || requete.username.isBlank()
                || requete.password == null || requete.password.length() < 6) {
            return ResponseEntity.badRequest().build();
        }

        if (utilisateurRepository.existsByUsername(requete.username)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setUsername(requete.username);
        utilisateur.setPassword(passwordEncoder.encode(requete.password));
        utilisateur.setRole(Role.ENTRAINEUR);
        utilisateurRepository.save(utilisateur);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}