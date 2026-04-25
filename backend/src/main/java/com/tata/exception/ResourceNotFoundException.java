package com.tata.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
	
	String entityName;
	String attributeName;
	Object attributeValue;
	
	public ResourceNotFoundException(String entityName, String attributeName,Object attributeValue) {
		super(String.format("Oops..!! %s not found with %s: %d", entityName, attributeName, attributeValue));
		
		this.entityName=entityName;
		this.attributeName=attributeName;
		this.attributeValue=attributeValue;
	}

}
