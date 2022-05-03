package com.project.literarycreation.service;

import com.project.literarycreation.domain.Paragraph;
import com.project.literarycreation.dto.request.AddFeeling;
import com.project.literarycreation.dto.response.ParagraphResponse;

import java.util.List;

public interface ParagraphService {

    Paragraph addParagraph(AddFeeling addFeeling);

    List<ParagraphResponse> getAllByFeeling(Integer feelingId);

}
