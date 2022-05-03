package com.project.literarycreation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileUser extends BaseUser {
    private byte[] profilePic;
}
