package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.*;
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

import java.nio.charset.StandardCharsets;
import java.util.Random;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:63343", "https://acksmi.de:32774", "http://localhost:32774", "https://firmstart.acksmi.de", "https://cloud.acksmi.de"})
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
        user.setPassword("thomaslorenz"); // Set standard password and encode it
        user.setIsLocked(false);

        User savedUser = userService.saveNewUser(user);
        firmling.setUser(savedUser);

        //TODO: Löschen weil Test!
        firmling.setFirmgruppe(Firmgruppe.MONTAG);

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

    @PostMapping("/validateToken")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
        System.out.println("TOKENREQUEST " + tokenRequest.getToken());
        System.out.println("TOKEN " + tokenRequest.getToken());
        try {
            boolean isValid = jwtTokenProvider.validateJwtToken(tokenRequest.getToken());
            if (isValid) {
                return ResponseEntity.ok("Token is valid " + jwtTokenProvider.getUserNameFromJwtToken(tokenRequest.getToken()));
            } else {
                return ResponseEntity.status(401).body("Token is invalid");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token validation failed: " + e.getMessage());
        }
    }

    @GetMapping("/secure-user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> getSecureData() {
        String responseData = "You are user";
        return ResponseEntity.ok(responseData);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateJwtToken(authentication);
            return ResponseEntity.ok(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Login failed: " + e.getMessage());
        }
    }

    @GetMapping("/public")
    public ResponseEntity<String> publicEndpoint(@RequestHeader HttpHeaders headers) {
        String token = headers.getFirst(HttpHeaders.AUTHORIZATION);

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if(!jwtTokenProvider.validateJwtToken(token)) {
                return ResponseEntity.status(401).body("Invalid Token");
            }
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
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
