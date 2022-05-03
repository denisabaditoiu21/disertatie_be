package com.project.literarycreation.dto.response;

import com.project.literarycreation.domain.Genre;
import com.project.literarycreation.domain.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MyLiteraryCreationResponse {
    private Integer id;
    private String name;
    private String cover;
    private String pdf;
    private Genre genre;
    private Status status;

}
