
package org.milaifontanals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public class gestioUsuarisProjectes {

    private JFrame contenidor_principal;
    
    
    //Usuaris
    private JPanel panell_usuaris,usuaris_botons,usuaris_botons2,usuaris_text_field;
    private JLabel titol_usuaris;
    private JButton btn_nou_usuari,btn_cancel_usuari,btn_esborra_usuari,btn_guarda_usuari;
    private JTextField text_nom_usuari,text_cognom1_usuari,text_cognom2_usuari,text_login_usuari,text_passwd;
    private DatePicker datepicker_usuari;
    
    private JList llista_usuaris;
    private JScrollPane scroll_pane_usuaris;
    
    
    //Projectes
    private JPanel panell_projectes, projectes_botons,projectes_botons2,projectes_text_field;
    private JLabel titol_projectes,titol_cap_projecte;
    private JButton btn_nou_projecte,btn_cancel_projecte,btn_esborra_projecte,btn_guarda_projecte;
    private JTextField text_nom_projecte;
    private JTextArea text_descripcio_projecte;
    
    private JList llista_projectes;
    private JScrollPane scroll_pane_projectes;
    
    private JComboBox<String> combo_cap_projecte;
    
    
    //Projectes assignats a l'usuari
    private JPanel panell_assignats,panell_assignats_botons;
    private JLabel titol_assignats,usuari_assignats;
    private JComboBox<String> combo_usuari_assignats;
    private JList llista_per_assignar,llista_assignats;
    private JButton btn_assigna,btn_treure;
    private JScrollPane scroll_pane_assingar, scroll_pane_assignats;
    
    
    
    //Buttons
    private GestioButton gb;
    
    public static void main(String[] args) {
          
        gestioUsuarisProjectes nm = new gestioUsuarisProjectes();
        
        
        nm.gui();
        
        
        
        
    }
    
    
    private void gui(){
        gb = new GestioButton();
        contenidor_principal = new JFrame("Grups sanguinis compatibles");
        contenidor_principal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
       
        disseny_usuaris();
        
        
        contenidor_principal.setVisible(true);
        contenidor_principal.setResizable(false);
        contenidor_principal.setSize(950,700);
        
        
    }

    //Contenidor OEST, que conté els ususaris
    private void disseny_usuaris() {
       
        disseny_north();
        
        disseny_center();
        
        disseny_south();
        
        
    }
    
    
    private void disseny_south() {
        
        panell_assignats = new JPanel(new FlowLayout());
        JPanel panell_vert = new JPanel();
        panell_assignats.add(Box.createHorizontalStrut(-40));
        titol_assignats = new JLabel("Proyectos assignados");
        titol_assignats.setFont(new Font(titol_assignats.getName(), Font.BOLD, 25));
        panell_vert.add(titol_assignats);
        
        
        
        usuari_assignats = new JLabel("Proyecto");
        panell_vert.add(usuari_assignats);
        panell_vert.setLayout(new BoxLayout(panell_vert, BoxLayout.Y_AXIS));
        
        
        combo_usuari_assignats=new JComboBox<String>();
        combo_usuari_assignats.setBounds(10,10,80,20);
        panell_vert.add(combo_usuari_assignats);
        
        panell_assignats.add(panell_vert);
        panell_assignats.add(Box.createHorizontalStrut(20));
        
        llista_per_assignar = new JList();
        
        Dimension listSize = new Dimension(200, 200);
        //Assignem la mida i fem que es mostri la llista       
        
        scroll_pane_assingar = new JScrollPane(llista_per_assignar);
        
        scroll_pane_assingar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_assingar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_pane_assingar.setMaximumSize(listSize);
        scroll_pane_assingar.setPreferredSize(listSize);
        scroll_pane_assingar.setSize(listSize);
        
        panell_assignats.add(scroll_pane_assingar);
        
        btn_assigna = new JButton("Asignar");
        btn_assigna.setEnabled(false);
        btn_assigna.setName("assignar");
        btn_assigna.addActionListener(gb);
        
        
        panell_assignats.add(Box.createHorizontalStrut(10));
        panell_assignats.add(btn_assigna);
        panell_assignats.add(Box.createHorizontalStrut(10));
        
        llista_assignats = new JList();
        //Assignem la mida i fem que es mostri la llista       
        
        scroll_pane_assignats = new JScrollPane(llista_assignats);
        
        scroll_pane_assignats.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_assignats.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_pane_assignats.setMaximumSize(listSize);
        scroll_pane_assignats.setPreferredSize(listSize);
        scroll_pane_assignats.setSize(listSize);
        
        panell_assignats.add(scroll_pane_assignats);
        
        btn_treure = new JButton("Quitar");
        btn_treure.setEnabled(false);
        btn_treure.setName("quitar");
        btn_treure.addActionListener(gb);
        
        panell_assignats.add(btn_treure);
        
        
        contenidor_principal.add(panell_assignats, BorderLayout.SOUTH);
        
        
        
        
        
    }

    private void disseny_center() {
        
        panell_projectes = new JPanel(new FlowLayout());
        panell_projectes.add(Box.createHorizontalStrut(-100));
        titol_projectes = new JLabel("Proyectos");
        titol_projectes.setFont(new Font(titol_projectes.getName(), Font.BOLD, 25));
        panell_projectes.add(titol_projectes);
        panell_projectes.add(Box.createHorizontalStrut(20));
        
       
        text_nom_projecte = new HintTextField("Nombre proyecto",10);
        text_descripcio_projecte = new HintTextArea("Descripción proyecto");
        Dimension descripcio_size = new Dimension(150, 50);
        text_descripcio_projecte.setMaximumSize(descripcio_size);
        text_descripcio_projecte.setPreferredSize(descripcio_size);
        text_descripcio_projecte.setSize(descripcio_size);
        
        projectes_text_field = new JPanel();
        projectes_text_field.add(text_nom_projecte);
        projectes_text_field.add(Box.createVerticalStrut(5));
        projectes_text_field.add(text_descripcio_projecte);
        
        //titol_cap_projecte = new JLabel("Cap projecte");
        //projectes_text_field.add(titol_cap_projecte);
        projectes_text_field.add(Box.createVerticalStrut(15));
        
        combo_cap_projecte=new JComboBox<String>();
        combo_cap_projecte.setBounds(10,10,80,20);
        projectes_text_field.add(combo_cap_projecte);
        
        projectes_text_field.setLayout(new BoxLayout(projectes_text_field, BoxLayout.Y_AXIS));
        

        
        panell_projectes.add(projectes_text_field);
        panell_projectes.add(Box.createHorizontalStrut(20));
        
        
        llista_projectes = new JList();
        Dimension listSize = new Dimension(300, 200);
        //Assignem la mida i fem que es mostri la llista
     
        
        scroll_pane_projectes = new JScrollPane(llista_projectes);
        
        scroll_pane_projectes.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_projectes.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_pane_projectes.setMaximumSize(listSize);
        scroll_pane_projectes.setPreferredSize(listSize);
        scroll_pane_projectes.setSize(listSize);
        
        panell_projectes.add(scroll_pane_projectes);
        panell_projectes.add(Box.createHorizontalStrut(20)); 
   
        
        btn_nou_projecte = new JButton("Nuevo");
        btn_nou_projecte.setName("nou_projetce");
        btn_nou_projecte.addActionListener(gb);
                
        btn_cancel_projecte = new JButton("Canelar");
        btn_cancel_projecte.setName("cancelar_projecte");
        btn_cancel_projecte.addActionListener(gb);
        
        btn_esborra_projecte = new JButton("Eliminar");
        btn_esborra_projecte.setEnabled(false);
        btn_esborra_projecte.setName("eliminar_projecte");
        btn_esborra_projecte.addActionListener(gb);
        
        btn_guarda_projecte = new JButton("Guardar");
        btn_guarda_projecte.setEnabled(false);
        btn_guarda_projecte.setName("guardar_projecte");
        btn_guarda_projecte.addActionListener(gb);
        
        
        
        projectes_botons = new JPanel();
        projectes_botons.add(btn_nou_projecte);
        projectes_botons.add(Box.createVerticalStrut(10));
        projectes_botons.add(btn_cancel_projecte);
        projectes_botons.add(Box.createVerticalStrut(10));
        projectes_botons.setLayout(new BoxLayout(projectes_botons, BoxLayout.Y_AXIS));
        
        panell_projectes.add(projectes_botons);
        panell_projectes.add(Box.createHorizontalStrut(10));
        
        projectes_botons2 = new JPanel();
    
        projectes_botons2.add(btn_esborra_projecte);
        projectes_botons2.add(Box.createVerticalStrut(10));
        projectes_botons2.add(btn_guarda_projecte);
        projectes_botons2.add(Box.createVerticalStrut(10));
        projectes_botons2.setLayout(new BoxLayout(projectes_botons2, BoxLayout.Y_AXIS));
        

        panell_projectes.add(projectes_botons2);
        
        contenidor_principal.add(panell_projectes, BorderLayout.CENTER);
        
    }
    
    
    private void disseny_north() {
        panell_usuaris = new JPanel(new FlowLayout());
        panell_usuaris.add(Box.createVerticalStrut(200));
        panell_usuaris.add(Box.createHorizontalStrut(0));
        titol_usuaris = new JLabel("Usuarios");
        titol_usuaris.setFont(new Font(titol_usuaris.getName(), Font.BOLD, 25));
        panell_usuaris.add(titol_usuaris);
        panell_usuaris.add(Box.createHorizontalStrut(20));
        
        text_nom_usuari = new HintTextField("Nom usuari",10);
        text_cognom1_usuari = new HintTextField("Cognom 1",10);
        text_cognom2_usuari = new HintTextField("Cognom 2",10);
        text_login_usuari = new HintTextField("login",10);
        text_passwd = new HintTextField("password",10);
        
       

        
        
        
        usuaris_text_field = new JPanel();
        
        usuaris_text_field.add(text_nom_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_cognom1_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_cognom2_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_login_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_passwd);
        usuaris_text_field.setLayout(new BoxLayout(usuaris_text_field, BoxLayout.Y_AXIS));
        
        JButton data_naix = new JButton("Fecha nacimiento");
        data_naix.setName("data_naix");
        data_naix.addActionListener(gb);
        
        usuaris_text_field.add(data_naix);
        
        panell_usuaris.add(usuaris_text_field);
        panell_usuaris.add(Box.createHorizontalStrut(20));
        
        llista_usuaris = new JList();
        Dimension listSize = new Dimension(300, 200);
        //Assignem la mida i fem que es mostri la llista
        
        scroll_pane_usuaris = new JScrollPane(llista_usuaris);
        
        scroll_pane_usuaris.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll_pane_usuaris.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll_pane_usuaris.setMaximumSize(listSize);
        scroll_pane_usuaris.setPreferredSize(listSize);
        scroll_pane_usuaris.setSize(listSize);
        
        panell_usuaris.add(scroll_pane_usuaris);
        
        panell_usuaris.add(Box.createHorizontalStrut(20));
        
        
        btn_nou_usuari = new JButton("Nuevo");
        btn_nou_usuari.setName("nou_usuari");
        btn_nou_usuari.addActionListener(gb);
        
        btn_cancel_usuari = new JButton("Canelar");
        btn_cancel_usuari.setName("cancelar_usuari");
        btn_cancel_usuari.addActionListener(gb);
        
        btn_esborra_usuari = new JButton("Eliminar");       
        btn_esborra_usuari.setEnabled(false);
        btn_esborra_usuari.setName("eliminar_usuari");
        btn_esborra_usuari.addActionListener(gb);
       
        
        btn_guarda_usuari = new JButton("Guardar");
        btn_guarda_usuari.setEnabled(false);
        btn_guarda_usuari.setName("guardar_usuari");
        btn_guarda_usuari.addActionListener(gb);
        
        
        usuaris_botons = new JPanel();
        usuaris_botons.add(btn_nou_usuari);
        usuaris_botons.add(Box.createVerticalStrut(20));
        usuaris_botons.add(btn_cancel_usuari);
        usuaris_botons.setLayout(new BoxLayout(usuaris_botons, BoxLayout.Y_AXIS));
        
        panell_usuaris.add(usuaris_botons);
        panell_usuaris.add(Box.createHorizontalStrut(20));
        
        usuaris_botons2 = new JPanel();
        usuaris_botons2.add(btn_esborra_usuari);
        usuaris_botons2.add(Box.createVerticalStrut(20));
        usuaris_botons2.add(btn_guarda_usuari);
        usuaris_botons2.setLayout(new BoxLayout(usuaris_botons2, BoxLayout.Y_AXIS));
        
        
        panell_usuaris.add(usuaris_botons2);
        
        contenidor_principal.add(panell_usuaris, BorderLayout.NORTH);
    }

    
    
    private class GestioButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           
            JButton b = (JButton) e.getSource();
            
            
            if(b.getName().equals("nou_usuari")){
                System.out.println(b.getName());
            }else if(b.getName().equals("cancelar_usuari")){
                System.out.println(b.getName());
            }else if(b.getName().equals("eliminar_usuari")){
                System.out.println(b.getName());
            }else if(b.getName().equals("guardar_usuari")){
                System.out.println(b.getName());
            }else if(b.getName().equals("nou_projetce")){
                System.out.println(b.getName());
            }else if(b.getName().equals("cancelar_projecte")){
                System.out.println(b.getName());
            }else if(b.getName().equals("eliminar_projecte")){
                System.out.println(b.getName());
            }else if(b.getName().equals("guardar_projecte")){
                System.out.println(b.getName());
            }else if(b.getName().equals("assignar")){
                System.out.println(b.getName());
            }else if(b.getName().equals("quitar")){
                System.out.println(b.getName());
            }else if(b.getName().equals("data_naix")){
                //INVESTIGAR COM POSAR UN NOU JFRAME A TRAVÉS DE UN BUTTON
                JPanel panell_datepicker = new JPanel();
                datepicker_usuari = new DatePicker(panell_datepicker);
            }
            

           
        }
    
    }



    
}
