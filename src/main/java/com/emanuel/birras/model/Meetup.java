package com.emanuel.birras.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "meetups")
public class Meetup {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_meetup")
	private Long id;
	
	@Column(name="name")
	private String name;

	@Column(name="date_from")
	private Timestamp dateFrom;
	
	private Integer hours;
	
	@Column(name="address")
	private String address;
	
	@Column(name="date_to")
	private Timestamp dateTo;
	
	/*
	@OneToMany(mappedBy="meetups")
	@JoinColumn(name = "id_meetup")
	@JsonBackReference
	private List<MeetupUsers> meetupUsers;
	*/
	public Meetup() {
		
	}
	
	public Meetup(Timestamp dateFrom, Integer hours) {
		this.dateFrom = dateFrom;
		this.hours = hours;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(Timestamp dateFrom) {
		this.dateFrom = dateFrom;
	}
	public Integer getHours() {
		return hours;
	}
	public void setHours(Integer hours) {
		this.hours = hours;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Timestamp getDateTo() {
		return dateTo;
	}
	public void setDateTo(Timestamp dateTo) {
		this.dateTo = dateTo;
	}
	/*
	public List<MeetupUsers> getMeetupUsers() {
		return meetupUsers;
	}
	public void setMeetupUsers(List<MeetupUsers> meetupUsers) {
		this.meetupUsers = meetupUsers;
	}*/
}
