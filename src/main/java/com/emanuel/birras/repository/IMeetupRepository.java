package com.emanuel.birras.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emanuel.birras.model.Meetup;

public interface IMeetupRepository extends JpaRepository<Meetup, Long> {
	
}
