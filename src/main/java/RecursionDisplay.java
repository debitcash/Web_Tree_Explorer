import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;

/*
 * RecursionDisplay.java: Custom JSF component <custom:showChildren> to recursively display a hierarchical structure of URLs
 * with their direct children and unique descendants. It dynamically generates a nested HTML table
 * structure to visualize the hierarchy.
 * 
 * Author: GitHub @debitcash
 * 
 */

// Generates the initial HTML structure and starts the recursive display of URL hierarchies.
@FacesComponent("RecursionDisplay")
public class RecursionDisplay extends UIComponentBase
{
	HashMap<String, HashSet<String>> allUniqueChildren;
	HashMap<String, HashSet<String>> allDirectChildren;
	int hiddenTableId = 0;
	
	// Encodes the beginning of the custom component. This method generates the initial HTML structure and starts the recursive display of URL hierarchies.
	public void encodeBegin (FacesContext context) throws IOException 
    {
        ResponseWriter writer = context.getResponseWriter();

        ArrayList<String> sortedUniqueChildrenForHome = (ArrayList<String>) getAttributes().get("firstValue"); 
        allUniqueChildren = (HashMap<String, HashSet<String>>) getAttributes().get("secondValue");  
        allDirectChildren = (HashMap<String, HashSet<String>>) getAttributes().get("thirdValue");
        
        for (String sortedUniqueChild: sortedUniqueChildrenForHome)
        {
        	int uniqueChildrenNum, directChildrenNum;
        	
        	HashSet<String> uniqueChildrenOfMainChildren = allUniqueChildren.get(sortedUniqueChild);
        	HashSet<String> directChildrenOfMainChildren = allDirectChildren.get(sortedUniqueChild);
        	
        	if(directChildrenOfMainChildren == null)
        		directChildrenNum = 0;
        	else
        		directChildrenNum = directChildrenOfMainChildren.size();
        	
        	if(uniqueChildrenOfMainChildren == null)
        		uniqueChildrenNum = 0;
        	else
        		uniqueChildrenNum = uniqueChildrenOfMainChildren.size();
        		
        	writer.write("<br/><label for=\"toggleTable" + hiddenTableId + "\" class=\"showTableText\">" + "" +
        					directChildrenNum +" " + sortedUniqueChild +  "___" + uniqueChildrenNum + "</label>" +
    						"<input type=\"checkbox\" id=\"toggleTable" + hiddenTableId + "\" class=\"hiddenCheckbox\">");
        	hiddenTableId++;
        	analyzeSet(uniqueChildrenOfMainChildren, context);
        }
    }

	@Override
	public String getFamily() {
		return "custom";
	}
	
	// Recursively generates HTML a passed set of Urls. This method sorts the URLs by their number of unique descendants
	// and generates a nested table structure.
	public void analyzeSet(HashSet<String> set, FacesContext context) {
		
		ResponseWriter writer = context.getResponseWriter();
		
		if (set == null)
			return;
		
		ArrayList<String> list = new ArrayList<>(set);
		
		list.sort((element1, element2) -> {
			int size1 = (allUniqueChildren.get(element1) == null) ? 0 : allUniqueChildren.get(element1).size();
		    int size2 = (allUniqueChildren.get(element2) == null) ? 0 : allUniqueChildren.get(element2).size();
			return Integer.compare(size2, size1);
		});
		
		try {
			writer.write("<table class=\"hiddenTable\" style=\"width:100%;border: 1px solid black;\"><tr><td style=\"width:10%;\"></td><td style=\"width:90%;\">");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		
		if (list != null)
        	for (String url : list)
        	{
        		int size, size1;
            	
        		if(allUniqueChildren.get(url) == null)
            		size = 0;
            	else
            		size = allUniqueChildren.get(url).size();
            	
            	if(allDirectChildren.get(url) == null)
            		size1 = 0;
            	else
            		size1 = allDirectChildren.get(url).size();
            	
	        	try {
	        		writer.write("<br/><label for=\"toggleTable" + hiddenTableId + "\" class=\"showTableText\">" + size1 + " " + url + 
        							"___" + size + "</label>" + "<input type=\"checkbox\" id=\"toggleTable" + hiddenTableId + "\" class=\"hiddenCheckbox\">");
	        		hiddenTableId++;
	        		
					//if(!url.equals(list.get(0)))
	        		
	        		//HashSet<String> cleanedArray = new HashSet<String>(allUniqueChildren.get(url));
	        		//cleanedArray.remove(url);
	        		
					//analyzeSet(cleanedArray, context);
	        		analyzeSet(allUniqueChildren.get(url), context);
	        	} 
	        	catch (Exception e) {}
        	}
		
		try {
			writer.write("</td></tr></table>");
		} 
		catch (IOException e) {
			e.printStackTrace();
		}

	}

}

