/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.persist.CPGestioProjecte;

/**
 *
 * @author Lenovo T530
 */
public class UI {
    
    private GestioButtons gb;
    
    private JFrame contenidor_principal;
    private JPanel panell_buttons;
    private JButton btn_engega,btn_atura;
    
    private JList llista_log;
    private JScrollPane scroll_pane_log;
    
    
    private CPGestioProjecte capa_pers;
    private String arxiu = "connexioMySQL.properties";
    
    
    public UI(){
        capa_pers = new CPGestioProjecte();
               
        
        contenidor_principal = new JFrame("Servidor");       
        gb = new GestioButtons();
        panell_buttons = new JPanel();
        
        btn_engega = new JButton("Engega");
        btn_engega.setName("engega");
        btn_engega.addActionListener(gb);
        
        btn_atura = new JButton("Atura");
        btn_atura.setName("atura");
        btn_atura.setEnabled(false);
        btn_atura.addActionListener(gb);
        
        panell_buttons.add(btn_engega);
        panell_buttons.add(btn_atura);
        
        llista_log = new JList(capa_pers.llista_log);
        
        
        Dimension listSize = new Dimension(200, 200);
        //Assignem la mida i fem que es mostri la llista       
        
        scroll_pane_log = new JScrollPane(llista_log);
        
        scroll_pane_log.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_log.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_pane_log.setMaximumSize(listSize);
        scroll_pane_log.setPreferredSize(listSize);
        scroll_pane_log.setSize(listSize);
        
        contenidor_principal.add(scroll_pane_log,BorderLayout.CENTER);
        
        
        contenidor_principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contenidor_principal.add(panell_buttons,BorderLayout.NORTH);       
        contenidor_principal.setVisible(true);
        contenidor_principal.setSize(750,500);
        
                        
        
        
             

    }

    private class GestioButtons implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent b) {
            
            JButton button = (JButton) b.getSource();
            
            if(button.getName().equals("atura")){
                System.out.println("atura");               
                capa_pers.close();
                btn_atura.setEnabled(false);
                btn_engega.setEnabled(true);
            }else if(button.getName().equals("engega")){
                System.out.println("engega");
                capa_pers.connect(arxiu);
                btn_atura.setEnabled(true);
                btn_engega.setEnabled(false);
                System.out.println(capa_pers.getLogin("Marisela","1234"));
                List<Projecte> projectes = capa_pers.getProjectes(10);
                for(Projecte p : projectes){
                    System.out.println(p+" CAP PROJECTE: "+p.getCapProjecte());
                }
                List<Tasca> tasques = capa_pers.getTasques(3);
                for(Tasca t : tasques){
                    System.out.println(t+" PROPIETARI: "+t.getPropietari()+" ESTAT: "+t.getEstat()+" RESPONSABLE"+t.getResponsable());
                }
            }
            
        }


    }
    
    
}
