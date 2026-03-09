package com.tata.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Role {
	
	@Id
	private Integer roleId;
	
	private String roleName;

}
