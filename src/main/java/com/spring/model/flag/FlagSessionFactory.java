package com.spring.model.flag;

final public class FlagSessionFactory {
	private static Flag flag = null;
	
	private FlagSessionFactory(){}
	
	static{
		flag = new Flag();
	}
	
	public static Flag getFlag(){
		return flag;
	}
	
}
