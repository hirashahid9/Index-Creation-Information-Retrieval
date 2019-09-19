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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author hiras
 */
public class Part2_UsingHashMap {
    
    public static HashMap<String,Integer> termids = new HashMap<String,Integer>();
    public static  HashMap<String, Integer> docids = new HashMap<String, Integer>();
    public static HashMap<Integer, termDetails> invertedIndex = new HashMap<Integer, termDetails>();
        
     public static void listFiles(String path) throws Exception
    {
        File folder = new File(path);
        File[] files = folder.listFiles();
                
        FileWriter fw=new FileWriter("C:\\Users\\hiras\\Documents\\termids.txt"); 
        File stoplist=new File("C:\\Users\\hiras\\Documents\\stoplist.txt");         //you have to give your own paths here
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
                      
                System.out.println(file.getName());
                System.out.println("Reading: "+file.getName());
                
                FileReader reader= new FileReader(file.getAbsolutePath());
                String str=new String(Part2_UsingHashMap.extractText(reader));     //to remove html tags etc
              
                
                StringTokenizer st = new StringTokenizer(str, ":/;/.?@#^^&[]=; -''…�\\√´“®<>&+”’—\"»©‘–·*{}%|(),!\t\n");
                //to tokenize the file
                
               int position=0;     //for words position in document
              
               
                while(st.hasMoreTokens()){
               
                    
                    String tempS=st.nextToken().toLowerCase();
                    
                    SnowballStemmer stemmer=new englishStemmer();
                    stemmer.setCurrent(tempS);
                    stemmer.stem();             //for stemming
                    String tempStr=stemmer.getCurrent();
                    
                    
                    position++;               //position of term in document
                         
                    if(!stopl.containsKey(tempStr))    //word not present in stop words list
                    {
                        if(!termids.containsKey(tempStr)){    //word not mapped already
                             
                            LinkedList<Integer> object = new LinkedList<Integer>();
                            
                            termDetails t=new termDetails();
                            t.docFrequency=1;                
                            t.termFrequency=1;
                            
                           
                            object.add(position);   //add position of term 
                            t.map.put(docids.get(file.getName()),object );  //(docID,list of positions in doc)
                           
                                                        
                            termids.put(tempStr,index);           //give this term a termID
                            writeToFile(fw, termids,tempStr);     //write term and termId in file
                            
                            invertedIndex.put(index, t);    //for inverted Index
                            index++;                         //to assign termId to next term
                          
                        }
                        else
                        {
                             int index1=termids.get(tempStr);
                             termDetails t=invertedIndex.get(index1);    
                             
                             t.termFrequency++;
                             
                             LinkedList<Integer> listOfPositions=t.map.get(docids.get(file.getName()));
                             if(listOfPositions==null){   //terms first occurence in the documentt
                             t.docFrequency++;            //but already present in any previous docs
                             
                             listOfPositions=new LinkedList<Integer>();
                             listOfPositions.add(position);
                             t.map.put(docids.get(file.getName()), listOfPositions);  
                             }
                             
                             else{
                             listOfPositions.add(position);
                             }
                             invertedIndex.replace(termids.get(tempStr), t);
                        }
                    }
                }
                      
                docIndex++;  //doc id
           }
            else if (file.isDirectory())
            {
                listFiles(file.getAbsolutePath());
            }
        }
         System.out.println("....");
        deltaEncoding(invertedIndex,docids.size());  //delta encoding of positions
        docDeltaEncoding(invertedIndex);            //first moving docid,positions from hashmap
                                                    //to linkedList then applying delta envoding on docs
        
        
        writeIndexToFile(invertedIndex,docids.size());  //inverted index to file
        
        fw.close();
        fwDoc.close();
         
    }
    public static void deltaEncoding(HashMap<Integer, termDetails> invertedIndex,int noOfDocs)
    {
        System.out.println("Delta Encoding....");
           int a=1;
        while(a<=invertedIndex.size())
        {
            termDetails t=invertedIndex.get(a);
            
            int b=1;
            while(b<=noOfDocs){
            
            int k=0;
            LinkedList<Integer> l=new LinkedList<Integer>();
            LinkedList<Integer> l1=new LinkedList<Integer>();
            l=t.map.get(b);
            if(l!=null){
               Collections.sort(l);   //sorting ths positions in list
               int x=l.size()-1;
               while(x>0)
               {
                  int no=l.get(x)-l.get(x-1);
                  l.removeLast();          //apply delta encoding and moving to another list
                  l1.addFirst(no);
                  //l.addLast(no);
                  x--;
               }
               int no=l.removeLast();
               l1.addFirst(no);
               t.map.replace(b, l1);
            }
            b++;
            }
             a++;
        } 
     
    }
    
    public static void docDeltaEncoding(HashMap<Integer, termDetails> invertedIndex)
    {
           System.out.println("Doc Delta Encoding....");
          HashMap<Integer,LinkedList<Integer>> templist=null;
        for(int i=1;i<=invertedIndex.size();i++)
        {
            LinkedList<docDetails> dl=new LinkedList<docDetails>();
            int size=0;
            termDetails t=invertedIndex.get(i);
            templist=t.map;                  //to move hashmap<docids,listOfPositions> to a LinkeList
            
            for(int k=1;size<templist.size();k++)
            {
               if(templist.get(k)!=null)
               {
                 docDetails d=new docDetails();
                 d.docId=k;
                 d.listOfPositions=templist.get(k);
                 size++;
                 
                 dl.add(d);
               }
               
              
            }
            
            LinkedList<docDetails> finaldl=new LinkedList<docDetails>();
             int x=dl.size()-1;            //applying delta encoding on docs
               while(x>0)
               {
                  int no=dl.get(x).docId-dl.get(x-1).docId;
                  LinkedList<Integer> t1=dl.get(x).listOfPositions;
                  docDetails d=new docDetails();
                  d.docId=no;
                  d.listOfPositions=t1;
                  
                  dl.removeLast();
                  finaldl.addFirst(d);
                  //l.addLast(no);
                  x--;
               }
               docDetails no=dl.removeLast();
               finaldl.addFirst(no);
               
             t.document=finaldl;
             
        }
        
    
    }
     public static void writeIndexToFile(HashMap<Integer, termDetails> invertedIndex,int noOfDocs) throws IOException
     {
        FileWriter f=new FileWriter("C:\\Users\\hiras\\Documents\\term_index.txt"); 
           
         System.out.println("Writing index to file....");
        int a=1;
        while(a<=invertedIndex.size())
        {
            f.append(Integer.toString(a));
            f.append(" ");
            termDetails t=invertedIndex.get(a);
            f.append(Integer.toString(t.termFrequency));
            f.append(" ");
            f.append(Integer.toString(t.docFrequency));
            f.append(" ");
            
            
            int b=0;
            while(b<t.document.size())
            {
                int j=0;
                LinkedList<Integer> temp=t.document.get(b).listOfPositions;
                while(j < temp.size())
                {
                    if(j==0)
                    {
                        f.append(Integer.toString(t.document.get(b).docId));
                    }
                    else
                    {
                        f.append(Integer.toString(0));
                    }
                    f.append(",");    
                    f.append(Integer.toString(temp.get(j)));
                    f.append(" ");
                    j++;
                }
                b++;
            
            }
            
            f.append("\n");
             a++;
        } 
        f.close();
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
        
        return Jsoup.parse(strbuffer.toString()).text();   //yo parse and remove html tags
    }
     
     public static void writeToFile(FileWriter fw,HashMap<String, Integer>termids,String tempStr) throws IOException
     {
          
           fw.append(Integer.toString(termids.get(tempStr)));
           fw.append("\t");
           fw.append(tempStr);
           fw.append("\n");
             
     }
   
    
}