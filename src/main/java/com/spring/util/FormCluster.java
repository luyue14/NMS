package com.spring.util;

import java.util.*;

import com.spring.model.cluster.*;
import com.spring.model.topo.*;

//生成初始化cluster配置，一个dp对应一个cluster
public class FormCluster {
	public static List<ClusterInfo> formCluster(Topo tp){
		List<ClusterInfo> clusterInfo = new ArrayList<ClusterInfo>();
		//List<Link> vlink = new ArrayList<Link>();
		List<Node> nodes = new ArrayList<Node>();
	    List<Link> links = new ArrayList<Link>();
	    //List<String> nodeId = new ArrayList<String>();
		//List<String> tn = new ArrayList<String>();
		//List<String> ip =new ArrayList<String>();
	    nodes = tp.getNode();
		links = tp.getLink();
	    Node tempNode = new Node();

	    int clusterId=0;
		//int linkNum = links.size();
		int nodeNum = nodes.size();
		//不需要link信息
		for(int i = 0;i<nodeNum;i++){
			tempNode = nodes.get(i);
			if((tempNode.getNodeId().length()!=0)&&(tempNode.getNodeId().charAt(0)!='h')){
				ClusterInfo aClusterInfo = new ClusterInfo();
				aClusterInfo.setClusterId(String.valueOf(clusterId));
				clusterId++;
				String dp = tempNode.getNodeId();
				aClusterInfo.getDpList().add(dp);
				EdgePort aEdgePort = new EdgePort();
				aEdgePort.setDpId(tempNode.getNodeId());
				List<String> dpPort = new ArrayList<String>();
				List<String> macAddress = new ArrayList<String>();
				List<EdgePort> edgePort = new ArrayList<EdgePort>();
				for(int j=0;j<tempNode.getTerminationPoint().size();j++){
					TerminationPoint terminationPoint = new TerminationPoint();
					terminationPoint = tempNode.getTerminationPoint().get(j);
					String tpId = terminationPoint.getTpId();
					String aDpPort=tpId.substring(tpId.indexOf(":")+1,tpId.length());
					aDpPort=aDpPort.substring(aDpPort.indexOf(":")+1,aDpPort.length());
					if(aDpPort.charAt(0)!='L'){
						dpPort.add(aDpPort);
						macAddress.add(getMacAddress());
					}
				}
				aEdgePort.setDpPort(dpPort);
				aEdgePort.setMacAddress(macAddress);
				edgePort.add(aEdgePort);
				aClusterInfo.setEdgePort(edgePort);
				clusterInfo.add(aClusterInfo);

			}
		}
		Cluster cluster = new Cluster();
		cluster.setClusterInfo(clusterInfo);
		
		System.out.println("***************cluster***************");
		System.out.println(cluster);
		
		return clusterInfo;
		
	}
	
	
	
	private static String getMacAddress(){
		int temp;
		char tempChar = 0;
		String mac =new String();
		for(int n=0;n<6;n++){
			mac = mac+".";
			for(int m =0;m<2;m++){
				temp = (int)(1+Math.random()*14);
				if(temp>9){
					tempChar = (char) ('A'+temp-10);
					mac  = mac +tempChar ;
				}else{
					mac  = mac +String.valueOf(temp) ;
				}
				
			}
		}
		mac = mac.substring(1);
		return mac;
	}
}
