package com.project.literarycreation.service.impl;

import com.project.literarycreation.domain.LiteraryCreation;
import com.project.literarycreation.domain.RegisterToken;
import com.project.literarycreation.domain.Role;
import com.project.literarycreation.domain.User;
import com.project.literarycreation.dto.request.ProfileUser;
import com.project.literarycreation.dto.response.LiteraryCreationAuthor;
import com.project.literarycreation.dto.response.MyPerformanceResponse;
import com.project.literarycreation.repository.LiteraryCreationNoteRepository;
import com.project.literarycreation.repository.LiteraryCreationRepository;
import com.project.literarycreation.repository.RegisterTokenRepository;
import com.project.literarycreation.repository.UserRepository;
import com.project.literarycreation.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private static final int EXPIRATION = 60 * 24;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private JavaMailSender mailSender;
    @Value("${front-end.base_url}")
    private String frontEndBaseUrl;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LiteraryCreationRepository literaryCreationRepository;
    
    @Autowired
    private LiteraryCreationNoteRepository literaryCreationNoteRepository;

    @Autowired
    private RegisterTokenRepository registerTokenRepository;

    @Override
    public void sendRegistrationEmail(String email) {
        RegisterToken registerToken = new RegisterToken();
        registerToken.setEmail(email);
        registerToken.setToken(UUID.randomUUID().toString());
        registerToken.setExpiryDate(this.calculateExpiryDate());
        registerToken.setConfirmed(false);

        this.registerTokenRepository.save(registerToken);

        String subject = "Confirmare inregistrare aplicatie Creatii literare";
        String confirmationUrl
                = this.frontEndBaseUrl + "/login?token=" + registerToken.getToken();
        String message = "Confirmati inregistrarea folosind link-ul de mai jos:";
        String warning = "Link-ul este valabil pentru 24 de ore.";

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(message + "\r\n\n" + confirmationUrl + "\r\n\n" + warning);
        this.mailSender.send(mailMessage);

    }

    @Override
    public ProfileUser getUserDetails(String username) {
        Optional<User> user = this.userRepository.findByUsername(username);
        return user.map(value -> this.modelMapper.map(value, ProfileUser.class)).orElse(null);
    }

    @Override
    public void updateUser(ProfileUser profileUer) {

        User user = this.userRepository.findByUsername(profileUer.getUsername()).orElse(null);
        if (user != null) {
            if (profileUer.getPassword() != null && !profileUer.getPassword().isEmpty()) {
                user.setPassword(this.encoder.encode(profileUer.getPassword()));
            }
            user.setEmail(profileUer.getEmail());
            user.setProfilePic(profileUer.getProfilePic());
            this.userRepository.save(this.modelMapper.map(user, User.class));
        }

    }

    @Override
    public List<LiteraryCreationAuthor> listAll() {
        List<LiteraryCreationAuthor> result = new ArrayList<>();
        Principal principal = SecurityContextHolder.getContext().getAuthentication();
        Role userRole = new Role();
        userRole.setId(2);
        this.userRepository.findAllByRolesIdIn(Collections.singletonList(1)).forEach(u -> {
            if (!u.getUsername().equals(principal.getName())) {
                LiteraryCreationAuthor author = this.modelMapper.map(u, LiteraryCreationAuthor.class);
                if (u.getProfilePic() != null) {
                    author.setProfilePic(Base64.getEncoder().encodeToString(u.getProfilePic()));
                }
                result.add(author);
            }

        });
        return result;
    }

    @Override
    public List<MyPerformanceResponse> getMyPerformance() {
    	List<MyPerformanceResponse> result = new ArrayList<>();
    	  Principal principal = SecurityContextHolder.getContext().getAuthentication();
          User currentUser = this.userRepository.findByUsername(principal.getName()).orElse(null);
          if(currentUser != null) {
        	  List<LiteraryCreation> creations = this.literaryCreationRepository.findAllByAuthorId(currentUser.getId());
        	  creations.forEach(l->{
        		  MyPerformanceResponse performanceRes = new MyPerformanceResponse();
        		  performanceRes.setCreationId(l.getId());
        		  performanceRes.setName(l.getName());
        		  performanceRes.setAvg(this.literaryCreationNoteRepository.getAvg(l.getId()));
        		  result.add(performanceRes);
        	  });
          }
        return result;
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE, EXPIRATION);
        return new Date(cal.getTime().getTime());
    }
}
