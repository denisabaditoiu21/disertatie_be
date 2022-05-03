package com.project.literarycreation.service;

import com.project.literarycreation.domain.User;
import com.project.literarycreation.dto.request.BaseUser;

public interface AuthenticationService {

    User registerUser(BaseUser user);

    void forgotPassword(String email);
}
