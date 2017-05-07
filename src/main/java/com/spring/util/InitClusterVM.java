package com.spring.util;

import java.util.ArrayList;
import java.util.List;

import com.spring.model.clusterVM.EdgePort;
import com.spring.model.clusterVM.ClusterInfo;
import com.spring.model.clusterVM.ClusterVM;
import com.spring.model.topo.Destination;
import com.spring.model.topo.Link;
import com.spring.model.topo.Node;
import com.spring.model.topo.Source;
import com.spring.model.topo.TerminationPoint;
import com.spring.model.topo.Topo;

public class InitClusterVM {
	public static ClusterVM initClusterVM(Topo tp){
		ClusterVM clusterVM= new ClusterVM();
		List<ClusterInfo> clusterInfo = new ArrayList<ClusterInfo>();
		/*************************************寻找不重复的link*************************************/	    
	
		List<Link> vlink = new ArrayList<Link>();
		List<Node> nodes = new ArrayList<Node>();
	    List<Link> links = new ArrayList<Link>();
	    List<String> nodeId = new ArrayList<String>();//用来记录所有的node
		List<String> tn = new ArrayList<String>();
		//List<String> ip =new ArrayList<String>();
	
	    nodes = tp.getNode();
		links = tp.getLink();
		int linkNum = links.size();
		int nodeNum = nodes.size();
	    Node tempNode = new Node();
	    Destination des = new Destination();
	    Source source = new Source();
	    String desNode = new String();
	    String desTp = new String();
	    String sourceNode = new String();
	    String sourceTp = new String();
	    
	    for(int i = 0;i<nodeNum;i++){
			tempNode = nodes.get(i);
			if(tempNode.getNodeId().charAt(0)!='h'){//判断不是host后进入
				nodeId.add(tempNode.getNodeId());
				List<String> en = new ArrayList<String>();//exist node	
			    tn.add(tempNode.getNodeId());
				//遍历link找出不重复的link
				for(int j=0; j < linkNum ; j++){
					Link tempLink =  new Link();
					//tempLink = links.get(j);
					des = links.get(j).getDestination();
					source = links.get(j).getSource();
					
					desNode = des.getDestNode();
					desTp = des.getDestTp();
					sourceNode = source.getSourceNode();
					sourceTp = source.getSourceTp();
					Boolean exist = false;
					//当destinationNode或者sourceNode等于当前node时 判断此条link是否被加入到vlink中，没有的话加入
					//新的link中 destinationnode为当前的遍历的节点
					if(desNode.equals(tempNode.getNodeId())){
						Destination tempDes = new Destination();
						Source tempSource = new Source();
						tempDes.setDestNode(desNode);
						tempDes.setDestTp(desTp);
						tempSource.setSourceNode(sourceNode);
						tempSource.setSourceTp(sourceTp);
						tempLink.setDestination(tempDes);
						tempLink.setSource(tempSource);
						for(int n=0;n<en.size();n++){
							if((en.get(n)).equals(sourceNode)){
								exist = true;
								break;
							}
						}
						for(int n=0;n<tn.size();n++){
							if((tn.get(n)).equals(sourceNode)){
								exist = true;
								break;
							}
						}
	
						if(!exist){
							//System.out.println(tempLink);
							vlink.add(tempLink);
						}
						en.add(sourceNode);
	
					}else if(sourceNode.equals(tempNode.getNodeId())){
						Destination tempDes = new Destination();
						Source tempSource = new Source();
						tempDes.setDestNode(sourceNode);
						tempDes.setDestTp(sourceTp);
						tempSource.setSourceNode(desNode);
						tempSource.setSourceTp(desTp);
						tempLink.setDestination(tempDes);
						tempLink.setSource(tempSource);
						for(int n=0;n<en.size();n++){
							if((en.get(n)).equals(desNode)){
								exist = true;
								break;
							}
						}
						for(int n=0;n<tn.size();n++){
							if((tn.get(n)).equals(desNode)){
								exist = true;
								break;
							}
						}
						if(!exist){
							vlink.add(tempLink);
						}
						en.add(desNode);
					}
				}
			}
	    }
	    
	    
		System.out.println(vlink.size());
		//输出找到的link
	    for(int i=0;i<vlink.size();i++){
	    	Link a = vlink.get(i);
	    	//System.out.println(vlink.get(i));
	
	    }
	    /****************************************************************************************/
	    int clusterId=0;
	    //int nodeNum = nodes.size(); 上面定义过
	    for(int i = 0;i<nodeNum;i++){
	    	tempNode = nodes.get(i);
	    	if(tempNode.getNodeId().charAt(0)!='h'){
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
	    
	    clusterVM.setClusterInfo(clusterInfo);
	    //System.out.println(clusterVM);
	    List<String> ip = getIpInfo();
	    int last16 = 0;
	    for(int i=0;i<clusterInfo.size();i++){
	    	for(int j=0;j<vlink.size();j++){
	    		int last8 =1;
	    		String ipPre = ip.get(0)+"."+String.valueOf(last16);
    	    	String dpId = clusterInfo.get(i).getEdgePort().get(0).getDpId();
    	    	desNode = vlink.get(j).getDestination().getDestNode();
    	    	if(dpId.equals(desNode)){
    	    		last16++;
    	    		//开始加入对应ip和network 上午做完！！！！！！！！！！！
    	    		//System.out.println("dpId equal desNode!!!!!!!!!!");//进入没问题
    	    		//先找到link的port
    	    		desTp = vlink.get(j).getDestination().getDestTp();
    	    		sourceNode = vlink.get(j).getSource().getSourceNode();
    	    		sourceTp = vlink.get(j).getSource().getSourceTp();
    	    		//System.out.println(desTp);
    	    		int desPort = Integer.parseInt(desTp.substring(desTp.length()-1, desTp.length()));
    	    		int sourcePort = -1;
    	    		if(sourceTp.charAt(0)!='h'){
    	    			sourcePort = Integer.parseInt(sourceTp.substring(sourceTp.length()-1, sourceTp.length()));
    	    		}
    	    		//System.out.println("desPort==========="+desPort);
    	    		//System.out.println("sourcePort================="+sourcePort);
    	    		
    	    		//为当前的cluster加上ip和network
	    			String ip1 = ipPre+"."+String.valueOf(last8)+"/24";
	    			last8++;
	    			String network = ipPre+".0/24";
	    			String ip2 = ipPre+"."+String.valueOf(last8)+"/24";
    				List<String> ipAddress = new ArrayList<String>();
    				List<String> net = new ArrayList<String>();
	    			for(int n=0;n<clusterInfo.get(i).getEdgePort().get(0).getDpPort().size();n++){
	    				//找到对应的端口号
	    				if(desPort==Integer.parseInt(clusterInfo.get(i).getEdgePort().get(0).getDpPort().get(n))){
	    					ipAddress.add(ip1);
	    					net.add(network);
	    				}else{
	    					if(clusterInfo.get(i).getEdgePort().get(0).getIpAddress().size()<n+1){
	    						ipAddress.add("0.0.0.0/0");
	    						net.add("0.0.0.0/0");
	    					}else{
	    						ipAddress.add(clusterInfo.get(i).getEdgePort().get(0).getIpAddress().get(n));
	    						net.add(clusterInfo.get(i).getEdgePort().get(0).getNetwork().get(n));
	    					}
	    				}
	    			}
    	    		clusterInfo.get(i).getEdgePort().get(0).setIpAddress(ipAddress);
    	    		clusterInfo.get(i).getEdgePort().get(0).setNetwork(net);
    	    		//System.out.println(ipAddress);
    	    		//System.out.println(net);
    	    		//ipAddress.clear();
    	    		//net.clear();
    	    		//找到link另一端的cluster
    				List<String> ipAddress2 = new ArrayList<String>();
    				List<String> net2 = new ArrayList<String>();
    	    		if(sourceNode.charAt(0)!='h'){
    	    			//System.out.println("right one");
	    	    		for(int n=0;n<clusterInfo.size();n++){
	    	    			if(sourceNode.equals(clusterInfo.get(n).getEdgePort().get(0).getDpId())){
	    		    			for(int m=0;m<clusterInfo.get(n).getEdgePort().get(0).getDpPort().size();m++){
	    		    				//找到对应的端口号
	    		    				if(sourcePort==Integer.parseInt(clusterInfo.get(n).getEdgePort().get(0).getDpPort().get(m))){
	    		    					ipAddress2.add(ip2);
	    		    					net2.add(network);
	    	    	    				//System.out.println("success!!!!!!!!");

	    		    				}else{
	    		    					if(clusterInfo.get(n).getEdgePort().get(0).getIpAddress().size()<m+1){
	    		    						ipAddress2.add("0.0.0.0/0");
	    		    						net2.add("0.0.0.0/0");
	    		    					}else{
	    		    						ipAddress2.add(clusterInfo.get(n).getEdgePort().get(0).getIpAddress().get(m));
	    		    						net2.add(clusterInfo.get(n).getEdgePort().get(0).getNetwork().get(m));
	    		    					}
	    		    				}
	    		    				
	    		    			}//for
	    		    			//System.out.println("ipAddress"+ipAddress);
	    		    			//System.out.println("net"+net);
	    		   	    		clusterInfo.get(n).getEdgePort().get(0).setIpAddress(ipAddress2);
	    	    	    		clusterInfo.get(n).getEdgePort().get(0).setNetwork(net2);
	    	    			}//if
	    	    		}
	    	    		
    	    		}
    	    	}
    	    	
	    		
	    		
	    	}
	    }
	    
System.out.println(clusterVM);
	    
	    return clusterVM;
	}
	
	private static List<String> getIpInfo(){
		List<String> info = new ArrayList<String>();
		String ip=new String();
		int maskLength = 16;
		//int restLength = 32-maskLength;
		
		for(int n = 0;n<maskLength/8;n++){
			ip  = ip + "." + String.valueOf((int)(1+Math.random()*253));
		}
		
		ip=ip.substring(1);
		System.out.println(ip);
		
		info.add("10.0");
		info.add(String.valueOf(maskLength));
		return  info;
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
