package com.spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.model.cluster.Cluster;
import com.spring.model.cluster.ClusterInfo;
import com.spring.model.cluster.ClusterRepository;
import com.spring.model.cluster.EdgePort;
import com.spring.model.clusterVM.ClusterVM;
import com.spring.model.message.SuccessMessage;
import com.spring.model.portconfig.PortConfig;
import com.spring.model.portconfig.PortConfigs;
import com.spring.model.topo.Topo;
import com.spring.model.topo.TopoRepository;
import com.spring.model.vm.PortList;
import com.spring.model.vm.VmInfo;
import com.spring.model.vm.Vmsetting;
import com.spring.model.vm.VmsettingRepository;
import com.spring.util.GetPortConfig;

@Controller
public class DivideClusterVm {
	
	@Autowired
	private TopoRepository topoRepository; 
	@Autowired
	private ClusterRepository clusterRepository; 
	@Autowired
	private VmsettingRepository vmsettingRepository; 
	
	
	@RequestMapping(value="/OneKeyStart/saveClusterVM.do",method=RequestMethod.POST)
	public @ResponseBody SuccessMessage handler(@RequestBody ClusterVM cv){
		System.out.println("cv??"+cv);
		SuccessMessage sm = new SuccessMessage();
		Cluster aCluster = new Cluster();
		List<ClusterInfo> cis = new ArrayList<ClusterInfo>();
		Vmsetting  aVm = new Vmsetting();
		List<VmInfo> vis = new ArrayList<VmInfo>();
		String vmNamePre = "rfvm";
		String hostIpNamePre = "192.169.1.";
		List<Topo> tps=topoRepository.findAll();
		Topo tp = new Topo();
		tp=tps.get(0);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//get portconfig
		PortConfigs portConfigs = new PortConfigs();
		List<PortConfig> portConfig = portConfigs.getPortConfigs();
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////

		//////////////////////////////////////////////////////////////////////////////
		GetPortConfig portConfigs2 = new GetPortConfig("portConfig");
		portConfigs2.load(portConfig);
		//Form Cluster
		aCluster.setName(cv.getName());
		aCluster.setTimeStamp(cv.getTimeStamp());
		aCluster.setHashCode(tp.hashCode());

		for(int i = 0;i<cv.getClusterInfo().size();i++){
			ClusterInfo aci = new ClusterInfo();
			List<EdgePort> eps = new ArrayList<EdgePort>();
			aci.setClusterId(cv.getClusterInfo().get(i).getClusterId());
			aci.setDpList(cv.getClusterInfo().get(i).getDpList());
			for(int j=0;j<cv.getClusterInfo().get(i).getEdgePort().size();j++){
				EdgePort ep = new EdgePort();
				ep.setDpId(cv.getClusterInfo().get(i).getEdgePort().get(j).getDpId());
				ep.setDpPort(cv.getClusterInfo().get(i).getEdgePort().get(j).getDpPort());
				ep.setMacAddress(cv.getClusterInfo().get(i).getEdgePort().get(j).getMacAddress());
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////
				for(int o=0;o<portConfig.size();o++){
					if(portConfig.get(o).getDpId().equals(cv.getClusterInfo().get(i).getEdgePort().get(j).getDpId())){
						ep.getDpPort().add(portConfig.get(o).getPort());
						ep.getMacAddress().add(portConfig.get(o).getMacAddress());
					}
				}
				///////////////////////////////////////////////////////////////////////////////////////////////////////////////
				
				eps.add(ep);
			}
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			
			String prename=null;

			for(int p=0;p<cv.getClusterInfo().get(i).getDpList().size();p++){
				//List<String> names = new ArrayList<String>();
				//name.add(cv.getClusterInfo().get(i).getDpList().get(p));
				String name = cv.getClusterInfo().get(i).getDpList().get(p);
				Boolean jud = false;

				for(int t = 0;t<eps.size();t++){
						if(eps.get(t).getDpId().equals(name)){	
							System.out.println(name);
							jud = true;
							break;
						}
					}
				
				if(!jud){
					for(int o=0;o<portConfig.size();o++){
						if(portConfig.get(o).getDpId().equals(name)){
							if((prename!=null)&&(prename.equals(name))){
								eps.get(eps.size()-1).getDpPort().add(portConfig.get(o).getPort());
								eps.get(eps.size()-1).getMacAddress().add(portConfig.get(o).getMacAddress());
								
							}else{
							
								EdgePort ep = new EdgePort();
								ep.setDpId(portConfig.get(o).getDpId());
								List<String> dpPorts = new ArrayList<String>();
								dpPorts.add(portConfig.get(o).getPort());
								ep.setDpPort(dpPorts);
								List<String> macs = new ArrayList<String>();
								macs.add(portConfig.get(o).getMacAddress());
								ep.setMacAddress(macs);
								System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
								prename = name;
								eps.add(ep);
							}

						}
					}
				}
				
			}

			
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////

			aci.setEdgePort(eps);
			cis.add(aci);
		}
		aCluster.setClusterInfo(cis);
		
		System.out.println(aCluster);
		
		
		//Form Vm
		aVm.setTimeStamp(cv.getTimeStamp());
		aVm.setTopoHashcode(tp.hashCode());
		for(int i = 0;i<cv.getClusterInfo().size();i++){
			List<PortList> pls = new ArrayList<PortList>();
			VmInfo avi = new VmInfo();
			char temp = (char)(i+'A');////////////////////////////////////////////change!
			avi.setName(vmNamePre+String.valueOf(temp));
			avi.setHostIp(hostIpNamePre+String.valueOf(i+100));
			avi.setVmId(cv.getClusterInfo().get(i).getClusterId());
			//avi.setPortSize(String.valueOf(cv.getClusterInfo().get(i).getEdgePort().size()));
			
			avi.setHostMacAddress(getMacAddress());
			int n = 1;
			
			//for(int j=0;j<cv.getClusterInfo().get(i).getEdgePort().size();j++){
			for(int j=0;j<aCluster.getClusterInfo().get(i).getEdgePort().size();j++){
				for(int p=0;p<aCluster.getClusterInfo().get(i).getEdgePort().get(j).getDpPort().size();p++){
				//for(int p=0;p<cv.getClusterInfo().get(i).getEdgePort().get(j).getIpAddress().size();p++){
					PortList pl = new PortList();
					pl.setName(vmNamePre+String.valueOf(temp)+"."+String.valueOf(n));
					n++;
					if((j+1>cv.getClusterInfo().get(i).getEdgePort().size())||(p+1>cv.getClusterInfo().get(i).getEdgePort().get(j).getIpAddress().size())){
						for(int y=0; y<portConfig.size();y++){
							if(aCluster.getClusterInfo().get(i).getEdgePort().get(j).getMacAddress().get(p).equals(portConfig.get(y).getMacAddress())){
								pl.setIpAddress(portConfig.get(y).getIpAddress());
								pl.setMacAddress(portConfig.get(y).getMacAddress());
								pl.setNetwork(portConfig.get(y).getNetwork());
								pls.add(pl);
								
								break;
							}
							
						}
						
					}else{
						pl.setIpAddress(cv.getClusterInfo().get(i).getEdgePort().get(j).getIpAddress().get(p));
						pl.setMacAddress(cv.getClusterInfo().get(i).getEdgePort().get(j).getMacAddress().get(p));
						pl.setNetwork(cv.getClusterInfo().get(i).getEdgePort().get(j).getNetwork().get(p));
						pls.add(pl);
					}
				}

			}
			
			/*for(int j=0;j<cv.getClusterInfo().get(i).getEdgePort().size();j++){
				for(int p=0;p<cv.getClusterInfo().get(i).getEdgePort().get(j).getDpPort().size();p++){
				//for(int p=0;p<cv.getClusterInfo().get(i).getEdgePort().get(j).getIpAddress().size();p++){
					PortList pl = new PortList();
					pl.setName(vmNamePre+String.valueOf(temp)+"."+String.valueOf(n));
					n++;
					if(p+1>cv.getClusterInfo().get(i).getEdgePort().get(j).getIpAddress().size()){
						for(int y=0; y<portConfig.size();y++){
							if(cv.getClusterInfo().get(i).getEdgePort().get(j).getMacAddress().get(p).equals(portConfig.get(y).getMacAddress())){
								pl.setIpAddress(portConfig.get(y).getIpAddress());
								pl.setMacAddress(portConfig.get(y).getMacAddress());
								pl.setNetwork(portConfig.get(y).getNetwork());
								pls.add(pl);
								
								break;
							}
							
						}
						
					}else{
						pl.setIpAddress(cv.getClusterInfo().get(i).getEdgePort().get(j).getIpAddress().get(p));
						pl.setMacAddress(cv.getClusterInfo().get(i).getEdgePort().get(j).getMacAddress().get(p));
						pl.setNetwork(cv.getClusterInfo().get(i).getEdgePort().get(j).getNetwork().get(p));
						pls.add(pl);
					}
				}

			}
			*/
			
			
			

			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			/*for(int j=0;j<cv.getClusterInfo().get(i).getDpList().size();j++){
				for(int o=0; o<portConfig.size();o++){
					if(portConfig.get(o).getDpId().equals(cv.getClusterInfo().get(i).getDpList().get(j))){
						PortList pl = new PortList();
						pl.setName(vmNamePre+String.valueOf(temp)+"."+String.valueOf(n));
						n++;
						pl.setIpAddress(portConfig.get(o).getIpAddress());
						pl.setMacAddress(portConfig.get(o).getMacAddress());
						pl.setNetwork(portConfig.get(o).getNetwork());
						pls.add(pl);
					}
				}
			}*/
			///////////////////////////////////////////////////////////////////////////////////////////////////////////////
			avi.setPortSize(String.valueOf(n-1));
			avi.setPortList(pls);
			vis.add(avi);
		}
		aVm.setVmInfo(vis);
		
		System.out.println(aVm);

		vmsettingRepository.deleteAll();
		clusterRepository.deleteAll();
		aCluster.setSelected(true);
		aVm.setSelected(true);
		clusterRepository.save(aCluster);
		vmsettingRepository.save(aVm);
		
		sm.setIsSuccess("success");
		return sm;
	}
	
	private static String getMacAddress(){
		int temp = 0;
		char tempChar = 0;
		String mac =new String();
		for(int n=0;n<6;n++){
			mac = mac+":";
			for(int m =0;m<2;m++){
				if(n==0&&m==1){
					temp = (int)(1+Math.random()*7);
					temp=temp*2;
				}else{
					temp = (int)(1+Math.random()*14);
				}
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