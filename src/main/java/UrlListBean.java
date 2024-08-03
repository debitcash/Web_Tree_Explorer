import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

/* 
 * UrlListBean.java: CDI bean used to provide URLs for display in an ArrayList to preserve the ordered array for display in JSF allresults.xhtml.
 * Acts as a bridge between the logic bean and the database.
 * 
 * Author: GitHub @debitcash
 * 
 */

@Named
@SessionScoped
public class UrlListBean implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@EJB
	private UrlsManagerBean urlsManagerBean;
	
	private Url newUrl = new Url();

	// Provides all the Url records available in the database
	public List<Url> getUrls() {
		List<Url> result = new ArrayList<>();
		List<UrlEntity> entities = urlsManagerBean.readList();
		
		for (UrlEntity urlEntity: entities) 
		{
			result.add(urlEntity.toDto());
			int size = result.size() - 1;
			String pathString = result.get(size).getPath();
			pathString = pathString.replace("[", "").replace("]", "");
			ArrayList<String> orderedPath = getOrderedPath(pathString);
			result.get(size).setPathArray(orderedPath);
		}
		return result;
	}
	
	// Adds the newly analyzed URL information to the database
	public void createNewUrl()
	{
		UrlEntity urlEntity = new UrlEntity();
		urlEntity.fromDto(newUrl);
		urlsManagerBean.create(urlEntity);
		newUrl = new Url();
	}
	
	public Url getNewUrl()
	{
		return newUrl;
	}
	
	// Returns sorted array from a string
	public ArrayList<String> getOrderedPath(String input)
	{
		String[] dataArray = input.split(",");
		ArrayList<String> dataList = new ArrayList<>(Arrays.asList(dataArray));
		return dataList;
	}


}
