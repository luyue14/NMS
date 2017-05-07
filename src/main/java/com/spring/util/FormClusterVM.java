package com.spring.util;

import java.util.*;

import com.spring.model.cluster.Cluster;
import com.spring.model.clusterVM.ClusterInfo;
import com.spring.model.clusterVM.ClusterVM;
import com.spring.model.clusterVM.EdgePort;
import com.spring.model.vm.Vmsetting;

public class FormClusterVM {
	public static ClusterVM FormClusterVM(Cluster cluster,Vmsetting vm){
		ClusterVM cv = new ClusterVM();
		List<ClusterInfo> cis = new ArrayList<ClusterInfo>();//clusterVM里的clusterInfo
		cv.setName(cluster.getName());
		for(int i=0;i<cluster.getClusterInfo().size();i++){
			ClusterInfo aci = new ClusterInfo();
			aci.setClusterId(cluster.getClusterInfo().get(i).getClusterId());
			aci.setDpList(cluster.getClusterInfo().get(i).getDpList());
			int portNum = 0;
			for(int j=0;j<cluster.getClusterInfo().get(i).getEdgePort().size();j++){
				EdgePort ep = new EdgePort();
				ep.setDpId(cluster.getClusterInfo().get(i).getEdgePort().get(j).getDpId());
				ep.setDpPort(cluster.getClusterInfo().get(i).getEdgePort().get(j).getDpPort());

				//ep.setDpPort(dpPort);
				for(int p=0;p<cluster.getClusterInfo().get(i).getEdgePort().get(j).getDpPort().size();p++){
					ep.getIpAddress().add(vm.getVmInfo().get(i).getPortList().get(portNum).getIpAddress());
					ep.getMacAddress().add(vm.getVmInfo().get(i).getPortList().get(portNum).getMacAddress());
					ep.getNetwork().add(vm.getVmInfo().get(i).getPortList().get(portNum).getNetwork());
					portNum++;
				}
				aci.getEdgePort().add(ep);
			}
			cv.getClusterInfo().add(aci);
		}
		
		
		return cv;
	}
}
