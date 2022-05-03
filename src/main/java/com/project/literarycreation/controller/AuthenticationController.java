package com.project.literarycreation.controller;

import com.project.literarycreation.domain.RegisterToken;
import com.project.literarycreation.domain.User;
import com.project.literarycreation.dto.request.BaseUser;
import com.project.literarycreation.dto.request.LoginRequest;
import com.project.literarycreation.dto.response.JwtResponse;
import com.project.literarycreation.dto.response.ResponseMessage;
import com.project.literarycreation.repository.RegisterTokenRepository;
import com.project.literarycreation.repository.UserRepository;
import com.project.literarycreation.security.jwt.JwtProvider;
import com.project.literarycreation.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthenticationController {

    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegisterTokenRepository registerTokenRepository;


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        User user = this.userRepository.findByUsername(loginRequest.getUsername()).orElse(null);
        if (user != null) {
            RegisterToken token = this.registerTokenRepository.findByEmail(user.getEmail()).orElse(null);
            if (token != null && !token.getConfirmed()) {
                return new ResponseEntity<>(new ResponseMessage("Contul nu a fost confirmat! Verificati adresa de e-mail."),
                        HttpStatus.BAD_REQUEST);
            }
        }


        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getUsername(), userDetails.getAuthorities()));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody BaseUser registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(new ResponseMessage("Numele utilizator exista!"),
                    HttpStatus.BAD_REQUEST);
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new ResponseMessage("E-mailul este deja inregistrat!"),
                    HttpStatus.BAD_REQUEST);
        }

        this.authenticationService.registerUser(registerRequest);

        return new ResponseEntity<>(new ResponseMessage("Utilizator inregistrat cu success!"), HttpStatus.OK);
    }


    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {

        if (!userRepository.existsByEmail(email)) {
            return new ResponseEntity<>(new ResponseMessage("Nu exista un cont asociat acestui e-mail!"),
                    HttpStatus.BAD_REQUEST);
        }
        this.authenticationService.forgotPassword(email);

        return new ResponseEntity<>(new ResponseMessage("Parola temporara a fost trimisa pe e-mail!"), HttpStatus.OK);
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(@RequestParam String token) {

        RegisterToken registrationToken = this.registerTokenRepository.findByToken(token).orElse(null);
        if (registrationToken != null && registrationToken.getExpiryDate().after(new Date())) {
            if (!registrationToken.getConfirmed()) {
                registrationToken.setConfirmed(true);
                this.registerTokenRepository.save(registrationToken);
                return new ResponseEntity<>(new ResponseMessage("Contul a fost confirmat!"), HttpStatus.OK);

            } else {
                return new ResponseEntity<>(new ResponseMessage("Contul a fost deja confirmat!"),
                        HttpStatus.BAD_REQUEST);
            }

        } else {
            return new ResponseEntity<>(new ResponseMessage("Token invalid!"),
                    HttpStatus.BAD_REQUEST);
        }


    }

}
