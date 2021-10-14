package com.emanuel.birras.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emanuel.birras.model.Temperature;

public interface ITemperatureRepository extends JpaRepository<Temperature, Long> {
	List<Temperature> findByDate(Timestamp date);
}
