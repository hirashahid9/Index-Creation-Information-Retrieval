/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.*;
/**
 *
 * @author hiras
 */
public abstract class sortByTerm implements Comparator<ListDetails> {
    public int compareTo(ListDetails l1, ListDetails l2){
    if(l1.tID>l2.tID)
    {
        return 1;  
    }
    else if(l1.tID<l2.tID)
    {
        return -1;
    }
    else
    {
        if(l1.dID>l2.dID)
        {
          return 1;
        }
        else if(l1.dID<l2.dID)
        {
            return -1;
        }
    }
return 0;
}
 
}
