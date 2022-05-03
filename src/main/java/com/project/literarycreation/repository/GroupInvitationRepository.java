package com.project.literarycreation.repository;

import com.project.literarycreation.domain.GroupInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupInvitationRepository extends JpaRepository<GroupInvitation, Integer> {

    List<GroupInvitation> findAllByReceiverIdAndAcceptedIsNull(Long id);
}
