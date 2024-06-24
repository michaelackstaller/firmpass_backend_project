package de.acksmi.firmapp.firmpass_backend_project.controller;

import de.acksmi.firmapp.firmpass_backend_project.model.User;
import de.acksmi.firmapp.firmpass_backend_project.security.JwtTokenProvider;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        return ResponseEntity.ok(userService.saveUser(user));
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
