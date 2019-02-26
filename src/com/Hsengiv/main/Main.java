package com.Hsengiv.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Main {
		
  //Variable Declaration	
  public static WebDriver driver;
  public static List<WebElement>Languages ;
  public static List<WebElement>Projs ;
  public static List<String> Langs;
  public static List<String>Projects;
  public static List<String>HyperLinks;
  public static List<WebElement>Stars;
  public static List<String>StringStars;
  public static List<WebElement>Container ;
  
    public static void VisitSite(){
    	//Initializing all the variables to prevent Nullpointers
    	Container = new ArrayList<WebElement>(); //a segment consisting of the repo and the stars
    	//Stars = new ArrayList<WebElement>();  // 
    	StringStars = new ArrayList<String>();  //Strings containing number of stars per repo
    	driver= new ChromeDriver(); // Driver for chrome based interaction
    	HyperLinks = new ArrayList<String>(); // Storing all the links of the various languages used ie: C++ JAVA to iterate 
       Langs = new ArrayList<String>(); //Strings containing the various languages
       Languages = new ArrayList<WebElement>(); // The Web Elements of the language String that even contains the links
       
       Projs = new ArrayList<WebElement>();//Web Elements of the repos(text)
       Projects = new ArrayList<String>();//Strings of the various repos
	  driver.navigate().to("https://github.com/trending");//Visit the trending page
	
	  Languages = driver.findElements(By.xpath("//ul[@class = 'filter-list small']/li/a")); // Note: We use Xpath for easy search

	 
	  for(WebElement link: Languages){
		  
		  Langs.add(link.getText());//Extract language names
		  HyperLinks.add(link.getAttribute("href"));//Extract links to iterate through languages
	  }
	  for(int  i = 1 ; i <= HyperLinks.size(); i ++){//iterating through each language
		 
		  Container = driver.findElements(By.xpath("//div[@class = 'f6 text-gray mt-2']")); // List of ALL the repos for that language
			 
		  for(WebElement container: Container){//Contains a single repo and a single stars. The reason we are iterating through Container and not directly the xpath of the stars is because some repos have no Stars and hence may mess up the entire code
			  String s = container.getText();
			  char[] a = s.toCharArray();
			  String Star = new String();//Number of stars per repo
			  int index = 0;
			  for(int i1 = 0; i1 < s.length(); i1++){
				    if(a[i1] == 'b' && a[i1+1] == 'y'  && (i1+2)==a.length &&a[i1-1]==' '){//The format of the text is "APP_NAME Number of Forks Build By Number of Stars". Here we check for the "By" index
					  Star = "X";//if there is no stars, then "by" is the end of the string, so we declare Star as "X" to prevent errors
					  break;
				  }
				    else if(a[i1] == 'b' && a[i1+1] == 'y'  &&a[i1+2]!=' '&&a[i1-1]==' '){
					  index  = i1+2;//Star number is 2 after "by" index
					  Star = s.substring(index+1); //Number of Stars is only the Last few chars
					  break; 
				  }
				 
			  }
			  
			  StringStars.add(Star);// Adding the Number of Stars of that repo to the arraylist
			  
		  }
		  Projs = driver.findElements(By.xpath("//div[@class = 'd-inline-block col-9 mb-1']/h3/a"));//Iterating per repo
		  for(WebElement ele: Projs){
			  Projects.add(ele.getText());//Collecting list of strings corresponding to repo
		  }
		  Projects.add("***");// Seperate the end of a language by adding ***
		  StringStars.add("***");
		  if(i != HyperLinks.size())
		  driver.navigate().to(HyperLinks.get(i));// Go to the next langauge
	  }
		  

	 //Now we need to make the CSV file
	  try (PrintWriter writer = new PrintWriter(new File("test.csv"))) {//Creating an IO writer object

	      StringBuilder sb = new StringBuilder();//Easy appending with commas for CSV
	      sb.append("RepoName ");//Collumn Names
	      sb.append(',');
	      sb.append("Number Of Stars, ");
	      sb.append("Language Used, ");
	      sb.append('\n');
	      int languageIndex = 0;
	      for(int i = 0 ; i< Projects.size() ;  i++){
	    	  if(Projects.get(i)=="***"){//Signifying the end of a language
	    		  
	    		  languageIndex++;
	    		  
	    	  }
	    	  else{
	    	  sb.append(Projects.get(i));// adding rows
	    	  sb.append(',');
	    	  if(StringStars.get(i).contains(",")) //Some three digit numbers have commas. This will result in a new collumn in CSV. Hence we are replacing Commas with Fullstops
	    	  {   
	    		  String temp = StringStars.get(i).replace(',', '.');
	    		  StringStars.set(i,temp );
	    	  }
	    	  sb.append(StringStars.get(i)+ ",");
	    	  sb.append(Langs.get(languageIndex)+",");
	    	  sb.append('\n');//going to the next row
	    	  }
	    	  
	    	  
	      }

	      writer.write(sb.toString());//Finally pushing it onto the CSV file

	      System.out.println("done!");//Once everything finishes, an assurance message :)

	    } catch (FileNotFoundException e) {//Catching exceptions when FileNotFound Exceptions turn up
	      System.out.println(e.getMessage());
	    }
	  
  }
public static void main(String []args){
///	 ChromeDriverManager.getInstance().setup();
	System.setProperty("webdriver.chrome.driver", "D:\\chromedriver_win32\\chromedriver.exe");//Chrome Driver Setup
	VisitSite();//Calling the Function
	
}
}

