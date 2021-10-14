package com.emanuel.birras.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.emanuel.birras.model.Meetup;
import com.emanuel.birras.model.MeetupUsers;
import com.emanuel.birras.model.User;

public interface IMeetupUsersRepository extends JpaRepository<MeetupUsers, Long> {	
	List<MeetupUsers> findByMeetup(Meetup meetup);
	
	List<MeetupUsers> findByUser(User user);
	
	List<MeetupUsers> findByMeetupAndUser(Meetup meetup, User user);
	
	List<MeetupUsers> findByUser(User user, Sort sort);
	
	List<MeetupUsers> findByMeetup(Meetup meetup, Sort sort);
	
	//List<MeetupUsers> findByUserByOrderByUserName(User user);
	
	
}
