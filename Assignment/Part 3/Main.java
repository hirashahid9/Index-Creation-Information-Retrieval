
//Hira Shahid
//17L-4090
//CS-A
//The main function is to test source codes and their outputs

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
import java.util.HashMap;
import org.tartarus.snowball.SnowballStemmer;
import org.tartarus.snowball.ext.englishStemmer;

public class Main {

  
    public static void main(String[] args)  throws Exception{

        Part2_UsingHashMap.listFiles("C:\\Users\\hiras\\Desktop\\corpus");

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
         
         if(Part2_UsingHashMap.termids.containsKey(tempStr))
         {
              int id=Part2_UsingHashMap.termids.get(tempStr);
            termDetails t=Part2_UsingHashMap.invertedIndex.get(id);
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
