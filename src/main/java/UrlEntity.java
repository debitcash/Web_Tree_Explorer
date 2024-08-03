import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/* 
 * UrlEntity.java: Defines a JPA entity that maps the Url class to the 'records' database table. 
 * This entity includes fields for 'name', 'depth', and 'path', along with methods to convert
 * between the entity and a data transfer object (DTO). 
 * 
 * Author: GitHub @debitcash
 * 
 */

@Entity
@Table (name = "records")
public class UrlEntity {
	@Id
	private String name;
	@Column(columnDefinition = "LONGTEXT")
	private String path;
	private int depth;
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String string) {
		this.path = string;
	}
	
	public void fromDto(Url url)
	{
		this.setName(url.getName());
		this.setDepth(url.getDepth());
		this.setPath(url.getPath());
	}

	public Url toDto() {
		Url url = new Url();
		url.setDepth(this.getDepth());
		url.setName(this.getName());
		url.setPath(this.getPath());
		return url;
	}
}
