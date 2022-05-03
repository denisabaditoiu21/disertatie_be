package com.project.literarycreation.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddLiteraryCreation {

    private String name;
    private byte[] cover;
    private byte[] pdf;
    private int genre;
}
