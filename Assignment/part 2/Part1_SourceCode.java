/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *  HIRA SHAHID
 * 17L-4090
 * CS-B
 * This is a code to assign termids to all terms in corpus and docids to all documents 
 * and write them in termids.txt and docids.txt 
 * 
 */
public class Part1_SourceCode {
    
      public static HashMap<String,Integer> termids = new HashMap<String,Integer>();
    public static  HashMap<String, Integer> docids = new HashMap<String, Integer>();
    
    public static void listFiles(String path) throws Exception
    {
        File folder = new File(path);
        File[] files = folder.listFiles();
                
        FileWriter fw=new FileWriter("C:\\Users\\hiras\\Documents\\termids.txt"); //ypu have to give your own paths here
        File stoplist=new File("C:\\Users\\hiras\\Documents\\stoplist.txt");
        FileWriter fwDoc=new FileWriter("C:\\Users\\hiras\\Documents\\docids.txt"); 
        
        HashMap< String,Integer> stopl = new HashMap<String,Integer>();
        
        Scanner s = new Scanner(stoplist);     //to read stoplist
        
        int j=1;
        while (s.hasNext()) {
                stopl.put(s.nextLine(),j);       //read stop words and map them to some value
                j++;
            }
   
         
           int index=1;        //to map terms to termIDS
           int docIndex=1;     //to map documents to docIDS 
          
         for (File file : files)
        {
            if (file.isFile())
            {
                      
               
                docids.put(file.getName(), docIndex);      //hash map to map documents to some integer(doc id) 
                writeToFile(fwDoc, docids,file.getName()); //write doc name and docId in file
                      
                System.out.println("Reading: "+file.getName());
                
                FileReader reader= new FileReader(file.getAbsolutePath());
                String str=new String(Part1_SourceCode.extractText(reader));     //to remove html tags 
              
                
                StringTokenizer st = new StringTokenizer(str, ":/;/.?@#^^&[]=; -''…�\\√´“®<>&+”’—\"»©‘–·*{}%|(),!\t\n");
                //to tokenize the file
                
               int position=0;     //for words position in document
              
               
                while(st.hasMoreTokens()){
               
                    
                    String tempS=st.nextToken().toLowerCase();
                    
                    SnowballStemmer stemmer=new englishStemmer();
                    stemmer.setCurrent(tempS);
                    stemmer.stem();
                    String tempStr=stemmer.getCurrent();
                    
                    
                    position++;               //position of term in document
                         
                    if(!stopl.containsKey(tempStr))    //word not present in stop words list
                    {
                        if(!termids.containsKey(tempStr)){    //word not mapped already
                             
                            LinkedList<Integer> object = new LinkedList<Integer>();
                            
                            termids.put(tempStr,index);           //give this term a termID
                            writeToFile(fw, termids,tempStr);     //write term and termId in file
                           
                            index++;                         //to assign termId to next term
                          
                        }
                       
                    }
                }
                      
                docIndex++;
           }
            else if (file.isDirectory())
            {
                listFiles(file.getAbsolutePath());   //if directory
            }
        }
        
         System.out.println("termids.txt created.");
         System.out.println("docids.txt created");
        fw.close();
        fwDoc.close();
         
    }
     public static void writeToFile(FileWriter fw,HashMap<String, Integer>termids,String tempStr) throws IOException
     {
          
           fw.append(Integer.toString(termids.get(tempStr)));
           fw.append("\t");
           fw.append(tempStr);      
           fw.append("\n");
             
     }
     
      public static String extractText(Reader reader) throws IOException {
        StringBuilder strbuffer;
        BufferedReader bufreader;
        
        strbuffer= new StringBuilder();
        bufreader= new BufferedReader(reader);
        
        
        String str=bufreader.readLine();
        while ( str != null) 
        {
            strbuffer.append(str);
            strbuffer.append(" ");               
            str=bufreader.readLine();
        }
        
        return Jsoup.parse(strbuffer.toString()).text();            //to extract text from documents
    }                                                               //and remove html tags
    
}
