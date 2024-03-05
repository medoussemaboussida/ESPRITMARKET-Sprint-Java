/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo4.test;

import com.example.demo4.entities.evenement;
import com.example.demo4.entities.participant;

import java.sql.Date;
import java.sql.SQLException;
import com.example.demo4.services.evenementService;
import com.example.demo4.services.participantService;


/**
 *
 * @author asus
 */
public class test {
    
      public static void main(String[] args) {   
          
          Date d=Date.valueOf("2022-06-11");
          Date d1=Date.valueOf("2020-04-12");
        try {
            //kifeh ya9ra el orde fel base de donnée , kifeh 3raf nom abonn bch n3amarha f nom 
            evenement e = new evenement(2,10,"nom21", "type21","image21.png","description21",d1);
            evenement e1 = new evenement(5,"nom3", "type3","image3.png","description3",d);
            evenement e2 = new evenement(6,"nom4", "type4","image4.png","description4",d);
            evenement e3 = new evenement(5,8,"nom5", "type5","image5.png","description5",d);
            
            
            participant p=new participant(d,1,2);
            participant p1=new participant(d1,3,4);
            participant p2=new participant(27,d1,55,45);

            participantService ps=new participantService();
            //ps.participant(p);
          //  ps.participant(p1);
           // ps.participant(p2);
            ps.modifierparticipant(p2);
            //ps.participant(p2);
            System.out.println("");
            evenementService ab = new evenementService();
            //ab.ajouterevenement(e1);
            //ab.ajouterevenement(e2);
           // ab.ajouterevenement(e3);
            //ab.ajouter(p);
            //ab.modifierevenement(e);
            //ab.supprimerevenement(e3);
            System.out.println(ab.recupererevenement());
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
}
