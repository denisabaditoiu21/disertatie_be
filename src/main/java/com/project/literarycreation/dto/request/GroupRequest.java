package com.project.literarycreation.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupRequest {
    private String name;
    private String description;
    private List<Long> usersIds;
}
