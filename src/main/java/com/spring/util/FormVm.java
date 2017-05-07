package com.spring.util;

import java.util.ArrayList;
import java.util.List;

import com.spring.model.topo.*;
import com.spring.model.vm.*;


/**
 * @author luyue
 *
 */
public class FormVm {
	
	/**
	 * @param tp
	 * @return
	 */
	public static List<Vmsetting> formVmsetting(Topo tp){
		List<Vmsetting> vmsetting = new ArrayList<Vmsetting>();
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
	    
	    
	    //循环node，每循环一次创建一个vmsetting放入到list<Vmsetting>中
	    //找到不重复的link
	    //去掉host
	    for(int i = 0;i<nodeNum;i++){
			tempNode = nodes.get(i);
			if(tempNode.getNodeId().charAt(0)!='h'){
				nodeId.add(tempNode.getNodeId());
				List<String> en = new ArrayList<String>();//exist node
				Vmsetting tempVmsetting = new Vmsetting();
				tempVmsetting.setPortSize("0");
	
			    tn.add(tempNode.getNodeId());
				vmsetting.add(tempVmsetting);
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
	    
	    //get ipinfo
	    ip = getIpInfo();
System.out.println(ip.get(0));	    
	    int last16 = 1;
	    int hostIpLast8 = 1;
	    String namePre = "rfvm";
	    String hostIpPre = "192.169.110";
	    String hostMacAddress;
	    
	    //get vmsetting
	    //tn 和 vmsetting顺序相同 根据tn来判断把ip信息放在哪个vm中
	    //暂定mask长度为24，前16位随机，后八位和后九到十六位递加
    	System.out.println("vmsize"+vmsetting.size());

	    for(int i =0; i<vmsetting.size();i++){
	    	vmsetting.get(i).setVmId(String.valueOf(i));
	    	//可能需要加的功能---->如果长度大于244的话 throw exception
	    	//暂时假设节点少于244个
	    	int portNameLast = 0;
	    	String nameOfVm = namePre+String.valueOf(i);
	    	vmsetting.get(i).setName(nameOfVm);
	    	vmsetting.get(i).setHostIp(hostIpPre+"."+String.valueOf(hostIpLast8));
	    	hostIpLast8++;
	    	vmsetting.get(i).setHostMacAddress(getMacAddress());
	    	for(int j=0;j<vlink.size();j++){
    		
	    		//判断link的destination node是否和vmsetting对应
	    		if(vlink.get(j).getDestination().getDestNode().equals(tn.get(i))){
	    	    	int last8 =1;

	    			PortList portList1 = new PortList();
	    			PortList portList2 = new PortList();
	    			
	    	    	String ipPre = ip.get(0)+"."+String.valueOf(last16);
	    	    	last16++;
//当portsize为null时出错
			    	
	    			int portSize = Integer.parseInt(vmsetting.get(i).getPortSize());
	    			portSize++;

	    			vmsetting.get(i).setPortSize(String.valueOf(portSize));

	    			portList1.setName(namePre+String.valueOf(i)+"."+String.valueOf(portNameLast));
	    			portNameLast++;
	    			portList1.setIpAddress(ipPre+"."+String.valueOf(last8)+"/24");
	    			last8++;
	    			portList1.setMacAddress(getMacAddress());
	    			portList1.setNetwork(ipPre+".0/24");
	    			portList2.setIpAddress(ipPre+"."+String.valueOf(last8)+"/24");
	    			portList2.setMacAddress(getMacAddress());
	    			portList2.setNetwork(ipPre+".0/24");
	    			vmsetting.get(i).getPortList().add(portList1);

	    			for(int n = 0;n<tn.size();n++){
	    				if(tn.get(n).equals(vlink.get(j).getSource().getSourceNode())){
	    					portSize = Integer.parseInt(vmsetting.get(n).getPortSize());
	    	    			portSize++;

	    	    			vmsetting.get(n).setPortSize(String.valueOf(portSize));
	    	    			vmsetting.get(n).getPortList().add(portList2);
	    				}
	    			}
	    			
	    		}
	    		
	    		//为没有加portname的port 加上name
	    		for(int m=0;m<vmsetting.get(i).getPortList().size();m++ ){
	    			if(vmsetting.get(i).getPortList().get(m).getName()==null){
	    				vmsetting.get(i).getPortList().get(m).setName(namePre+String.valueOf(i)+"."+String.valueOf(portNameLast));
	    			   portNameLast++;
	    			}
	    		}
	    		
	    		
	    	}
	    }
	    

	    for(int i=0;i<vmsetting.size();i++){
	    	System.out.println("vmsetting");
	    	System.out.println(vmsetting.get(i));
	    }
	    */
		return vmsetting;
	}
	
	
	/**
	 * @return List<String>
	 * 	index 0 mask
	 * 	index 1 mask length
	 */
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
	

