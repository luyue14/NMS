package com.spring.model.flag;

import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

public class Flag {
	
	Logger logger = Logger.getLogger(Flag.class);  
	
	//int flag = -1;
	Queue<String> flags = new LinkedList<String>();
	private boolean hasFlag = false;
	public synchronized void putFlag(String flag){
		if(flags.size()>4){
			try{
				this.wait();
			} catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
		this.flags.add(flag);
		logger.info("put flag = "+flag);
		hasFlag=true;
		this.notify();
	}
	
	public synchronized String getFlag() {
		if(!hasFlag){
			try{
				this.wait();
			}catch (InterruptedException e){
				e.printStackTrace();
			}
		}
		String flag = flags.poll();
		if(flags.size()==0){ hasFlag = false;}
		this.notify();
		logger.info("get flag = "+flag);
		return flag;
	}
	
}
