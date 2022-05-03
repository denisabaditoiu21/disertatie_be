package com.project.literarycreation.service;

import com.project.literarycreation.domain.*;
import com.project.literarycreation.dto.request.AddLiteraryCreation;
import com.project.literarycreation.dto.response.LiteraryCreationRecomendationResponse;
import com.project.literarycreation.dto.response.LiteraryCreationResponse;
import com.project.literarycreation.dto.response.MyLiteraryCreationResponse;

import java.util.List;

public interface LiteraryCreationService {

    List<Genre> getAvailableGenres();

    List<Feeling> getAvailableFeelings();

    List<Status> getAvailableStatuses();

    List<MyLiteraryCreationResponse> getMyCreations(Integer statusId);

    List<LiteraryCreationResponse> getAllCreations(List<Integer> statuses);

    LiteraryCreation updateStatus(Integer creationId, Integer statusId);

    void addLiteraryCreation(AddLiteraryCreation literaryCreation);

    void recommendCreation(Integer creationId, List<Long> usersIds);
    
    void addNote(Integer creationId, Integer note);

    List<LiteraryCreationRecomendationResponse> getMyRecommendations();
    
    List<LiteraryCreationRecomendationResponse> updateRecommendation(Integer recommendationId, Integer accepted);
}
