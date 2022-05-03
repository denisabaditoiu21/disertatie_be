package com.project.literarycreation.service.impl;

import com.project.literarycreation.domain.Paragraph;
import com.project.literarycreation.dto.request.AddFeeling;
import com.project.literarycreation.dto.response.ParagraphResponse;
import com.project.literarycreation.repository.FeelingRepository;
import com.project.literarycreation.repository.LiteraryCreationRepository;
import com.project.literarycreation.repository.ParagraphRepository;
import com.project.literarycreation.service.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
public class ParagraphServiceImpl implements ParagraphService {

    @Autowired
    private ParagraphRepository paragraphRepository;

    @Autowired
    private LiteraryCreationRepository literaryCreationRepository;

    @Autowired
    private FeelingRepository feelingRepository;

    @Override
    public Paragraph addParagraph(AddFeeling addFeeling) {
        Paragraph paragraph = new Paragraph();
        paragraph.setText(addFeeling.getText());
        this.feelingRepository.findById(addFeeling.getFeelingId()).ifPresent(paragraph::setFeeling);
        this.literaryCreationRepository.findById(addFeeling.getCreationId()).ifPresent(paragraph::setCreation);
        return this.paragraphRepository.save(paragraph);
    }

    @Override
    public List<ParagraphResponse> getAllByFeeling(Integer feelingId) {
        List<ParagraphResponse> response = new ArrayList<>();
        List<Paragraph> paragraphs;
        if (feelingId != null) {
            paragraphs = paragraphRepository.findAllByFeelingId(feelingId);
        } else {
            paragraphs = paragraphRepository.findAll();
        }
        paragraphs.forEach(p -> {
                    ParagraphResponse res = new ParagraphResponse();
                    res.setText(p.getText());
                    res.setCreationCover(Base64.getEncoder().encodeToString(p.getCreation().getCover()));
                    res.setCreationName(p.getCreation().getName());
                    response.add(res);
                }
        );
        return response;
    }
}
