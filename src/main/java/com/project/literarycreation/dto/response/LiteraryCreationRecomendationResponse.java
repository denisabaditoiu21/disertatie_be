package com.project.literarycreation.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LiteraryCreationRecomendationResponse extends LiteraryCreationResponse {
	private Integer accepted;
	private Integer recommendationId;
	 private String recommendedBy;
}
