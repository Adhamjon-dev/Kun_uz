package dasturlash.uz.controller;

import dasturlash.uz.dto.VerificationBySmsDTO;
import dasturlash.uz.dto.profile.RegistrationDTO;
import dasturlash.uz.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<String> registration(@Valid @RequestBody RegistrationDTO dto) {
        return ResponseEntity.ok(authService.registration(dto));
    }

    @PutMapping("/registration/sms/verification")
    public ResponseEntity<String> verificationBySms(@RequestBody VerificationBySmsDTO dto) {
        return ResponseEntity.ok(authService.verificationBySms(dto));
    }

}
