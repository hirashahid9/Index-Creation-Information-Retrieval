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
                      
                System.out.println(file.getName());
                
                FileReader reader= new FileReader(file.getAbsolutePath());
                String str=new String(Part2_UsingHashMap.extractText(reader));     //to remove html tags etc
               
                System.out.println(str);
              
                
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
                            
                            termDetails t=new termDetails();
                            t.docFrequency=1;                
                            t.termFrequency=1;
                            
                           
                            object.add(position);
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
                             if(listOfPositions==null){
                             t.docFrequency++;
                             
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
                      
                docIndex++;
           }
            else if (file.isDirectory())
            {
                listFiles(file.getAbsolutePath());
            }
        }
         
        deltaEncoding(invertedIndex,docids.size());
        docDeltaEncoding(invertedIndex);
       
        
        
        writeIndexToFile(invertedIndex,docids.size());
        
        fw.close();
        fwDoc.close();
        
        
        
        System.out.println("Inverted Index: ");
        int z=1;
        while(z<invertedIndex.size())
        {
            System.out.print(z);
            System.out.print("\t");
           // termDetails t=invertedIndex.get(a);
           
            invertedIndex.get(z).printTermDetails();
            System.out.print("\n");
             z++;
        }
         
    }
    public static void deltaEncoding(HashMap<Integer, termDetails> invertedIndex,int noOfDocs)
    {
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
               Collections.sort(l);
               int x=l.size()-1;
               while(x>0)
               {
                  int no=l.get(x)-l.get(x-1);
                  l.removeLast();
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
          
          HashMap<Integer,LinkedList<Integer>> templist=null;
        for(int i=1;i<=invertedIndex.size();i++)
        {
            LinkedList<docDetails> dl=new LinkedList<docDetails>();
            int size=0;
            termDetails t=invertedIndex.get(i);
            templist=t.map;
            
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
             int x=dl.size()-1;
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
                    f.append(Integer.toString(t.document.get(b).docId));
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
        
        return Jsoup.parse(strbuffer.toString()).text();
    }
     
     public static void writeToFile(FileWriter fw,HashMap<String, Integer>termids,String tempStr) throws IOException
     {
          
           fw.append(Integer.toString(termids.get(tempStr)));
           fw.append("\t");
           fw.append(tempStr);
           fw.append("\n");
             
     }
    
    public static void main(String[] args)  throws Exception{

       // listFiles("C:\\Users\\hiras\\Desktop\\corpus");
       listFiles("C:\\Users\\hiras\\Downloads\\temp");
      // withoutHashMap.listSortFiles("C:\\Users\\hiras\\Downloads\\temp");
      // withoutHashMap.listSortFiles("C:\\Users\\hiras\\Desktop\\corpus");
         if(args.length!=1)
         {
           System.out.println("Invalid Information. Please enter word to be retrieved.");
         }
         else{
         
         String term=new String(args[0]);
         
         SnowballStemmer stemmer=new englishStemmer();
         stemmer.setCurrent(term);
         stemmer.stem();
         String tempStr=stemmer.getCurrent();
         
         if(termids.containsKey(tempStr))
         {
              int id=termids.get(tempStr);
            termDetails t=invertedIndex.get(id);
            System.out.println("Listings for the term: "+term);
            System.out.println("Term id: " +  id);
            System.out.println("Number of documents containing term: "+t.docFrequency);
            System.out.println("Term frequency in corpus: "+t.termFrequency);
         }
         else
         {
           System.out.println("Term not found in corpus.");
         }
         
        
         }
    }
    
}
