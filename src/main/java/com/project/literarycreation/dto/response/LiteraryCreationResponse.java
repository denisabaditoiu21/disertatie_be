package com.project.literarycreation.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LiteraryCreationResponse extends MyLiteraryCreationResponse {
	private Boolean canAddNote;
    private LiteraryCreationAuthor author;
}
