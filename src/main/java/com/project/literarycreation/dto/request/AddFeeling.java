package com.project.literarycreation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddFeeling {

    private String text;
    private Integer feelingId;
    private Integer creationId;

}
