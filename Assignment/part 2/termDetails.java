/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author hiras
 */
public class termDetails {
    
     public int termFrequency;
    public int docFrequency;
    LinkedList<docDetails> document=new LinkedList<docDetails>();
    //HashMap<Integer,ArrayList<Integer>> map=new HashMap<Integer,ArrayList<Integer>>();
    HashMap<Integer,LinkedList<Integer>> map=new HashMap<Integer,LinkedList<Integer>>();
    
    public void printTermDetails(){
       
       System.out.print(termFrequency);
       System.out.print("\t");
       System.out.print(docFrequency);
       System.out.print("\t");
       System.out.println(map);
    
    }
    
}
class docDetails {
int docId;
LinkedList<Integer> listOfPositions=new LinkedList<Integer>();
}