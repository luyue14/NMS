package com.spring.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.flowtable.Flow;
import com.spring.model.flowtable.Flowtable;
import com.spring.util.FlowtableGetter;

@EnableMongoRepositories("com.spring.model")
@Controller
public class ReturnFlowtable{
	@RequestMapping(value="/Physical-Topo/flowtable.do",method=RequestMethod.POST)
    public @ResponseBody String handler(@RequestBody String nodeId){
		nodeId=nodeId.substring(1,nodeId.length()-1);
		FlowtableGetter flowtableGetter = new FlowtableGetter(nodeId);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		JSONObject flowtable = flowtableGetter.getFlowtable();
		System.out.println(flowtable);

		String flowtableString = flowtable.toString();
		//flowtableString.replaceAll("flow-node-inventory:table"," table"); 
		JSONArray nodes = flowtable.getJSONArray("node");
		JSONObject tables = nodes.getJSONObject(0);
		JSONArray table = tables.getJSONArray("flow-node-inventory:table");
		int length = table.length();
		List<Flowtable> forSort = new ArrayList<Flowtable>();
		for(int i=0;i<length;i++){
			Flowtable temp = new Flowtable(table.getJSONObject(i));
			try{
				temp.getTable().get("flow");
		System.out.println("exist");
				forSort.add(temp);
			}catch(Exception e){
				System.out.println("not exist");

			}
		}
		
		Collections.sort(forSort);
		
		JSONArray result = new JSONArray();
		for(int i=0;i<forSort.size();i++){
			JSONObject temp = forSort.get(i).getTable();
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////
			JSONArray array = temp.getJSONArray("flow");
			List<Flow> forFlowSort = new ArrayList<Flow>();
			int arrayLength = array.length();
			System.out.println("arraylength="+arrayLength);
			for(int j=0;j<arrayLength;j++){
				Flow tempFlow = new Flow(array.getJSONObject(j));
				forFlowSort.add(tempFlow);
				System.out.println("flow");
			}
			Collections.sort(forFlowSort);
			JSONArray flowResult = new JSONArray();

			for(int j=0;j<forFlowSort.size();j++){
				JSONObject tempFlow = forFlowSort.get(j).getFlow();
				flowResult.put(tempFlow);
			}
			System.out.println("flowresult");
System.out.println(flowResult);
			
			//////////////////////////////////////////////////////////////////////////////////////////////////////////////

			JSONObject element = new JSONObject();
			element.put("id",temp.get("id"));
			element.put("flow",flowResult);
			result.put(element);
		}
		JSONObject results =new JSONObject();
		results.put("table", result);
		System.out.println(results.toString());
		///////
		return results.toString();
	}
}