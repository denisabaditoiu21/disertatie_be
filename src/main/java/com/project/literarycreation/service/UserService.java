package com.project.literarycreation.service;

import com.project.literarycreation.dto.request.ProfileUser;
import com.project.literarycreation.dto.response.LiteraryCreationAuthor;
import com.project.literarycreation.dto.response.MyPerformanceResponse;

import java.util.List;

public interface UserService {

    void sendRegistrationEmail(String email);

    ProfileUser getUserDetails(String username);

    List<LiteraryCreationAuthor> listAll();

    List<MyPerformanceResponse> getMyPerformance();

    void updateUser(ProfileUser user);
}
