package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.Firmling;
import de.acksmi.firmapp.firmpass_backend_project.model.User;
import de.acksmi.firmapp.firmpass_backend_project.security.JwtTokenProvider;
import de.acksmi.firmapp.firmpass_backend_project.service.FirmlingService;
import de.acksmi.firmapp.firmpass_backend_project.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:63343") // Fügen Sie dies hinzu, um CORS für diesen Controller zu ermöglichen
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private FirmlingService firmlingService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody Firmling firmling) {
        String baseUsername = firmling.getFirstName().toLowerCase() + "." + firmling.getLastName().toLowerCase();
        String username = generateUniqueUsername(baseUsername);

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("thomaslorenz")); // Set standard password and encode it
        user.setIsLocked(false);

        User savedUser = userService.saveUser(user);
        firmling.setUser(savedUser);
        firmlingService.saveFirmling(firmling);

        return ResponseEntity.ok(savedUser);
    }

    private String generateUniqueUsername(String baseUsername) {
        String username = baseUsername;
        Random random = new Random();
        while (userService.findByUsername(username) != null) {
            username = baseUsername + random.nextInt(1000); // Append a random number to make it unique
        }
        return username;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtTokenProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint(@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret.getBytes())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String username = claims.getSubject();
                return ResponseEntity.ok("You are logged in as " + username);
            } catch (SignatureException e) {
                return ResponseEntity.status(401).body("Invalid JWT signature");
            } catch (MalformedJwtException e) {
                return ResponseEntity.status(400).body("Malformed JWT token");
            } catch (Exception e) {
                return ResponseEntity.status(500).body("An error occurred while parsing the JWT token");
            }
        }
        return ResponseEntity.ok("This is a public endpoint. Anyone can access this.");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public ResponseEntity<String> userEndpoint() {
        return ResponseEntity.ok("This is a user endpoint. You need a valid JWT with USER role to access this.");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<String> adminEndpoint() {
        return ResponseEntity.ok("This is an admin endpoint. You need a valid JWT with ADMIN role to access this.");
    }
}
