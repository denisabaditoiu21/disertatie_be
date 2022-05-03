package com.project.literarycreation.service.impl;

import com.project.literarycreation.domain.*;
import com.project.literarycreation.dto.request.AddLiteraryCreation;
import com.project.literarycreation.dto.response.LiteraryCreationRecomendationResponse;
import com.project.literarycreation.dto.response.LiteraryCreationResponse;
import com.project.literarycreation.dto.response.MyLiteraryCreationResponse;
import com.project.literarycreation.repository.*;
import com.project.literarycreation.service.LiteraryCreationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LiteraryCreationServiceImpl implements LiteraryCreationService {
	@Autowired
	private GenreRepository genreRepository;
	@Autowired
	private StatusRepository statusRepository;
	@Autowired
	private FeelingRepository feelingRepository;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LiteraryCreationNoteRepository literaryCreationNoteRepository;
	@Autowired
	private LiteraryCreationRepository literaryCreationRepository;
	@Autowired
	private RecommendationRepository recommendationRepository;

	@Override
	public List<Genre> getAvailableGenres() {
		return genreRepository.findAll();
	}

	@Override
	public void addLiteraryCreation(AddLiteraryCreation literaryCreation) {
		LiteraryCreation creation = new LiteraryCreation();
		creation.setCover(literaryCreation.getCover());
		creation.setName(literaryCreation.getName());
		creation.setPdf(literaryCreation.getPdf());
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		this.userRepository.findByUsername(principal.getName()).ifPresent(creation::setAuthor);
		creation.setGenre(this.genreRepository.findFirstById(literaryCreation.getGenre()));
		this.statusRepository.findById(1).ifPresent(creation::setStatus);
		this.literaryCreationRepository.save(creation);

	}

	@Override
	public List<Status> getAvailableStatuses() {
		return statusRepository.findAll();
	}

	@Override
	public List<MyLiteraryCreationResponse> getMyCreations(Integer statusId) {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userRepository.findByUsername(principal.getName()).orElse(null);
		List<LiteraryCreation> creations = new ArrayList<>();
		if (currentUser != null) {
			if (statusId != null) {
				creations = this.literaryCreationRepository.findAllByAuthorIdAndStatusId(currentUser.getId(), statusId);
			} else {
				creations = this.literaryCreationRepository.findAllByAuthorId(currentUser.getId());
			}
			return creations.stream().map(l -> {
				MyLiteraryCreationResponse res = this.modelMapper.map(l, MyLiteraryCreationResponse.class);
				res.setCover(Base64.getEncoder().encodeToString(l.getCover()));
				res.setPdf(Base64.getEncoder().encodeToString(l.getPdf()));
				return res;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public List<LiteraryCreationResponse> getAllCreations(List<Integer> statuses) {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userRepository.findByUsername(principal.getName()).orElse(null);
		return this.literaryCreationRepository.findAllByStatusIdIn(statuses).stream().map(l -> {
			LiteraryCreationResponse res = this.modelMapper.map(l, LiteraryCreationResponse.class);
			res.setPdf(Base64.getEncoder().encodeToString(l.getPdf()));
			res.setCanAddNote(!this.literaryCreationNoteRepository.existsByLiteraryCreationAndUser(l, currentUser));
			res.setCover(Base64.getEncoder().encodeToString(l.getCover()));
			if (l.getAuthor().getProfilePic() != null) {
				res.getAuthor().setProfilePic(Base64.getEncoder().encodeToString(l.getAuthor().getProfilePic()));
			}

			return res;
		}).collect(Collectors.toList());
	}

	@Override
	public List<Feeling> getAvailableFeelings() {
		return this.feelingRepository.findAll();
	}

	@Override
	public LiteraryCreation updateStatus(Integer creationId, Integer statusId) {
		LiteraryCreation creation = this.literaryCreationRepository.findById(creationId).orElse(null);
		if (creation != null) {
			Status status = this.statusRepository.findById(statusId).orElse(null);
			if (status != null) {
				creation.setStatus(status);
				return this.literaryCreationRepository.save(creation);
			}
		}
		return null;

	}

	@Override
	public void recommendCreation(Integer creationId, List<Long> usersIds) {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userRepository.findByUsername(principal.getName()).orElse(null);

		if (currentUser != null) {
			LiteraryCreation creation = this.literaryCreationRepository.findById(creationId).orElse(null);
			if (creation != null) {
				for (Long userId : usersIds) {
					Recommendation recommendation = new Recommendation();
					recommendation.setCreation(creation);
					recommendation.setReceiverId(userId);
					recommendation.setSenderId(currentUser.getId());
					recommendation.setAccepted(0);
					this.recommendationRepository.save(recommendation);
				}
			}

		}
	}

	@Override
	public List<LiteraryCreationRecomendationResponse> getMyRecommendations() {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userRepository.findByUsername(principal.getName()).orElse(null);
		if (currentUser != null) {
			List<Recommendation> recommendations = this.recommendationRepository
					.findAllByReceiverIdAndAcceptedNot(currentUser.getId(), 2);
			return recommendations.stream().map(l -> {
				LiteraryCreationRecomendationResponse res = this.modelMapper.map(l.getCreation(),
						LiteraryCreationRecomendationResponse.class);
				res.setAccepted(l.getAccepted());
				res.setRecommendationId(l.getId());
				res.setCover(Base64.getEncoder().encodeToString(l.getCreation().getCover()));
				res.setPdf(Base64.getEncoder().encodeToString(l.getCreation().getPdf()));
				res.setCanAddNote(!this.literaryCreationNoteRepository.existsByLiteraryCreationAndUser(l.getCreation(),
						currentUser));
				if (l.getCreation().getAuthor().getProfilePic() != null) {
					res.getAuthor().setProfilePic(
							Base64.getEncoder().encodeToString(l.getCreation().getAuthor().getProfilePic()));
				}
				res.setRecommendedBy(this.userRepository.findById(l.getSenderId()).get().getUsername());
				return res;
			}).collect(Collectors.toList());
		}
		return Collections.emptyList();

	}

	@Override
	public void addNote(Integer creationId, Integer note) {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = this.userRepository.findByUsername(principal.getName()).orElse(null);
		if (currentUser != null) {
			LiteraryCreation creation = this.literaryCreationRepository.findById(creationId).orElse(null);
			if (creation != null) {
				LiteraryCreationNote creationNote = new LiteraryCreationNote();
				creationNote.setUser(currentUser);
				creationNote.setLiteraryCreation(creation);
				creationNote.setNote(note);
				this.literaryCreationNoteRepository.save(creationNote);
			}
		}
	}

	@Override
	public List<LiteraryCreationRecomendationResponse> updateRecommendation(Integer recommendationId,
			Integer accepted) {
		Recommendation rec = this.recommendationRepository.findById(recommendationId).orElse(null);
		if (rec != null) {
			rec.setAccepted(accepted);
			this.recommendationRepository.save(rec);
		}

		return this.getMyRecommendations();

	}

}
