package com.spring.aop;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class CheckHandler {
	static Logger logger = Logger.getLogger(CheckHandler.class);  
	@Pointcut(value = "execution(public * com.spring.controller..*.handler*(..))")
	public void checkHandler(){
		
        
	}
	
	@Before("checkHandler()")
	public void beforeHandle(JoinPoint joinPoint) {
		StringBuffer sb = new StringBuffer();  
		sb.append("开始方法："+joinPoint.getTarget().getClass() + "." + joinPoint.getSignature().getName()+ "()  ");  
		sb.append("Info：[enter into handler]");  
        logger.info(sb.toString());  
	}
	
	@After("checkHandler()")
	public void afterHandle(JoinPoint joinPoint) {
		StringBuffer sb = new StringBuffer();  
		sb.append("结束方法："+joinPoint.getTarget().getClass() + "." + joinPoint.getSignature().getName()+ "()  ");  
		sb.append("Info：[after into handler]");  
		logger.info(sb.toString());
	}
	
	@AfterThrowing(pointcut = "checkHandler()",throwing = "ex")
	public void afterThrowE(JoinPoint joinPoint,Exception ex) {
		logger.info("after throw exception.");
		StringBuffer sb = new StringBuffer();  
		//sb.append("开始方法："+joinPoint.getTarget().getClass() + "." + joinPoint.getSignature().getName()+ "()  ");  
        sb.append("错误信息如下：["+ex.getMessage()+"]");  
        logger.error(sb.toString());  
	}
	/*
	@Before(value = "execution(public * com.spring.controller..*.handler*(..))")
	public void beforeHandle() {
		System.out.println("before handle.");
	}


	@After(value = "execution(public * com.spring.controller..*.handler*(..))")
	public void afterHandle() {
		System.out.println("after handle.");
	}
*/	
}
