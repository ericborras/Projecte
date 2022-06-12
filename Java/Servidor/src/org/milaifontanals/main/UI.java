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
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Properties;
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
import org.milaifontanals.IPersistence;
import org.milaifontanals.PersistenceException;
import org.milaifontanals.exception.ServidorException;
import static org.milaifontanals.main.TipusOperacio.GET_LOGIN;
import org.milaifontanals.model.Entrada;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.Tasca;
import org.milaifontanals.model.Usuari;

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
    
    
    private IPersistence capa_pers;
    private String arxiu;
    
    private Thread thread;
    private ServerSocket socket_connections;
    
    private static final int port = 44444;
    
    public UI(String nomFitxer){
        arxiu = nomFitxer;
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(nomFitxer));
            String nom_capa = props.getProperty("capa");

            // des de fitxer de propietats o via args. Mai per codi
            capa_pers = (IPersistence) Class.forName(nom_capa).newInstance();
            System.out.println("Capa carregada i en funcionament");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ex) {
            System.out.println("No es pot carregar capa per motiu.....");
            return;
        } catch (PersistenceException ex) {
            System.out.println(ex.getMessage());
            return;
        } catch (IOException ex) {
            System.out.println("No es pot trobar el fitxer de propietats.....");
            return;
        }     
               
        
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
        
        llista_log = new JList(capa_pers.getLog());
        
        
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
                capa_pers.tancaConnexio();
                btn_atura.setEnabled(false);
                btn_engega.setEnabled(true);
                thread.stop();
                
            }else if(button.getName().equals("engega")){
                
                thread = new Thread(new Runnable(){
                    @Override
                    public void run() {
                        System.out.println("engega");
                        capa_pers.obrir_connexio(arxiu);
                        btn_atura.setEnabled(true);
                        btn_engega.setEnabled(false);


                        try (ServerSocket serverSocket = new ServerSocket(port)) {

                            System.out.println("Server is listening on port " + port);

                            while (true) {
                                Socket socket = serverSocket.accept();
                                System.out.println("New client connected");

                                new ServerThread(socket,arxiu).start();
                            }

                        } catch (IOException ex) {
                            System.out.println("Server exception: " + ex.getMessage());
                            ex.printStackTrace();
                        }
                    
                    }
                });
                thread.start();
     
        }


    }
    
    }   
}