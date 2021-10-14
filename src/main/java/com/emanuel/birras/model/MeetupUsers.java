package com.emanuel.birras.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "meetup_users")
public class MeetupUsers {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="check_in")
	private Boolean checkIn;
	
	@ManyToOne
	@JoinColumn(name = "id_meetup")
	@JsonManagedReference
	private Meetup meetup; 
	
	@ManyToOne
	@JoinColumn(name = "id_user")
	@JsonManagedReference
	private User user;
	
	public MeetupUsers() {
		
	}
	
	public MeetupUsers(Boolean checkIn, Meetup meetup, User user) {
		super();
		this.checkIn = checkIn;
		this.meetup = meetup;
		this.user = user;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(Boolean checkIn) {
		this.checkIn = checkIn;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Meetup getMeetup() {
		return meetup;
	}
	public void setMeetup(Meetup meetup) {
		this.meetup = meetup;
	}
	
}
