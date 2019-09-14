/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

/**
 *
 * @author hiras
 */
public class Part2_WithoutHashMap {
    
    static void listSortFiles(String path) throws Exception
    {
        File folder = new File(path);
        File[] files = folder.listFiles();
        
        HashMap<String,Integer> termids = new HashMap<String,Integer>();   //to store 
        HashMap<String, Integer> docids = new HashMap<String, Integer>();

        LinkedList<ListDetails> invertedIndex = new LinkedList<ListDetails>();
              
        FileWriter fw=new FileWriter("C:\\Users\\hiras\\Documents\\termids1.txt"); 
        File stoplist=new File("C:\\Users\\hiras\\Documents\\stoplist.txt");
        FileWriter fwDoc=new FileWriter("C:\\Users\\hiras\\Documents\\docids1.txt"); 
        ArrayList<ArrayList<Integer>> termExists=new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> termFirstPositionInDoc=new ArrayList<ArrayList<Integer>>();
        
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
                            ListDetails ld=new ListDetails();
                            
                            ArrayList<Integer> tmp=new ArrayList<Integer>();
                            tmp.add(docIndex);
                            termExists.add(tmp);
                            
                            ArrayList<Integer> tmp1=new ArrayList<Integer>();
                            tmp1.add(invertedIndex.size());
                            termFirstPositionInDoc.add(tmp1);
                            
                            termids.put(tempStr,index);           //give this term a termID
                            writeToFile(fw, termids,tempStr);     //write term and termId in file
                            
                            
                            ld.tID=index;
                            ld.dID=docids.get(file.getName());
                            ld.docFreq=1;
                            ld.listOfpositions.addLast(position);
                            invertedIndex.add(ld);
                           
                            index++;                         //to assign termId to next term
                         
                        }
                        else
                        {
                            ListDetails temp=new ListDetails();
                            
                            temp.dID=docids.get(file.getName());
                            temp.tID=termids.get(tempStr);
                   
                        
                            int ind=termids.get(tempStr);
                            ind--;
                            
                            if (termExists.get(ind).size() < docIndex)
                                                                       //first occurence of term in a document that is already in terms list
                            {    
                                temp.docFreq=1;
                                temp.listOfpositions.add(position);
                                 invertedIndex.add(temp); 
                                 termExists.get(ind).add(docIndex);
                                 termFirstPositionInDoc.get(ind).add(invertedIndex.size()-1);
                            }
                            else
                            {
                              int num=termFirstPositionInDoc.get(ind).get(docIndex-1);
                              ListDetails templd=invertedIndex.get(num);
                              templd.docFreq++;                  //subsequent occerences of term in a document
                              templd.listOfpositions.add(position);
                            }
       
                        }
                }
                }
                      
                docIndex++;
     
           }
            else if (file.isDirectory())
            {
                listSortFiles(file.getAbsolutePath());            //if it is a directory
            }
        }
       
        fw.close();
        fwDoc.close();
     
     
        Collections.sort(invertedIndex, new ListDetails());
            
        
        LinkedList<termDetailsWithoutHash> finalIndex=createPostingList(invertedIndex);
        deltaEncoding(finalIndex,docids.size());
        docDeltaEncoding(finalIndex);
        printIndex(finalIndex);
        
        
    }
  
   public static void docDeltaEncoding(LinkedList<termDetailsWithoutHash> invertedIndex)
    {
          
          HashMap<Integer,LinkedList<Integer>> templist=null;
        for(int i=0;i<invertedIndex.size();i++)
        {
            LinkedList<docDetails> dl=new LinkedList<docDetails>();
            int size=0;
            termDetailsWithoutHash t=invertedIndex.get(i);
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
              // templist.remove(i);
              
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
   
  public static void printIndex(LinkedList<termDetailsWithoutHash> finall) throws IOException
  {
        FileWriter f=new FileWriter("C:\\Users\\hiras\\Documents\\term_index_.txt"); 
       System.out.print("\n\n FINAL \n\n");
       int a=0;
       
         while(a<finall.size())
        {
            f.append(Integer.toString(a+1));
            f.append(" ");
            termDetailsWithoutHash t=finall.get(a);
            f.append(Integer.toString(t.termFreq));
            f.append(" ");
            f.append(Integer.toString(t.docFreq));
            f.append(" ");
            /*
            int b=1;
            while(b<=t.map.size()){
            
            int j=0;
            LinkedList<Integer> l;//=new LinkedList<Integer>();
            l=t.map.get(b);
            //System.out.println(l.size());
            if(l!=null){
            while(j<l.size())
            {
                
                f.append(Integer.toString(b));
                f.append(",");    
                f.append(Integer.toString(l.get(j)));
                f.append(" ");
                j++;
            }
            }
            b++;
            }
            
            */
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
  
  
  
   public static LinkedList<termDetailsWithoutHash> createPostingList(LinkedList<ListDetails> invertedIndex)
   {
         LinkedList<termDetailsWithoutHash> finalIndex=new LinkedList<termDetailsWithoutHash>();
         int c=1;
                                        //it makes a single posting list for each termid merging docids and lists of thier positions
         for(int i=0;i<invertedIndex.size();)
         {
             int termFreq=0;
             termDetailsWithoutHash td=new termDetailsWithoutHash();
            
             while(i<invertedIndex.size() && invertedIndex.get(i).tID==c)
             {
                 ListDetails temp=invertedIndex.get(i);
                 td.docFreq++;                                              
                 td.termFreq=td.termFreq+temp.docFreq;
                 td.map.put(temp.dID, temp.listOfpositions);
                 i++;
             }
             
             finalIndex.add(td);
              c++;
         }
         
         return finalIndex;   
   }
   public static void deltaEncoding(LinkedList<termDetailsWithoutHash> invertedIndex,int noOfDocs)
    {
           int a=0;
        while(a<invertedIndex.size())
        {
            termDetailsWithoutHash t=invertedIndex.get(a);
            
            int b=1;
            while(b<=noOfDocs){                        
                                                       
            int k=0;
            LinkedList<Integer> l;
            LinkedList<Integer> l1=new LinkedList<Integer>();
            l=t.map.get(b);
            if(l!=null){
               Collections.sort(l); //sort the lists of positions of term
               int x=l.size()-1;
               while(x>0)
               {
                  int no=l.get(x)-l.get(x-1);
                  l.removeLast();         //after sorting apply delta encoding and make a new list
                  l1.addFirst(no);
                  x--;
               }
               int no=l.removeLast();      //when x=0
               l1.addFirst(no);
               t.map.replace(b, l1);
            }
            b++;
            }
             a++;
        } 
     
    }
}
