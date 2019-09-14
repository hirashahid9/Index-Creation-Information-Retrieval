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
public class termDetailsWithoutHash {
    
    int tId;
    int termFreq;
    int docFreq;
    HashMap<Integer,LinkedList<Integer>> map=new HashMap<Integer,LinkedList<Integer>>();
    LinkedList<docDetails> document=new LinkedList<docDetails>();
     
    public class docInfo{
    int doc;
    LinkedList<Integer> positions=new LinkedList<Integer>();
    
    public void printDocDetails()
    {
       System.out.print(doc);
       System.out.print("   ");
       System.out.print(positions);
    
    }
    }
    
}
