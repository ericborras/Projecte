
package org.milaifontanals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.milaifontanals.model.Usuari;
import org.milaifontanals.persistence.Persistencia;


public class UI {
    
    
    //Buttons
    private GestioButton gb;
    private GestioLlista gll;
    
    private JFrame contenidor_principal;
    
    
    //Usuaris
    private JPanel panell_usuaris,usuaris_botons,usuaris_botons2,usuaris_text_field;
    private JLabel titol_usuaris;
    private JButton btn_nou_usuari,btn_cancel_usuari,btn_esborra_usuari,btn_guarda_usuari;
    private JTextField text_nom_usuari,text_cognom1_usuari,text_cognom2_usuari,text_login_usuari,text_passwd,text_naix_usuari;
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
    private JLabel titol_assignats,usuari_assignats,rol_assignats;
    private JComboBox<String> combo_usuari_assignats;
    private JComboBox<String> combo_rol_assignats;
    private JList llista_per_assignar,llista_assignats;
    private JButton btn_assigna,btn_treure;
    private JScrollPane scroll_pane_assingar, scroll_pane_assignats;
    
    
    private DefaultListModel usuaris = new DefaultListModel();
    
    //Persistència
    private Persistencia pers_mysql;
    private EntityManager em = null;
    
    
    
    
    
    private boolean modeAlta = false;
    private boolean modeEdit = false;
    
    //USUARI QUE SERÀ EL SELECCIONAT DE LA JLIST
    Usuari usuari = null;
    private int day,month,year;
    
    public UI(){ 
        pers_mysql = new Persistencia();
        
        em = pers_mysql.obrir_connexio();
        
        
        if(em==null){
            //Mostrar pantalla error
            gui_error();
        }else{
            usuaris = pers_mysql.mostrar_usuaris();
            gui();
            
            
        }
    }
        
    
    private void gui_error() {
        
        JFrame main = new JFrame("Gestió usuaris");
        JOptionPane.showMessageDialog(main, "Error crític","No s'ha pogut establir la connexió",JOptionPane.ERROR_MESSAGE);
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.exit(0);
    }
    
    private void gui(){
        gb = new GestioButton();
        gll = new GestioLlista();
        
        contenidor_principal = new JFrame("Gestió usuaris");
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
        
        
    }
    
    
    private void disseny_center() {
        
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
        
        rol_assignats = new JLabel("Rol");
        panell_vert.add(rol_assignats);
        
        combo_rol_assignats =new JComboBox<String>();
        combo_rol_assignats.setBounds(10,10,80,20);
        panell_vert.add(combo_rol_assignats);
        
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
        panell_assignats.add(Box.createVerticalStrut(300));
        
        contenidor_principal.add(panell_assignats, BorderLayout.CENTER);
        
        
        
        
        
    }

    private void d() {
        
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
        
        JLabel label_nom_usuari = new JLabel("Nombre usuario");
        JLabel label_cognom1_usuari = new JLabel("Primer apellido");
        JLabel label_cognom2_usuari = new JLabel("Segundo apellido");
        JLabel label_login_usuari = new JLabel("Login");
        JLabel label_passwdHash_usuari = new JLabel("Password");
        
        text_nom_usuari = new JTextField("Nombre usuario",10);
        
        text_cognom1_usuari = new JTextField("Primer apellido",10);
        text_cognom2_usuari = new JTextField("Segundo apellido",10);
        text_login_usuari = new JTextField("Login",10);
        text_passwd = new JTextField("Password",10);
        
       

        
        
        
        usuaris_text_field = new JPanel();
        
        usuaris_text_field.add(label_nom_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_nom_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        

        usuaris_text_field.add(label_cognom1_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_cognom1_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        
        usuaris_text_field.add(label_cognom2_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_cognom2_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        
        usuaris_text_field.add(label_login_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_login_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        
        usuaris_text_field.add(label_passwdHash_usuari);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(text_passwd);
        usuaris_text_field.setLayout(new BoxLayout(usuaris_text_field, BoxLayout.Y_AXIS));
        
        JButton data_naix = new JButton("Fecha nacimiento");
        data_naix.setName("data_naix");
        data_naix.addActionListener(gb);
        
        usuaris_text_field.add(Box.createVerticalStrut(5));
        usuaris_text_field.add(data_naix);
        usuaris_text_field.add(Box.createVerticalStrut(5));
        text_naix_usuari = new JTextField();
        text_naix_usuari.setEditable(false);
        usuaris_text_field.add(text_naix_usuari);
        
        panell_usuaris.add(usuaris_text_field);
        panell_usuaris.add(Box.createHorizontalStrut(20));
        
        llista_usuaris = new JList(usuaris);
        //AFEGIR EL LISTER A LA LLISTA
        llista_usuaris.addListSelectionListener(gll);
        
        
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
        panell_usuaris.add(Box.createVerticalStrut(300));
        
        contenidor_principal.add(panell_usuaris, BorderLayout.NORTH);
    }

    private void persistencia() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private class GestioLlista implements ListSelectionListener{

        
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            
            if(!e.getValueIsAdjusting()){
                System.out.println(llista_usuaris.getSelectedValue().toString()); 
                usuari = (Usuari) llista_usuaris.getSelectedValue();
                emplena_camps();
                btn_esborra_usuari.setEnabled(true);
                
            }
            
            
            
        }

        private void emplena_camps() {
            //text_nom_usuari,text_cognom1_usuari,text_cognom2_usuari,text_login_usuari,text_passwd;
            text_nom_usuari.setText(usuari.getNom());
            text_cognom1_usuari.setText(usuari.getCognom1());
            if(usuari.getCognom2()!=null){
                text_cognom2_usuari.setText(usuari.getCognom2());
            }else{
                text_cognom2_usuari.setText("");
            }
            text_login_usuari.setText(usuari.getLogin());
            text_passwd.setText(usuari.getPasswdHash());
            
            //usuaris_text_field
            Calendar cal = new GregorianCalendar();
            cal.setTime(usuari.getDataNaixement());

            text_naix_usuari.setText(cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.MONTH)+"/"+cal.get(Calendar.YEAR));
            //Camp data
            
        }
    }



    



    
    
    private class GestioButton implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
           
            JButton b = (JButton) e.getSource();
            
            
            if(b.getName().equals("nou_usuari")){
                afegir_usuari();
            }else if(b.getName().equals("cancelar_usuari")){
                System.out.println(b.getName());
            }else if(b.getName().equals("eliminar_usuari")){
                eliminar_usuari();
            }else if(b.getName().equals("guardar_usuari")){
                guardar_usuari();
            }else if(b.getName().equals("assignar")){
                System.out.println(b.getName());
            }else if(b.getName().equals("quitar")){
                System.out.println(b.getName());
            }else if(b.getName().equals("data_naix")){
                //INVESTIGAR COM POSAR UN NOU JFRAME A TRAVÉS DE UN BUTTON
                JPanel panell_datepicker = new JPanel();
                datepicker_usuari = new DatePicker(panell_datepicker);
                year = datepicker_usuari.year;
                month = datepicker_usuari.month+1;
                day = Integer.parseInt(datepicker_usuari.day);
                
                text_naix_usuari.setText(day+"/"+month+"/"+year);
                
                
            }
            

           
        }

        private void afegir_usuari() {
            modeAlta = true;
            modeEdit = false;
            
            //Netejar textfields
            text_nom_usuari.setText("");
            text_cognom1_usuari.setText("");
            text_cognom2_usuari.setText("");
            text_login_usuari.setText("");
            text_passwd.setText("");
            text_naix_usuari.setText("");
            
            
            //Treure seleccionat de la llista i amagar el button
            btn_esborra_usuari.setEnabled(false);
            llista_usuaris.clearSelection();
            
            
            
            
            
            
            
        }

        private void guardar_usuari() {

            if(modeAlta){
                if(text_nom_usuari.getText().length()>0 && text_cognom1_usuari.getText().length()>0 && text_login_usuari.getText().length()>0 
                && text_passwd.getText().length()>0 && text_naix_usuari.getText().length()>0)
                {
                    //Construir objecte usuari
                    Usuari u = new Usuari(text_nom_usuari.getText(),text_cognom1_usuari.getText(),new Date(),text_login_usuari.getText(),text_passwd.getText());
                    if(text_cognom2_usuari.getText().length()>0){
                        u.setCognom2(text_cognom2_usuari.getText());
                    }
                    
                    try{
                        em.getTransaction().begin();
                        em.persist(u);
                        em.getTransaction().commit();
                        
                        //Eliminar de la ModelList
                        //usuaris.remove(llista_usuaris.)
                        
                        JOptionPane.showMessageDialog(contenidor_principal,"Usuario guardado con éxito","Guardar",JOptionPane.INFORMATION_MESSAGE);
                    }catch(Exception ex){
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(contenidor_principal,"No se ha podido guardar el usuario","Error al guardar",JOptionPane.ERROR_MESSAGE);
                    }
                    
                    
                    

                }else{
                    JOptionPane.showMessageDialog(contenidor_principal,"Nombre, el primer apellido, login, password y fecha nacimiento son obligatorios","Error al guardar",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                //MODE UPDATE
                if(usuari!=null){
                    
                }
                
                
                
                
            }

            
            
            
            
        }

        private void eliminar_usuari() {
            
            //Esborrar usuari
            try{
                
                Object[] options = {"Si","No"};
                    int n = JOptionPane.showOptionDialog(contenidor_principal,
                                                        "Estás segur que quieres eliminar el usuario "+usuari+"?", 
                                                        "Eliminar", 
                                                        JOptionPane.YES_NO_OPTION, 
                                                        JOptionPane.QUESTION_MESSAGE,
                                                        null,
                                                        options, 
                                                        null);
                    if(n==0){
                        
                        int pos = llista_usuaris.getSelectedIndex();
                        
                        
                        System.out.println("SI");
                        em.getTransaction().begin();           
                        em.remove(usuari);
                        em.getTransaction().commit();
                        
                        
                        
                        
                        JOptionPane.showMessageDialog(contenidor_principal,"Usuario eliminado con éxito","Eliminar usuario",JOptionPane.INFORMATION_MESSAGE);
                    }else if(n==1){
                        System.out.println("NO");
                    }
                

                
                //Treure de la llista
                
                
            }catch(Exception ex){
                JOptionPane.showMessageDialog(contenidor_principal,"No se ha podido eliminar el usuario","Error al eliminar el usuario",JOptionPane.ERROR_MESSAGE);
            }
            
            
            
        }
    
    }
    
    
    
}
