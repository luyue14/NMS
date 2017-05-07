package com.spring.model.message;

/**
 * @author luyue
 *
 */
public class SuccessMessage {
	private String isSuccess = new String();

	public SuccessMessage(){};
	public SuccessMessage(String message){
		this.isSuccess = message;
	}
	
	public String getIsSuccess() {
		return isSuccess;
	}

	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}

	@Override
	public String toString() {
		return "successMessage [isSuccess=" + isSuccess + "]";
	}
	
}
