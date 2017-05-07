package com.spring.util;

import java.util.*;

import com.spring.model.cluster.*;
import com.spring.model.topo.Topo;
import com.spring.model.vm.*;

public class FormVm2 {
	public static List<Vmsetting> formVmsetting(Cluster cluster, Topo tp){
		List<Vmsetting> vmsetting = new ArrayList<Vmsetting>();
		/*List<ClusterInfo> clusterInfo = cluster.getClusterInfo();
		
	    String namePre = "rfvm";
	    String hostIpPre = "192.169.110";
		
	    
/*************************************寻找不重复的link*************************************/	    

/*		List<Link> vlink = new ArrayList<Link>();
		List<Node> nodes = new ArrayList<Node>();
	    List<Link> links = new ArrayList<Link>();
	    List<String> nodeId = new ArrayList<String>();
		List<String> tn = new ArrayList<String>();
		List<String> ip =new ArrayList<String>();

	    nodes = tp.getNode();
		links = tp.getLink();
		int linkNum = links.size();
		int nodeNum = nodes.size();
	    Node tempNode = new Node();
	    Destination des = new Destination();
	    Source source = new Source();
	    String desNode = new String();
	    String sourceNode = new String();
	    
	    for(int i = 0;i<nodeNum;i++){
			tempNode = nodes.get(i);
			if(tempNode.getNodeId().charAt(0)!='h'){
				nodeId.add(tempNode.getNodeId());
				List<String> en = new ArrayList<String>();//exist node
				//Vmsetting tempVmsetting = new Vmsetting();
				//tempVmsetting.setPortSize("0");
	
			    tn.add(tempNode.getNodeId());
				//vmsetting.add(tempVmsetting);
				//遍历link找出不重复的link
				for(int j=0; j < linkNum ; j++){
					Link tempLink =  new Link();
					des = links.get(j).getDestination();
					source =links.get(j).getSource();
					
					desNode = des.getDestNode();
					sourceNode = source.getSourceNode();
					Boolean exist = false;
					//当destinationNode或者sourceNode等于当前node时 判断此条link是否被加入到vlink中，没有的话加入
					//新的link中 destinationnode为当前的遍历的节点
					if(desNode.equals(tempNode.getNodeId())){
						Destination tempDes = new Destination();
						Source tempSource = new Source();
						tempDes.setDestNode(desNode);
						tempSource.setSourceNode(sourceNode);
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
							vlink.add(tempLink);
						}
						en.add(sourceNode);
	
					}else if(sourceNode.equals(tempNode.getNodeId())){
						Destination tempDes = new Destination();
						Source tempSource = new Source();
						tempDes.setDestNode(sourceNode);
						tempSource.setSourceNode(desNode);
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
	    	System.out.println(vlink.get(i));

	    }
	    
/****************************************************************************************/	    

	    //mac address 通过cluster来获得 
	    //ip address通过link信息来获得
/*		for(int i=0;i<clusterInfo.size();i++){
			
			ClusterInfo aCluster = new ClusterInfo();
			Vmsetting aVm = new Vmsetting();
			aCluster = clusterInfo.get(i);
			aVm.setVmId(aCluster.getClusterId());
			aVm.setName(namePre+String.valueOf(i));
			aVm.setHostIp(hostIpPre+"."+String.valueOf(i));
			aVm.setHostMacAddress(getMacAddress());
			aVm.setPortSize("0");
			List<PortList> portList = new ArrayList<PortList>();
			//add port
			System.out.println("edgeportttttttttttttttt"+aCluster.getEdgePort().size());
			for(int j=0;j<aCluster.getEdgePort().size();j++){
				EdgePort aEdgePort = new EdgePort();
				aEdgePort= aCluster.getEdgePort().get(j);
				System.out.println("dpportttttttttttttttt"+aEdgePort.getDpPort().size());
				for(int m=0;m<aEdgePort.getDpPort().size();m++){
					PortList aPort = new PortList();
					aPort.setName(aVm.getName()+"."+String.valueOf(m));
					aVm.setPortSize(String.valueOf(Integer.parseInt(aVm.getPortSize())+1));
				    //aPort.setMacAddress(aEdgePort.getMacAddress().get(m));
				    aVm.getPortList().add(aPort);
				}
					
			}
			vmsetting.add(aVm);
		}
		
		
/*********************************生成IP**********************************************/

/*	    ip = getIpInfo();
	    int last16 = 1;
	    int hostIpLast8 = 1;
	    String hostMacAddress;
		
	    for(int i =0; i<vmsetting.size();i++){
	    	//可能需要加的功能---->如果长度大于244的话 throw exception
	    	//暂时假设节点少于244个
	    	for(int j=0;j<vlink.size();j++){
    		
	    		//判断link的destination node是否和vmsetting对应
	    		if(vlink.get(j).getDestination().getDestNode().equals(tn.get(i))){
	    	    	int last8 =1;
	    			
	    	    	String ipPre = ip.get(0)+"."+String.valueOf(last16);
	    	    	last16++;

	    	    	//得到portNum后通过和cluster里对比来获得mac地址并且给对应ip地址赋值
	    	    	int portNumIndex = 0;
	    	    	String portNum = vmsetting.get(i).getName().substring(vmsetting.get(i).getName().indexOf(":")+1);
/*************************遍历cluster来找mac地址，和port的index*************************/
/*	    	    	System.out.println("******遍历cluster来找mac地址，和port的index*******");
	    	    	for(int m=0;m<clusterInfo.size();m++){
	    	    		if(clusterInfo.get(m).getClusterId().equals(vmsetting.get(i).getVmId())){
	    	    			for(int o=0;o<clusterInfo.get(m).getEdgePort().get(0).getDpPort().size();o++){
	    	    				System.out.println(portNum+" is port number.");
	    	    				System.out.println(clusterInfo.get(m).getEdgePort().get(0).getDpPort().get(o)+"is another");
	    	    				if(clusterInfo.get(m).getEdgePort().get(0).getDpPort().get(o).equals(portNum)){
	    	    					portNumIndex = o;
	    	    					String mac = clusterInfo.get(m).getEdgePort().get(0).getMacAddress().get(o);
	    	    					vmsetting.get(i).getPortList().get(o).setMacAddress(mac);
	    	    					
	    	    					System.out.println(mac);
	    	    					
	    	    				}
	    	    			}
	    	    		}
	    	    	}
	    	    	System.out.println("******遍历完*******");
/*************************************************************************/	    	    
/*	    	    	System.out.println("******portNumIndex*******"+portNumIndex);
	    	    	System.out.println(vmsetting.get(i));
	    	    	vmsetting.get(i).getPortList().get(portNumIndex).setIpAddress(ipPre+"."+String.valueOf(last8)+"/24");
	    	    	System.out.println("******遍历完一个语句后*******");
	    			last8++;
	    			vmsetting.get(i).getPortList().get(portNumIndex).setNetwork(ipPre+".0/24");
//遍历查找
	    			
	    			
	    			//portList2.setIpAddress(ipPre+"."+String.valueOf(last8)+"/24");
	    			//portList2.setNetwork(ipPre+".0/24");
	    			//vmsetting.get(i).getPortList().add(portList1);
	    			
	    			System.out.println("******for循环前*******");	    			
	    			for(int n = 0;n<tn.size();n++){
	    				if(tn.get(n).equals(vlink.get(j).getSource().getSourceNode())){
	    					//portSize = Integer.parseInt(vmsetting.get(n).getPortSize());
	    	    			//portSize++;

	    	    			//vmsetting.get(n).setPortSize(String.valueOf(portSize));
	    					/*************************遍历cluster来找mac地址，和port的index*************************/
/*	    	    	    	for(int m=0;m<clusterInfo.size();m++){
	    	    	    		if(clusterInfo.get(m).getClusterId().equals(vmsetting.get(i).getVmId())){
	    	    	    			for(int o=0;o<clusterInfo.get(m).getEdgePort().get(0).getDpPort().size();o++)
	    	    	    				if(clusterInfo.get(m).getEdgePort().get(0).getDpPort().get(o).equals(portNum)){
	    	    	    					portNumIndex = o;
	    	    	    					String mac = clusterInfo.get(m).getEdgePort().get(0).getMacAddress().get(o);
	    	    	    					vmsetting.get(n).getPortList().get(o).setMacAddress(mac);
	    	    	    					}
	    	    	    		}
	    	    	    	}
	    	    	    	/*************************************************************************/	  	    					
	    					
	/*    	    	    	vmsetting.get(n).getPortList().get(portNumIndex).setIpAddress(ipPre+"."+String.valueOf(last8)+"/24");
	    	    			last8++;
	    	    			vmsetting.get(n).getPortList().get(portNumIndex).setNetwork(ipPre+".0/24");
	    				}
	    			}
	    			
	    		}
    		
	    	}		
	    }
/**************************************************************************************/
	/*	System.out.println("vmsetting sizeeeeeeeeeeeeeeeeeeee"+vmsetting.size());
		for(int i=0;i<vmsetting.size();i++){
			System.out.println("new");
			System.out.println(vmsetting.get(i));
		}*/
		
		return vmsetting;
	}
	
	
	private static List<String> getIpInfo(){
		List<String> info = new ArrayList<String>();
		String ip=new String();
		int maskLength = 16;
		int restLength = 32-maskLength;
		
		for(int n = 0;n<maskLength/8;n++){
			ip  = ip + "." + String.valueOf((int)(1+Math.random()*253));
		}
		ip=ip.substring(1);
		System.out.println(ip);
		
		info.add(ip);
		info.add(String.valueOf(maskLength));
		return  info;
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
