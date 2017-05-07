package com.spring.model.flowtable;

import org.json.JSONObject;

public class Flow implements Comparable<Flow>{
	int id;
	int priority;
	JSONObject flow;
	
	public Flow(JSONObject flow){
		try{
			this.priority = Integer.parseInt(flow.get("priority").toString());
		}catch(Exception e){
			this.priority=-1;
		}
		this.flow=flow;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public JSONObject getFlow() {
		return flow;
	}

	public void setFlow(JSONObject flow) {
		this.flow = flow;
	}

	public int compareTo(Flow flow) {
			return flow.priority-this.priority;
	}

}
