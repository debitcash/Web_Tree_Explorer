import java.util.ArrayList;

/* 
 * Url.java: Defines a data transfer object used in data-related classes.
 * 
 * Author: GitHub @debitcash
 * 
 */

public class Url {
	private String name;
	private int depth;
	private String path;
	private ArrayList<String> pathArray;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getDepth() {
		return depth;
	}
	
	public void setDepth(int depth) {
		this.depth = depth;
	}
	
	public String getPath() {
		return path;
	}
	
	public void setPath(String string) {
		this.path = string;
	}
	
	public ArrayList<String> getPathArray() {
		return pathArray;
	}
	
	public void setPathArray(ArrayList<String> pathArray) {
		this.pathArray = pathArray;
	}
}
