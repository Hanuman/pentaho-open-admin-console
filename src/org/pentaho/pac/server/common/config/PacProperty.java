package org.pentaho.pac.server.common.config;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "PAC_PROPERTIES")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MODULE_NAME")
public abstract class PacProperty {

	public PacProperty() {
	}

	public PacProperty(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Id
	@Column(name = "PROPERTY_NAME")
	private String name;

	@Column(name = "PROPERTY_VALUE")
	private String value;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
