package com.project.literarycreation.controller;

import com.project.literarycreation.domain.Group;
import com.project.literarycreation.dto.request.GroupMessageRequest;
import com.project.literarycreation.dto.request.GroupRequest;
import com.project.literarycreation.dto.response.GroupMessageResponse;
import com.project.literarycreation.dto.response.GroupResponse;
import com.project.literarycreation.dto.response.InviteResponse;
import com.project.literarycreation.dto.response.MyPerformanceResponse;
import com.project.literarycreation.dto.response.ResponseMessage;
import com.project.literarycreation.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api//ecommerce-art-app/group")
public class GroupController {

	@Autowired
	private GroupService groupService;

	@PostMapping("/create")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResponseMessage> addCreation(@RequestBody GroupRequest groupRequest) {
		Group group = this.groupService.create(groupRequest);
		if (group != null) {
			return new ResponseEntity<>(new ResponseMessage("Grup creat!"), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new ResponseMessage("Eroare la crearea grupului!"), HttpStatus.BAD_REQUEST);
		}

	}

	@GetMapping("/respond")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<ResponseMessage> respondInvite(@RequestParam Integer response,
			@RequestParam Integer inviteId) {
		this.groupService.respondToInvitation(response, inviteId);
		return new ResponseEntity<>(new ResponseMessage("Raspuns trimis!"), HttpStatus.OK);

	}

	@GetMapping("/my-invites")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<InviteResponse>> getMyInvites() {
		return new ResponseEntity<>(this.groupService.getMyInvites(), HttpStatus.OK);

	}

	@GetMapping("/my-groups")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<GroupResponse>> getMyGroups() {
		return new ResponseEntity<>(this.groupService.getMyGroups(), HttpStatus.OK);
	}

	@GetMapping("/group-messages")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<GroupMessageResponse>> getGroupMessages(@RequestParam Integer groupId) {
		return new ResponseEntity<>(this.groupService.getGroupMessages(groupId), HttpStatus.OK);
	}
	
	@PostMapping("/add-message")
	@PreAuthorize("hasRole('USER')")
	public ResponseEntity<List<GroupMessageResponse>> addGroupMessage(@RequestBody GroupMessageRequest groupMessageRequest) {
		return new ResponseEntity<>(this.groupService.addGroupMessage(groupMessageRequest.getMessage(), groupMessageRequest.getGroupId()), HttpStatus.OK);
	}
}
