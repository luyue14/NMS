package com.spring.model.flowtable;

import org.json.JSONObject;

public class Flowtable implements Comparable<Flowtable>{
	int id;
	int priority;
	JSONObject table;
	
	public Flowtable(JSONObject table){
		this.id = Integer.parseInt(table.get("id").toString());
		try{
			this.priority = Integer.parseInt(table.getJSONObject("flow").get("priority").toString());
		}catch(Exception e){
			this.priority=-1;
		}
		this.table=table;
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

	public JSONObject getTable() {
		return table;
	}

	public void setTable(JSONObject table) {
		this.table = table;
	}

	public int compareTo(Flowtable flowtable) {
			return this.id-flowtable.id;
	}
}
