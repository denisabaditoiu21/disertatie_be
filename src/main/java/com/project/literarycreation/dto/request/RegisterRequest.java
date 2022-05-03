package com.project.literarycreation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest extends BaseUser {
    private String token;
}
