package com.spring.model.topo;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


public interface TopoRepository extends MongoRepository<Topo, String> {

	public List<Topo> findByLink_Destination_destNode(String destNode);

	public List<Topo> findByLink_Source_sourceNode(String sourceNode);

	public List<Topo> findAll();

	public Topo save(Topo topo);

	public List<Topo> findByNode_nodeId(String nodeId);

}
