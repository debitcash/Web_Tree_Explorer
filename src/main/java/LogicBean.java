import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;

import org.jsoup.nodes.Element;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

/* 
 * LogicBean.java: Handles web scraping tasks using Jsoup to extract and process URLs from a given starting point. 
 * It manages various types of links (e.g., external links, emails, images, PDFs) and tracks depth in the web hierarchy. 
 * The class interacts with injected beans to retrieve configuration and update results.
 * 
 * Author: GitHub @debitcash
 * 
 */


@Named("logicBean")
@RequestScoped

public class LogicBean implements Serializable 
{
	private static final long serialVersionUID = 1L;
    @Inject
    private InfoBean infoBean;  
    @Inject
    private UrlListBean urlListBean;
    
    private HashSet<String> visitedLinks = new HashSet<>(); // stores aTags that have been processed
    private ArrayList<String> algorythmPath = new ArrayList<>();
    private ArrayList<String> depthTracking = new ArrayList<>();
    
    private HashMap<String, HashSet<String>> allUniqueChildren = new HashMap<>();
    private HashMap<String, HashSet<String>> allDirectChildren = new HashMap<>();
    private TreeMap<String, HashSet<String>> sortedAllDirectChildren = new TreeMap<>();
    private ArrayList<String> allSortedKeys;
    
    
    private ArrayList<String> maxDepthArray = new ArrayList<>();;
    
    private HashSet<String> externalLinks = new HashSet<>();
    private HashSet<String> phones = new HashSet<>();
    private HashSet<String> emails = new HashSet<>();
    private HashSet<String> pictures = new HashSet<>();
    private HashSet<String> pdfs = new HashSet<>();
    
    private float progressIncrement = 1;
    
    private String homeLink;
    private String linkLimit;
    private String rowLimit;
    private int rowLimitInt;
    
    // Initializes the bean after construction. It processes the provided input, retrieves child URLs recursively, 
    // computes unique child URLs, sorts direct children, and updates the database with the results.
    @PostConstruct
    public void start() throws Exception
    {
    	int rowCount = 0;
    	
    	checkProvidedInput();
    	getChildren(homeLink, rowCount);
		getUniqueChildrenMap();
		sortAllDirectChildren();
     
		System.out.println(infoBean.getProgress());
		
		resetInfoForNewInput();
		updateDatabaseWithResult();
    }
    
    // Validates and sets the input values for the home link, link limit, and row limit. Provides default values if necessary.
    public void checkProvidedInput() {
    	homeLink = infoBean.getInfoHomeLink();
		linkLimit = infoBean.getInfoLinkLimit();
		rowLimit = infoBean.getInfoRowLimit();
		
		if (homeLink == "")
			homeLink= "https://www.tkemlupsbusiness.ca";
	 
		if (linkLimit == null || linkLimit == "")
			linkLimit= "";
		 
		if (rowLimit == null ||rowLimit.isEmpty())
		{
			rowLimitInt = 3;
		}
		else
			rowLimitInt = Integer.parseInt(rowLimit);
    }
    
    // Recursively fetches and processes child URLs from the given URL up to a specified depth. 
    // It filters and categorizes links based on their type and updates the tracking and progress information.
    public void getChildren(String url, int row) throws Exception
    {
    	depthTracking.add(url);
        algorythmPath.add(url);
        
        HashSet<String> allChildrenSet = new HashSet<>();
        
        if (!allDirectChildren.containsKey(url))
            allDirectChildren.put(url, allChildrenSet); 
        
        //get all the elements with <a> tag and store them in the elementList

        List<Element> elementList = new ArrayList<>();
        Elements aTags = new Elements(elementList);
        try {
	        Document doc = Jsoup.connect(url).get();
	        Element body = doc.body();
	        aTags = body.getElementsByTag("a");
        }
        catch (Exception e)
        {}
        
        LinkedList<String> filteredUrls = new LinkedList<>();
        
        // extract links from tagElements and sort them to appropriate maps/arrays
        for (Element tagElement : aTags) 
        {
            String link = tagElement.attr("abs:href");
            
            if (link.startsWith("http"))
                allDirectChildren.get(url).add(link);
            if (link.endsWith(".pdf"))
            	pdfs.add(link);
            
            if (link.startsWith("tel") || !link.startsWith("http") || visitedLinks.contains(link) || link.endsWith(".jpg") || link.endsWith(".png") || 
                    !(new URL(link).getHost()).equals(new URL(url).getHost()) || row >= rowLimitInt || link.equals(linkLimit)) 
            {
                if (link.startsWith("http") && !new URL(link).getHost().equals(new URL(url).getHost()))
                    externalLinks.add(link);
                 
                else if (link.startsWith("tel"))
                    phones.add(link.replace("tel:", ""));
                else if (link.startsWith("mail"))
                    emails.add(link.replace("mailto:", ""));
                else if (link.endsWith(".jpg") || link.endsWith(".png"))
                	pictures.add(link);
                continue;
            }
            filteredUrls.add(link);
        }
        
        if (filteredUrls.size() == 0)
        {
        	if(depthTracking.size() > maxDepthArray.size())
        	{
        		ArrayList<String> tempList = new ArrayList<>(depthTracking);
        		maxDepthArray = tempList;
        	}
        	depthTracking.remove(depthTracking.size() - 1);
        	return;
        }
        
        else
        {
            row++;
            
            for (String link: filteredUrls)
            {
                visitedLinks.add(link);
                
            }
            
            for (String filteredUrl: filteredUrls)
            {
            	if (url.equals(homeLink))
                {
                	int size = filteredUrls.size();
                	
                	infoBean.setProgress((1 - (size - progressIncrement)/size) * 100);
                	System.out.println((1 - (size - progressIncrement)/size) * 100);
                	progressIncrement++;
            	}
        		getChildren(filteredUrl, row); 
            }
            
          algorythmPath.add(url + "ENDED");  
          depthTracking.remove(depthTracking.size() - 1);
        return;
        }
    }
     
    //Computes a map of unique child URLs for each URL based on the algorithm path. Updates the allUniqueChildren map with new entries.
    private HashMap<String, HashSet<String>> getUniqueChildrenMap()
    {
       for (String url: algorythmPath)
       {
           if (allUniqueChildren.containsKey(url))
               continue;
               
           else
           {
               try{
                   ArrayList<String> newArrayList = new ArrayList<>(algorythmPath.subList(algorythmPath.indexOf(url) + 1, algorythmPath.lastIndexOf(url+"ENDED")));
                   newArrayList.removeIf(element -> element.contains("ENDED"));
                   newArrayList.removeIf(element -> element.equals(url));
                   newArrayList.removeIf(element -> !allDirectChildren.get(url).contains(element) || element.equals(url));
                   HashSet<String> newHashSet = new HashSet<>(newArrayList);
                   allUniqueChildren.put(url, newHashSet);
               }
               catch (Exception e)
               {
               }
           }
       }
       return allUniqueChildren;
    }
    
    // Sorts the direct children URLs by the number of unique children in descending order. 
    // Updates the sortedAllDirectChildren TreeMap and the allSortedKeys list.
    private void sortAllDirectChildren()
    {
    	sortedAllDirectChildren = new TreeMap<>(
            (key1, key2) -> {
           	 	HashSet<String> set1 = allDirectChildren.get(key1);
           	    HashSet<String> set2 = allDirectChildren.get(key2);

           	    int sizeCompare;
           	    try {
           	        int size1 = (set1 == null) ? 0 : set1.size();
           	        int size2 = (set2 == null) ? 0 : set2.size();
           	        sizeCompare = Integer.compare(size2, size1);
           	    } catch (Exception e) {
           	        return key1.compareTo(key2);
           	    }

           	    if (sizeCompare == 0) {
           	        return key1.compareTo(key2); // Fallback to natural order if sizes are equal
           	    }
           	    return sizeCompare;
           	});
       
    	sortedAllDirectChildren.putAll(allDirectChildren);
        
    	allSortedKeys = new ArrayList<>(sortedAllDirectChildren.keySet());
     }
    
    
    
    public void resetInfoForNewInput() 
    {
		infoBean.setInfoHomeLink(null);
		infoBean.setInfoLinkLimit(null);
		infoBean.setInfoRowLimit(null);
		infoBean.setProgress(0);
		infoBean.setProgressMessage("");
    }
    
    public void updateDatabaseWithResult()
    {
		urlListBean.getNewUrl().setName(homeLink);
		urlListBean.getNewUrl().setDepth(maxDepthArray.size());
		urlListBean.getNewUrl().setPath(maxDepthArray.toString());
		urlListBean.createNewUrl();
    }
    
    public ArrayList<String> provideSortedUniqueChildrenForUrl(String url)
    {
	   	 ArrayList<String> tempArray = new ArrayList<>(allUniqueChildren.get(url));
	   	 ArrayList<String> outputArray = new ArrayList<>();
   	 
	   	 for (String link : allSortedKeys)
	   	 {
	   		 if (tempArray.contains(link))
	   			 outputArray.add(link);
	   	 }

   	 	return outputArray;
    }
    
    
	public HashSet<String> getPhones()
	{
	    return phones;
	}
 
	public HashSet<String> getEmails()
	{
	    return emails;
	}
	
	public HashSet<String> getPictures()
	{
	    return pictures;
	}
 
	public  HashMap<String, HashSet<String>> getAllDirectChildren()
	{
	    return allDirectChildren;
	}
 
	public  HashMap<String, HashSet<String>> getAllUniqueChildren()
	{
	    return allUniqueChildren;
	}
 
	public  TreeMap<String, HashSet<String>> getSortedAllDirectChildren()
	{
	    return sortedAllDirectChildren;
	}
 
	public ArrayList<String> allSortedKeys()
	{
		return allSortedKeys;
	}
 
	public ArrayList<String> getMaxDepthArray()
	{
		return maxDepthArray;
	}
 
	public String getHomeLink()
	{
	    return homeLink;
	}
 
	public void setHomeLink(String link)
	{
		homeLink = link;
	}
 
	public void setRowLimit(String limit)
	{
		rowLimit = limit;
	}
 
	public String getRowLimit()
	{
		return rowLimit;
	}
 
	public void setLinkLimit(String link)
	{
		linkLimit = link;
	}
 
	public String getLinkLimit()
	{
		return linkLimit;
	}

	public HashSet<String> getPdfs()
	{
		return pdfs;
	}

	public void setPdfs(HashSet<String> pdfs) 
	{
		this.pdfs = pdfs;
	}
}
