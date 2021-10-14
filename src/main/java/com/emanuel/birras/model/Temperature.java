package com.emanuel.birras.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "temperature")
public class Temperature {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_temperature")
	private Long id;

	@Column(name="date_temperature")
	private Timestamp date;
	
	@Column(name="temperature")
	private Double temperature;
	
	@Column(name="date_modified")
	private Timestamp dateModified;

	public Temperature() {
		
	}
	
	public Temperature(Timestamp date, Double temperature) {
		this.date = date;
		this.temperature = temperature;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}
	public Double getTemperature() {
		return temperature;
	}
	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	public Timestamp getDateModified() {
		return dateModified;
	}
	public void setDateModified(Timestamp dateModified) {
		this.dateModified = dateModified;
	}
}
