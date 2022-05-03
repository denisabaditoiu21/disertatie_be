package com.project.literarycreation.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "authors_groups")
public class Group {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Integer id;

	@Column(nullable = false)
	private String name;

	@Column
	private String description;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	private User admin;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "users_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
	private Set<User> users = new HashSet<>();
}
