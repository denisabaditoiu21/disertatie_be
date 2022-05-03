package com.project.literarycreation.service.impl;

import com.project.literarycreation.Utils.RoleName;
import com.project.literarycreation.domain.Role;
import com.project.literarycreation.domain.User;
import com.project.literarycreation.dto.request.BaseUser;
import com.project.literarycreation.repository.RoleRepository;
import com.project.literarycreation.repository.UserRepository;
import com.project.literarycreation.service.AuthenticationService;
import com.project.literarycreation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserService userService;

    @Override
    public User registerUser(BaseUser registerRequest) {
        // Creating user's account, encode password
        User user = new User(registerRequest.getName(), registerRequest.getUsername(), registerRequest.getEmail(),
                encoder.encode(registerRequest.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByName(RoleName.ROLE_USER).orElse(null));
        user.setRoles(roles);
        User savedUser = userRepository.save(user);
        this.userService.sendRegistrationEmail(savedUser.getEmail());
        return savedUser;
    }

    @Override
    public void forgotPassword(String email) {
        String generatedPassword = this.getAlphaNumericString(10);
        User user = this.userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            user.setPassword(encoder.encode(generatedPassword));
            this.userRepository.save(user);
            this.sendForgotPasswordEmail(generatedPassword, email);
        }

    }

    private String getAlphaNumericString(int n) {
        // chose a Character random from this String
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "0123456789"
                + "abcdefghijklmnopqrstuvxyz";

        // create StringBuffer size of alphaNumericString
        StringBuilder sb = new StringBuilder(n);

        for (int i = 0; i < n; i++) {
            // add Character one by one in end of sb
            sb.append(alphaNumericString.charAt(new Random().nextInt(alphaNumericString.length())));
        }

        return sb.toString();
    }

    private void sendForgotPasswordEmail(String password, String email) {
        String subject = "Resetare parola aplicatie Creatii literare";

        String message = "Ati resetat parola. Noua parola:";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\r\n" + password + "\r\n");
        mailSender.send(mailMessage);
    }

}
