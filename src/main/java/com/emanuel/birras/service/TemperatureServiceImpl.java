package com.emanuel.birras.service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.emanuel.birras.model.Temperature;
import com.emanuel.birras.repository.ITemperatureRepository;

@Service
public class TemperatureServiceImpl implements ITemperatureRepository {

	@Autowired
	private ITemperatureRepository repo;

	@Override
	public List<Temperature> findAll() {
		return repo.findAll();
	}

	@Override
	public List<Temperature> findAll(Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Temperature> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Temperature> List<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void flush() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Temperature> S saveAndFlush(S entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Temperature> List<S> saveAllAndFlush(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteAllInBatch(Iterable<Temperature> entities) {
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
	public Temperature getOne(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Temperature getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Temperature> List<S> findAll(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Temperature> List<S> findAll(Example<S> example, Sort sort) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Page<Temperature> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean saveTemperature(Timestamp date, Timestamp dateModified) {
		List<Temperature> t = repo.findByDate(date);
		if (t.size()>0) {
			LocalDate DateModifiedinDb = t.get(0).getDateModified().toLocalDateTime().toLocalDate();
			long q= ChronoUnit.DAYS.between(DateModifiedinDb,dateModified.toLocalDateTime().toLocalDate());
			return q!=0;
		}
		return true;
	}
	
	@Override
	public <S extends Temperature> S save(S entity) {
		boolean save = true;
		
		save = saveTemperature(entity.getDate(), entity.getDateModified());
		
		if (save) {
			List<Temperature> t = repo.findByDate(entity.getDate());
			if (t.size()>0) {
				entity.setId(t.get(0).getId());
			}
			return repo.save(entity);
		}
		else {
			return entity;
		}
	}

	@Override
	public Optional<Temperature> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Temperature entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Temperature> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public <S extends Temperature> Optional<S> findOne(Example<S> example) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Temperature> Page<S> findAll(Example<S> example, Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <S extends Temperature> long count(Example<S> example) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public <S extends Temperature> boolean exists(Example<S> example) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Temperature> findByDate(Timestamp date) {
		LocalDate aux = date.toLocalDateTime().toLocalDate();
		return repo.findByDate(Timestamp.valueOf(aux.atStartOfDay()));
	}


	
}
