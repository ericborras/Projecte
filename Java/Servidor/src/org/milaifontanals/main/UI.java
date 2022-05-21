/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.milaifontanals.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.impl.orbutil.ObjectWriter;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import org.milaifontanals.exception.ServidorException;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.persist.CPGestioProjecte;
import org.milaifontanals.persist.DetallTasca;

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
    
    private Thread thread;
    private ServerSocket socket_connections;
    
    private static final int port = 4444;
    
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
                thread.stop();
                
            }else if(button.getName().equals("engega")){
                System.out.println("engega");
                capa_pers.connect(arxiu);
                btn_atura.setEnabled(true);
                btn_engega.setEnabled(false);


                
                
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            socket_connections = new ServerSocket(port);
                        } catch (IOException ex) {
                            Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        while (true) { // TODO: while ! tancar servidor
                            Socket s = null;

                            try {
                                // socket object to receive incoming client requests
                                System.out.println("Esperant clients...");
                                s = socket_connections.accept();

                                System.out.println("Nou client connectat : " + s);

                                /*
                                // obtaining input and out streams
                                ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
                                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());

                                System.out.println("[SRV] Client handlers = " + cs.clientHandlers.size());
                //                System.out.println("[SRV] Assignant ClientHandler per al client");

                                // create a new thread object
                                ClientHandler ch = new ClientHandler(s, ois, oos, cs, cs.dbManager, cs.nomFitxerPropietats);
                                cs.clientHandlers.add(ch);

                                // Invoking the start() method
                                ch.start();
            */

                            } catch (Exception e) {
                                System.out.println("PRIMER TRY CATCH: "+e.getMessage());
                                try {
                                    s.close();
                                } catch (IOException ex) {
                                    System.out.println("SEGON TRY CATCH: "+ex.getMessage());
                                }
                                e.printStackTrace();
                            }



                            List<Tasca> tasques_pendents = capa_pers.getNotificacionsPendents(12);
                            for(Tasca tasca : tasques_pendents){
                                System.out.println(tasca);
                            }

                            //Passar a json
                            ObjectMapper mapper = new ObjectMapper();
                            try{
                                String json = mapper.writeValueAsString(tasques_pendents);
                                System.out.println(json);
                            }catch(Exception ex){
                                throw new ServidorException("Error: ",ex);
                            }

                            //System.out.println(json);
                    }
                            }
                });
                
                try{
                    SwingUtilities.invokeLater(thread);
                }catch(Exception ex){
                    System.out.println("EE ERROR: "+ex.getMessage());
                }
                
                    
                
                
            
        }


    }
    
    }   
}
