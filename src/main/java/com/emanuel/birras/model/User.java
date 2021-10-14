package com.emanuel.birras.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "users_db",
	indexes = {@Index(name = "idx_user_db",  columnList="userBeer", unique = true), @Index(name = "idx_user_db_email",  columnList="email", unique = true)})
public class User {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_user")
	private Long id;
	
	@Column(length=30)
	private String userBeer;
	
	@Column(length=254)
	private String email;
	
	@Column(length=100)
	private String name;
	
	@Column(length=20)
	private String password; //TODO: preferable to be in another table, and must to be encrypted
	
	@Column
	private Boolean isAdmin;

	/*
	@OneToMany(mappedBy="users")
	@JoinColumn(name = "id_user")
	@JsonBackReference
	private List<MeetupUsers> meetupUsers;
	*/
	public User() {
		
	}
	
	public User(String userBeer, String email, String name, String password) {
		this.userBeer = userBeer;
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserBeer() {
		return userBeer;
	}
	public void UserBeer(String userBeer) {
		this.userBeer = userBeer;
	}
	public void setUserBeer(String userBeer) {
		this.userBeer = userBeer;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	/*
	public List<MeetupUsers> getMeetupUsers() {
		return meetupUsers;
	}
	public void setMeetupUsers(List<MeetupUsers> meetupUsers) {
		this.meetupUsers = meetupUsers;
	}
	*/
}
