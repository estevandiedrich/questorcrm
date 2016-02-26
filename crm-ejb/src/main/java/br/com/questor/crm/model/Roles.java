package br.com.questor.crm.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@SequenceGenerator(name="ROLES_SEQUENCE", sequenceName="ROLES_SEQUENCE", allocationSize=1, initialValue=1)
public class Roles implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7217453127916070913L;
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="ROLES_SEQUENCE")
    private Long id;
	@NotNull
	private String PrincipalID;
	@NotNull
	private String Role;
	@NotNull
	private String RoleGroup;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPrincipalID() {
		return PrincipalID;
	}
	public void setPrincipalID(String principalID) {
		PrincipalID = principalID;
	}
	public String getRole() {
		return Role;
	}
	public void setRole(String role) {
		Role = role;
	}
	public String getRoleGroup() {
		return RoleGroup;
	}
	public void setRoleGroup(String roleGroup) {
		RoleGroup = roleGroup;
	}
	
}
