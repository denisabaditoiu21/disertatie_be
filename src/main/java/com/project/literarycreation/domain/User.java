package com.project.literarycreation.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Getter
@Setter
@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "email" }) })
public class User {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String username;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Lob
	@Column(columnDefinition = "LONGBLOB")
	private byte[] profilePic;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

	@ManyToMany(mappedBy = "users")
	Set<Group> groups;

	public User() {
	}

	public User(String name, String username, String email, String password) {
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
	}
}
