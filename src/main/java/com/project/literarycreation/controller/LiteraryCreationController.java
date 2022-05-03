package com.project.literarycreation.controller;

import com.project.literarycreation.domain.Feeling;
import com.project.literarycreation.domain.Genre;
import com.project.literarycreation.domain.LiteraryCreation;
import com.project.literarycreation.domain.Status;
import com.project.literarycreation.dto.request.AddFeeling;
import com.project.literarycreation.dto.request.AddLiteraryCreation;
import com.project.literarycreation.dto.response.LiteraryCreationRecomendationResponse;
import com.project.literarycreation.dto.response.LiteraryCreationResponse;
import com.project.literarycreation.dto.response.MyLiteraryCreationResponse;
import com.project.literarycreation.dto.response.ParagraphResponse;
import com.project.literarycreation.dto.response.ResponseMessage;
import com.project.literarycreation.service.LiteraryCreationService;
import com.project.literarycreation.service.ParagraphService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/ecommerce-art-app")
public class LiteraryCreationController {

	@Autowired
	private LiteraryCreationService literaryCreationService;

	@Autowired
	private ParagraphService paragraphService;

	@GetMapping("/genres")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<Genre>> getAllGenres() {
		return new ResponseEntity<>(literaryCreationService.getAvailableGenres(), HttpStatus.OK);
	}

	@PostMapping("/add")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResponseMessage> addCreation(@RequestBody AddLiteraryCreation literaryCreation) {
		this.literaryCreationService.addLiteraryCreation(literaryCreation);
		return new ResponseEntity<>(new ResponseMessage("Lucrare adaugata cu success"), HttpStatus.OK);
	}

	@GetMapping("/statuses")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<List<Status>> getAllStatuses() {
		return new ResponseEntity<>(literaryCreationService.getAvailableStatuses(), HttpStatus.OK);
	}

	@GetMapping("/feelings")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<Feeling>> getAllFeelings() {
		return new ResponseEntity<>(literaryCreationService.getAvailableFeelings(), HttpStatus.OK);
	}

	@GetMapping("/list")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> getMyCreations(@RequestParam(required = false) Integer statusId) {
		List<MyLiteraryCreationResponse> creations = this.literaryCreationService.getMyCreations(statusId);
		return new ResponseEntity<>(creations, HttpStatus.OK);
	}

	@GetMapping("/list-all")
	@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
	public ResponseEntity<?> getAllCreations(@RequestParam(required = false) List<Integer> statuses) {
		if (statuses == null || statuses.isEmpty()) {
			statuses = new ArrayList<>();
			statuses.add(3);
		}
		List<LiteraryCreationResponse> creations = this.literaryCreationService.getAllCreations(statuses);
		return new ResponseEntity<>(creations, HttpStatus.OK);
	}

	@PostMapping("/associate-feeling")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResponseMessage> associateFeeling(@RequestBody AddFeeling addFeelingRequest) {
		this.paragraphService.addParagraph(addFeelingRequest);
		return new ResponseEntity<>(new ResponseMessage("Sentiment asociat!"), HttpStatus.OK);
	}

	@GetMapping("/change-status")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> changeCreationStatus(Integer creationId, Integer statusId) {
		LiteraryCreation creation = this.literaryCreationService.updateStatus(creationId, statusId);
		if (creation != null) {
			return new ResponseEntity<>(new ResponseMessage("Status actualizat!"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseMessage("Eroare la setarea statusului!"), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/list-by-feeling")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<ParagraphResponse>> getAllByFeeling(@RequestParam(required = false) Integer feelingId) {
		return new ResponseEntity<>(this.paragraphService.getAllByFeeling(feelingId), HttpStatus.OK);

	}

	@GetMapping("/recommend")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> recommendCreation(@RequestParam Integer creationId, @RequestParam List<Long> usersIds) {
		this.literaryCreationService.recommendCreation(creationId, usersIds);
		return new ResponseEntity<>(new ResponseMessage("Recomandari trimise!"), HttpStatus.OK);
	}

	@GetMapping("/my-recommendations")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<LiteraryCreationRecomendationResponse>> getMyRecommendations() {
		List<LiteraryCreationRecomendationResponse> creations = this.literaryCreationService.getMyRecommendations();
		return new ResponseEntity<>(creations, HttpStatus.OK);
	}

	@GetMapping("/add-note")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<?> addNote(@RequestParam Integer creationId, @RequestParam Integer note) {
		this.literaryCreationService.addNote(creationId, note);
		return new ResponseEntity<>(new ResponseMessage("Nota adagata!"), HttpStatus.OK);
	}

	@GetMapping("/update-recommendation")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<LiteraryCreationRecomendationResponse>> updateRecommendation(
			@RequestParam Integer recommendationId, @RequestParam Integer accepted) {
		List<LiteraryCreationRecomendationResponse> creations = this.literaryCreationService
				.updateRecommendation(recommendationId, accepted);
		return new ResponseEntity<>(creations, HttpStatus.OK);
	}

}
