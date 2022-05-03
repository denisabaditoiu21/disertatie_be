package com.project.literarycreation.controller;

import com.project.literarycreation.dto.request.ProfileUser;
import com.project.literarycreation.dto.response.LiteraryCreationAuthor;
import com.project.literarycreation.dto.response.MyPerformanceResponse;
import com.project.literarycreation.dto.response.ResponseMessage;
import com.project.literarycreation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/ecommerce-art-app/user")
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> updateUser(@RequestBody ProfileUser profileUser) {
        this.userService.updateUser(profileUser);
        return new ResponseEntity<>(new ResponseMessage("Informatii actualizate!"), HttpStatus.OK);
    }

    @GetMapping("/details")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<?> getUserDetails(@RequestParam String username) {
        ProfileUser user = this.userService.getUserDetails(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new ResponseMessage("Detaliile utilizatoruli nu sunt disponibile"), HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/list-all")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LiteraryCreationAuthor>> listAllUsers() {
        List<LiteraryCreationAuthor> authors = this.userService.listAll();
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @GetMapping("/my-performance")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<MyPerformanceResponse>> getMyPerformance() {
        return new ResponseEntity<>(this.userService.getMyPerformance(), HttpStatus.OK);
    }
    
}
