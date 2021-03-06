package com.spring.model.topoConfig;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TopoCoordinateRepository extends MongoRepository<TopoCoordinate, String> {
	
	public List<TopoCoordinate> findAll();

	public TopoCoordinate save(TopoCoordinate topo);
	public List<TopoCoordinate> findByConfigId(int configId);
	public List<TopoCoordinate> findByTimeStamp(long timeStamp);
	public void deleteByConfigId(int configId);
	public void deleteByTimeStamp(long timeStamp);
	public void deleteAll();
	



}
