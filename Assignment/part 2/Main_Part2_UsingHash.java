
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

public class Main {

  
    //public static HashMap<Integer, termDetails> invertedIndex = new HashMap<Integer, termDetails>();
    
    public static void main(String[] args) throws Exception{
        
        String filePath=args[0];   //pass path of the corpus as argument
        File tmp=new File(filePath);
        if(tmp.exists()){
        //Part1_SourceCode.listFiles(filePath);
        Part2_UsingHashMap.listFiles(filePath);
        
        }
        else {
          System.out.print("File does not exist");
        }
        
    }
    
}
