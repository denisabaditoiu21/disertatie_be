package com.project.literarycreation.service.impl;

import com.project.literarycreation.domain.Group;
import com.project.literarycreation.domain.GroupInvitation;
import com.project.literarycreation.domain.GroupMessage;
import com.project.literarycreation.domain.User;
import com.project.literarycreation.dto.request.GroupRequest;
import com.project.literarycreation.dto.response.GroupMessageResponse;
import com.project.literarycreation.dto.response.GroupResponse;
import com.project.literarycreation.dto.response.InviteResponse;
import com.project.literarycreation.repository.GroupInvitationRepository;
import com.project.literarycreation.repository.GroupMessageRepository;
import com.project.literarycreation.repository.GroupRepository;
import com.project.literarycreation.repository.UserRepository;
import com.project.literarycreation.service.GroupService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

	@Autowired
	private GroupRepository groupRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GroupInvitationRepository groupInvitationRepository;

	@Autowired
	private GroupMessageRepository groupMessageRepository;

	@Override
	public Group create(GroupRequest groupRequest) {
		Group group = modelMapper.map(groupRequest, Group.class);
		group.setUsers(new HashSet<>());

		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User admin = this.userRepository.findByUsername(principal.getName()).orElse(null);
		if (admin != null) {
			group.setAdmin(admin);
		}
//        if (groupRequest.getUsersIds() != null) {
//            groupRequest.getUsersIds().forEach(id -> {
//                User user = new User();
//                user.setId(id);
//                group.getUsers().add(user);
//            });
//        }

		Group savedGroup = groupRepository.save(group);
		// trimite invitatii in grup
		if (groupRequest.getUsersIds() != null) {
			groupRequest.getUsersIds().forEach(id -> {
				GroupInvitation invitation = new GroupInvitation();
				invitation.setSenderId(admin.getId());
				invitation.setReceiverId(id);
				invitation.setGroup(savedGroup);
				this.groupInvitationRepository.save(invitation);
			});
		}
		return savedGroup;
	}

	@Override
	public void respondToInvitation(Integer response, Integer inviteId) {
		GroupInvitation invitation = groupInvitationRepository.findById(inviteId).orElse(null);
		if (invitation != null) {
			User user = new User();
			user.setId(invitation.getReceiverId());
			if (response == 1) {
				invitation.getGroup().getUsers().add(user);
			}
			invitation.setAccepted(response == 1);
			this.groupInvitationRepository.save(invitation);
		}

	}

	@Override
	public List<InviteResponse> getMyInvites() {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.findByUsername(principal.getName()).orElse(null);
		List<InviteResponse> result = new ArrayList<>();
		if (user != null) {
			this.groupInvitationRepository.findAllByReceiverIdAndAcceptedIsNull(user.getId()).forEach(i -> {
				InviteResponse res = new InviteResponse();
				res.setId(i.getId());
				res.setGroupName(i.getGroup().getName());
				User sender = this.userRepository.findById(i.getSenderId()).orElse(null);
				res.setSender(sender != null ? sender.getUsername() : null);
				result.add(res);
			});
		}
		return result;
	}

	@Override
	public List<GroupResponse> getMyGroups() {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.findByUsername(principal.getName()).orElse(null);
		List<GroupResponse> results = new ArrayList<>();
		if (user != null) {
			user.getGroups().forEach(g -> {
				GroupResponse response = this.modelMapper.map(g, GroupResponse.class);
				response.setAdmin(g.getAdmin().getUsername());
				results.add(response);

			});

			this.groupRepository.findByAdmin(user).forEach(g -> {
				GroupResponse response = this.modelMapper.map(g, GroupResponse.class);
				response.setAdmin(g.getAdmin().getUsername());
				results.add(response);

			});

		}
		return results;
	}

	@Override
	public List<GroupMessageResponse> getGroupMessages(Integer groupId) {
		List<GroupMessageResponse> results = new ArrayList<>();
		this.groupMessageRepository.findByGroupIdOrderByCreationDateAsc(groupId).forEach(m -> {
			GroupMessageResponse res = new GroupMessageResponse();
			res.setSenderPic(Base64.getEncoder().encodeToString(m.getSender().getProfilePic()));
			res.setSenderName(m.getSender().getUsername());
			res.setMessage(m.getMessage());
			res.setCreationDate(m.getCreationDate().toString());
			results.add(res);
		});

		return results;
	}

	@Override
	public List<GroupMessageResponse> addGroupMessage(String message, Integer groupId) {
		Principal principal = SecurityContextHolder.getContext().getAuthentication();
		User user = this.userRepository.findByUsername(principal.getName()).orElse(null);
		GroupMessage groupMessage = new GroupMessage();
		groupMessage.setCreationDate(new Date());
		groupMessage.setGroupId(groupId);
		groupMessage.setMessage(message);
		groupMessage.setSender(user);

		this.groupMessageRepository.save(groupMessage);

		return this.getGroupMessages(groupId);
	}
}
