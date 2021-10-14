package com.emanuel.birras.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emanuel.birras.model.Meetup;
import com.emanuel.birras.model.MeetupUsers;
import com.emanuel.birras.model.User;
import com.emanuel.birras.repository.IMeetupUsersRepository;

@Service
public class MeetupUsersServiceImpl implements IMeetupUsersRepository {

	@Autowired
	private IMeetupUsersRepository repo;

	@Override
	public List<MeetupUsers> findAll() {
		return repo.findAll();
	}
	
	@Override
	public List<MeetupUsers> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MeetupUsers> findAllById(Iterable<Long> ids) {
		return repo.findAllById(ids);
	}

	@Override
	public <S extends MeetupUsers> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends MeetupUsers> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends MeetupUsers> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<MeetupUsers> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllByIdInBatch(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllInBatch() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MeetupUsers getOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MeetupUsers getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends MeetupUsers> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends MeetupUsers> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<MeetupUsers> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends MeetupUsers> S save(S entity) {
		return repo.save(entity);
	}

	@Override
	public Optional<MeetupUsers> findById(Long id) {
		return repo.findById(id);
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		repo.deleteById(id);
	}

	@Override
	public void delete(MeetupUsers entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends MeetupUsers> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends MeetupUsers> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends MeetupUsers> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends MeetupUsers> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends MeetupUsers> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public List<MeetupUsers> findByMeetupAndUser(Meetup meetup, User user) {
		return repo.findByMeetupAndUser(meetup, user);
	}

	@Override
	public List<MeetupUsers> findByMeetup(Meetup meetup) {
		return repo.findByMeetup(meetup);
	}

	@Override
	public List<MeetupUsers> findByUser(User user) {
		return repo.findByUser(user);
	}
	
	@Override
	public List<MeetupUsers> findByUser(User user, Sort sort) {
		return repo.findByUser(user, sort);
	}
	
	@Override
	public List<MeetupUsers> findByMeetup(Meetup meetup, Sort sort) {
		return repo.findByMeetup(meetup, sort);
	}
	
	/*@Override
	public List<MeetupUsers> findByUserByOrderByUserName(User user) {
		return repo.findByUserByOrderByUserName(user);
	}*/
}
