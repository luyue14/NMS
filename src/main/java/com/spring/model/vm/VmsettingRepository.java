package com.spring.model.vm;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;


public interface VmsettingRepository extends MongoRepository<Vmsetting, String> {
	public List<Vmsetting> findAll();
	public List<Vmsetting> findByTopoHashcode(int hashCode);
	public Vmsetting save(Vmsetting s);
}