package com.project.literarycreation.service;

import com.project.literarycreation.domain.Group;
import com.project.literarycreation.domain.GroupMessage;
import com.project.literarycreation.dto.request.GroupRequest;
import com.project.literarycreation.dto.response.GroupMessageResponse;
import com.project.literarycreation.dto.response.GroupResponse;
import com.project.literarycreation.dto.response.InviteResponse;

import java.util.List;

public interface GroupService {

	Group create(GroupRequest groupRequest);

	void respondToInvitation(Integer response, Integer inviteId);

	List<InviteResponse> getMyInvites();

	List<GroupResponse> getMyGroups();

	List<GroupMessageResponse> getGroupMessages(Integer groupId);

	List<GroupMessageResponse> addGroupMessage(String message, Integer groupId);
}
