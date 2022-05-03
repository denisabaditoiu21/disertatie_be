package com.project.literarycreation.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "group_messages")
public class GroupMessage {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "sender_id")
    private User sender;

	@Column(nullable = false)
	private Integer groupId;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private Date creationDate;

}
