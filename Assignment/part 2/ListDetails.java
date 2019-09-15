/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.Comparator;
import java.util.LinkedList;

/**
 *
 * @author hiras
 */
public class ListDetails implements Comparator<ListDetails>{
    
    int tID;
    int dID;
    LinkedList <Integer> listOfpositions=new LinkedList<Integer>();
    int docFreq;
    


 public int compare(ListDetails d, ListDetails d1) {
      
      if(d.tID!=d1.tID){
     return d.tID - d1.tID;
     }
     else
     {
     return d.dID-d1.dID;
     }   
     
   }
 
 public int compareTo(ListDetails d1) {
     if(this.tID!=d1.tID){
     return this.tID - d1.tID;
     }
     else
     {
     return this.dID-d1.dID;
     }   
    }
 
  public String toString() {
        return  "Term: "+ tID + ","+ dID + "\n";
    }

}
    
