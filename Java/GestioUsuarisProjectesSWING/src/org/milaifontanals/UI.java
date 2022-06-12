
package org.milaifontanals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import static org.milaifontanals.MD5Utils.bytesToHex;
import org.milaifontanals.capa.CapaMYSQL;
import org.milaifontanals.model.Projecte;
import org.milaifontanals.model.ProjecteUsuari;
import org.milaifontanals.model.Rol;
import org.milaifontanals.model.Usuari;


public class UI {
    
    
    //Buttons
    private GestioButton gb;
    private GestioLlista gll;
    private GestioCombo gc;
    
    private JFrame contenidor_principal;
    
    
    //Usuaris
    private JPanel panell_usuaris,usuaris_botons,usuaris_botons2,usuaris_text_field;
    private JLabel titol_usuaris;
    private JButton btn_nou_usuari,btn_cancel_usuari,btn_esborra_usuari,btn_guarda_usuari;
    private JTextField text_nom_usuari,text_cognom1_usuari,text_cognom2_usuari,text_login_usuari,text_passwd,text_naix_usuari;
    private DatePicker datepicker_usuari;
    
    private JList llista_usuaris;
    private JScrollPane scroll_pane_usuaris;
    
    
    private Projecte[] projectes;
    private Rol[] rols;
    private String[] s_rols;
    
    
    //Projectes assignats a l'usuari
    private JPanel panell_assignats,panell_guarda;
    private JLabel titol_assignats,usuari_assignats,rol_assignats;
    private JComboBox combo_projectes;
    private JComboBox combo_rols;
    JList llista_per_assignar,llista_assignats;
    private JButton btn_assigna,btn_treure,btn_guarda;
    private JScrollPane scroll_pane_assingar, scroll_pane_assignats;
    
    
    private DefaultListModel usuaris = new DefaultListModel();
    private DefaultListModel usuaris_assignats_projecte = null;
    private DefaultListModel usuaris_assignar_projecte = null;
    
    //Persistència
    private IPersistence pers_mysql;
    //private EntityManager em = null;
    
    
    
    
    
    private boolean modeAlta = false;
    private boolean modeEdit = false;
    private boolean rol_selected = false;
    private boolean llista_assignar_selected = false;
    
    //USUARI QUE SERÀ EL SELECCIONAT DE LA JLIST
    Usuari usuari = null;
    //PROJECTE QUE SERÀ EL SELECCIONAT DEL JCOMBOBOX
    Projecte projecte = null;
    //USUARI QUE SERÀ EL SELECCIONAT DE LA JLIST QUE NO ESTAN ASSIGNATS AL PROJECTE SELECCIONAT
    Usuari usuari_pendent_assignar = null;
    //USUARI QUE SERÀ EL SELECCIONAT DE LA JLIST QUE ESTAN ASSIGNATS AL PROJECTE SELECCIONAT
    Usuari usuari_assignat = null;
    
    //LLISTA QUE CONTÉ EL PROJECTE-USUARI QUE CLICA EL COMBO
    //LA ANIREM MODIFICANT EN MEMÒRIA I ANIRÀ A LA BASE DE DADES
    List<ProjecteUsuari> projectes_usuaris = null;
    
    List<ProjecteUsuari> projectes_usuaris_per_esborrar = null;
    List<ProjecteUsuari> projectes_usuari_per_afegir = null;
    
    List<Integer> llista_ids_projectes =null;

    private int idx_llista_usuaris;
    private int day,month,year;
    
    public UI(String nomFitxer){ 
        try {
            Properties props = new Properties();
            props.load(new FileInputStream(nomFitxer));
            String nom_capa = props.getProperty("capa");

            // des de fitxer de propietats o via args. Mai per codi
            pers_mysql = (IPersistence) Class.forName(nom_capa).newInstance();
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
        
        try{
            pers_mysql.obrir_connexio(nomFitxer);
        }catch(Exception ex){
            gui_error();
        }
        
        
        //Per defecte està en mode edit
        modeEdit = true;

        usuaris = pers_mysql.mostrar_usuaris();
        projectes = pers_mysql.mostrar_projectes();
        rols = pers_mysql.mostrar_rols();
        s_rols = new String[rols.length];
        for(int i=0;i<rols.length;i++){
            s_rols[i] = rols[i].getNom();
        }
        gui();
           
           
            
        
    }
        
    
    private void gui_error() {
        
        JFrame main = new JFrame("Gestió usuaris");
        JOptionPane.showMessageDialog(main, "Error crític","No s'ha pogut establir la connexió",JOptionPane.ERROR_MESSAGE);
        //main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        System.exit(0);
    }
    
    private void gui(){
        gb = new GestioButton();
        gll = new GestioLlista();
        gc = new GestioCombo();
        usuaris_assignats_projecte = new DefaultListModel();
        usuaris_assignar_projecte = new DefaultListModel();
        projectes_usuaris = new ArrayList();
        projectes_usuaris_per_esborrar = new ArrayList();
        projectes_usuari_per_afegir = new ArrayList();
        llista_ids_projectes = new ArrayList();
        
        contenidor_principal = new JFrame("Gestió usuaris");
        contenidor_principal.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
       
        disseny_usuaris();
        
        
        contenidor_principal.setVisible(true);
        contenidor_principal.setResizable(false);
        contenidor_principal.setSize(1100,700);
        
        contenidor_principal.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                int dialogButton = JOptionPane.showConfirmDialog (contenidor_principal, "Seguro que quieres cerrar la aplicación?","ALERTA",JOptionPane.YES_NO_OPTION);

                switch(dialogButton){
                    case JOptionPane.YES_OPTION:
                        pers_mysql.tancaConnexio();
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        break;
                }
                
                
            }
        });
       
        
        
    }

    //Contenidor OEST, que conté els ususaris
    private void disseny_usuaris() {
       
        disseny_north();
 
        disseny_center();
        
        disseny_south();
        
        
    }
    
    
    private void disseny_center() {
        
        panell_assignats = new JPanel(new FlowLayout());
        JPanel panell_vert = new JPanel();     
        titol_assignats = new JLabel("Proyectos assignados");
        titol_assignats.add(Box.createHorizontalStrut(-200));
        titol_assignats.setFont(new Font(titol_assignats.getName(), Font.BOLD, 25));
        panell_vert.add(titol_assignats);
        
        
        
        usuari_assignats = new JLabel("Proyecto");
        panell_vert.add(usuari_assignats);
        panell_vert.setLayout(new BoxLayout(panell_vert, BoxLayout.Y_AXIS));
        
        
        combo_projectes=new JComboBox(new DefaultComboBoxModel(projectes));  
        combo_projectes.addItemListener(gc);
        combo_projectes.setName("combo_projectes");
        combo_projectes.setMaximumSize(combo_projectes.getPreferredSize());
        combo_projectes.setSelectedIndex(-1);
        panell_vert.add(combo_projectes);
        
        rol_assignats = new JLabel("Rol");
        panell_vert.add(rol_assignats);
        
        combo_rols =new JComboBox(new DefaultComboBoxModel(s_rols));
        combo_rols.setName("combo_rols");
        combo_rols.addItemListener(gc);
        combo_rols.setMaximumSize(combo_rols.getPreferredSize());
        combo_rols.setSelectedIndex(-1);
        panell_vert.add(combo_rols);       
        
        panell_assignats.add(panell_vert);
        panell_assignats.add(Box.createHorizontalStrut(20));
        
        llista_per_assignar = new JList(usuaris_assignar_projecte);
        llista_per_assignar.setName("llista_per_assignar");
        llista_per_assignar.addListSelectionListener(gll);
        
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
        
        llista_assignats = new JList(usuaris_assignats_projecte);
        llista_assignats.setName("llista_assignats");
        llista_assignats.addListSelectionListener(gll);
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
        llista_usuaris.setName("llista_usuaris");
        //AFEGIR EL LISTENER A LA LLISTA
        llista_usuaris.addListSelectionListener(gll);
        
        
        Dimension listSize = new Dimension(300, 300);
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

    private void disseny_south() {
        panell_guarda = new JPanel();
        btn_guarda = new JButton("Guardar");
        btn_guarda.setName("guarda");
        btn_guarda.addActionListener(gb);
        
        panell_guarda.add(btn_guarda);
        
        contenidor_principal.add(panell_guarda,BorderLayout.SOUTH);
    }


    private class GestioLlista implements ListSelectionListener{

        
        
        @Override
        public void valueChanged(ListSelectionEvent e) {
            
            JList llista = (JList) e.getSource();
            
            if(llista.getName().equals("llista_usuaris")){
                if(!e.getValueIsAdjusting()){
                    System.out.println(llista_usuaris.getSelectedValue().toString()); 
                    usuari = (Usuari) llista_usuaris.getSelectedValue();
                    emplena_camps();
                    btn_esborra_usuari.setEnabled(true);
                    idx_llista_usuaris = llista_usuaris.getSelectedIndex();
                    System.out.println("IDX: "+idx_llista_usuaris);
                    modeEdit = true;
                }
            }else if(llista.getName().equals("llista_per_assignar")){
                if(!e.getValueIsAdjusting()){
                    rol_selected = false;
                    llista_assignar_selected = true;                    

                    btn_assigna.setEnabled(llista_assignar_selected && rol_selected);
                    usuari_pendent_assignar = (Usuari) llista_per_assignar.getSelectedValue();
                    
                    combo_rols.setSelectedIndex(-1);
                    
                }               
            }else if(llista.getName().equals("llista_assignats")){
                if(!e.getValueIsAdjusting()){
                    btn_treure.setEnabled(true);
                    
                    usuari_assignat = (Usuari) llista_assignats.getSelectedValue();
                    
                    if(usuari_assignat!=null){
                        //Mostrar el rol de l'usuari seleccionat
                        for(ProjecteUsuari pru : projectes_usuaris){
                            if(usuari_assignat.equals(pru.getUsuari())){
                                System.out.println("ENTRO!!!"+pru.getRol().getNom());

                                combo_rols.setSelectedItem(pru.getRol().getNom());
                            }
                        }
                    }

                    
                }
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
                neteja_inputs();
            }else if(b.getName().equals("cancelar_usuari")){
                cancelar_usuari();
            }else if(b.getName().equals("eliminar_usuari")){
                eliminar_usuari();
            }else if(b.getName().equals("guardar_usuari")){
                guardar_usuari();
            }else if(b.getName().equals("assignar")){
                assignar_usuari_projecte();
            }else if(b.getName().equals("quitar")){
                treure_usuari_projecte();
            }else if(b.getName().equals("data_naix")){
                //INVESTIGAR COM POSAR UN NOU JFRAME A TRAVÉS DE UN BUTTON
                JPanel panell_datepicker = new JPanel();
                datepicker_usuari = new DatePicker(panell_datepicker);
                year = datepicker_usuari.year;
                month = datepicker_usuari.month+1;
                day = Integer.parseInt(datepicker_usuari.day);
                
                text_naix_usuari.setText(day+"/"+month+"/"+year);
                
                
            }else if(b.getName().equals("guarda")){
                guardar_usuari_projecte();
            }
            

           
        }

        private void neteja_inputs() {
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
                if(text_nom_usuari.getText().trim().length()>0 && text_cognom1_usuari.getText().trim().length()>0 && text_login_usuari.getText().trim().length()>0 
                && text_passwd.getText().trim().length()>0 && text_naix_usuari.getText().length()>0)
                {
                    //Construir objecte usuari
                    
                    
                    Date data_naix = new GregorianCalendar(year, month, day).getTime();
                    
                    //Encriptar 
                    MD5Utils md5 = new MD5Utils();
                    byte[] md5InBytes = md5.digest(text_passwd.getText().getBytes(StandardCharsets.UTF_8));
                    String hash = bytesToHex(md5InBytes);

                   
                   
                    
                    
                    
                    Usuari u = null;
                    
                    try{
                        u = new Usuari(text_nom_usuari.getText(),text_cognom1_usuari.getText(),data_naix,text_login_usuari.getText(),hash);
                    
                        if(text_cognom2_usuari.getText().length()>0){
                           u.setCognom2(text_cognom2_usuari.getText());
                        }

                        try{

                           pers_mysql.comensaTransaccio();
                           pers_mysql.guardaCanvis(u);
                           pers_mysql.commit();
                           pers_mysql.neteja();

                           /*
                           em.getTransaction().begin();                      
                           em.persist(u);
                           em.getTransaction().commit();
                           em.clear();
                           */
                           try{
                               //Guardar a la modelLlist
                               usuaris.addElement(u);
                           }catch(Exception ex){

                           }

                           JOptionPane.showMessageDialog(contenidor_principal,"Usuario guardado con éxito","Guardar",JOptionPane.INFORMATION_MESSAGE);
                        }catch(Exception ex){
                            System.out.println(ex.getMessage());
                            JOptionPane.showMessageDialog(contenidor_principal,"No se ha podido guardar el usuario","Error al guardar",JOptionPane.ERROR_MESSAGE);
                            pers_mysql.rollback();
                        }
                    
                    
                    }catch(RuntimeException ex){
                        JOptionPane.showMessageDialog(contenidor_principal,ex.getMessage(),"Error al guardar",JOptionPane.ERROR_MESSAGE);
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(contenidor_principal,"Nombre, el primer apellido, login, password y fecha nacimiento son obligatorios","Error al guardar",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                //MODE UPDATE
                if(usuari!=null){
                    if(text_nom_usuari.getText().trim().length()>0 && text_cognom1_usuari.getText().trim().length()>0 && text_login_usuari.getText().trim().length()>0 
                    && text_passwd.getText().trim().length()>0 && text_naix_usuari.getText().length()>0)
                    {
                        usuari.setNom(text_nom_usuari.getText());
                        usuari.setCognom1(text_cognom1_usuari.getText());
                        if(text_cognom2_usuari.getText().length()>0){
                            usuari.setCognom2(text_cognom2_usuari.getText());
                        }
                        usuari.setLogin(text_login_usuari.getText());
                        usuari.setPasswdHash(text_passwd.getText());
                        Date data_naix = new GregorianCalendar(year, month, day).getTime();
                        usuari.setDataNaixement(data_naix);
                        
                        
                        
                    try{
                        
                        pers_mysql.comensaTransaccio();
                        pers_mysql.sobreEscriureCanvis(usuari);
                        pers_mysql.commit();
                        pers_mysql.neteja();
                        /*
                        em.getTransaction().begin();
                        em.merge(usuari);
                        em.getTransaction().commit();
                        em.clear();
                        */
                        try{
                            //Guardar a la modelLlist
                            //usuaris.addElement(u);
                        }catch(Exception ex){
                        
                        }
                        
                        JOptionPane.showMessageDialog(contenidor_principal,"Usuario modificado con éxito","Modificar",JOptionPane.INFORMATION_MESSAGE);
                    }catch(Exception ex){
                        pers_mysql.rollback();
                        System.out.println(ex.getMessage());
                        JOptionPane.showMessageDialog(contenidor_principal,"Error al modificar el usuario","Error al modificar",JOptionPane.ERROR_MESSAGE);
                    }
                        
                    }
                    
                    
                    
                    
                }
                
                
                
                
            }

            
            
            
            
        }
        
        private void eliminar_usuari() {
            
            //Esborrar usuari
            boolean merge=false;
            try{
                
                Object[] options = {"Sí","No"};
                    int n = JOptionPane.showOptionDialog(contenidor_principal,
                                                        "Estás seguro que quieres eliminar el usuario "+usuari+"?", 
                                                        "Eliminar", 
                                                        JOptionPane.YES_NO_OPTION, 
                                                        JOptionPane.QUESTION_MESSAGE,
                                                        null,
                                                        options, 
                                                        null);
                    if(n==0){                      
                        System.out.println("SI");
                        
                        pers_mysql.comensaTransaccio();                       
                        pers_mysql.esborrar(usuari);
                        pers_mysql.commit();
                        /*
                        em.getTransaction().begin();
                        System.out.println(usuari);                       
                        em.remove(usuari);
                        
                        em.getTransaction().commit();
                        */
                        
                        //Treure de la llista
                        //Esborrar en memoria
                        try{

                            System.out.println("SIZE INICIAL: "+usuaris.size());
                            usuaris.remove(idx_llista_usuaris);
                            System.out.println("SIZE FINAL: "+usuaris.size());
                            
                        }catch(Exception ex){
                            
                            System.out.println(ex.getMessage());
                            
                        }
                        neteja_inputs();
                        JOptionPane.showMessageDialog(contenidor_principal,"Usuario eliminado con éxito","Eliminar usuario",JOptionPane.INFORMATION_MESSAGE);
                    }else if(n==1){
                        System.out.println("NO");
                    }
              

                
            }catch(Exception ex){
                merge = true;
                pers_mysql.rollback();
                //em.getTransaction().rollback();             
                System.out.println(ex.getMessage());
                
            }
            
            if(merge){
                try{
                    /*
                    em.getTransaction().begin();
                    em.remove(em.contains(usuari) ? usuari : em.merge(usuari));
                    em.getTransaction().commit();
                    */
                    pers_mysql.comensaTransaccio();
                    pers_mysql.modificarOEsborrar(usuari);
                    pers_mysql.commit();
                    
                    //Treure de la llista
                    //Esborrar en memoria
                    try{

                        System.out.println("SIZE INICIAL: "+usuaris.size());
                        usuaris.remove(idx_llista_usuaris);
                        System.out.println("SIZE FINAL: "+usuaris.size());

                    }catch(Exception ex){

                        System.out.println(ex.getMessage());

                    }
                    neteja_inputs();
                    
                    JOptionPane.showMessageDialog(contenidor_principal,"Usuario eliminado con éxito","Eliminar usuario",JOptionPane.INFORMATION_MESSAGE);
                }catch(Exception ex){
                    pers_mysql.rollback();
                    JOptionPane.showMessageDialog(contenidor_principal,"No se ha podido eliminar el usuario","Error al eliminar el usuario",JOptionPane.ERROR_MESSAGE);
                }

                
            }
            

            
            
            
        }

        private void cancelar_usuari() {
            modeEdit = true;
            modeAlta = false;
            
            //En cas que tingui un usuari seleccionat previament, posarem les seves dades inicials
            if(usuari!=null){
                gll.emplena_camps();
                
                
            }
            
            
        }

        private void assignar_usuari_projecte() {

            String nom_rol = combo_rols.getSelectedItem().toString();
            Rol r = null;
            for(int i=0;i<rols.length;i++){
                if(rols[i].getNom().equals(nom_rol)){
                    r = rols[i];
                }
            }
            //Quan s'assigna, afegim a la llista projectes_usuaris amb el ROL que ha escollit
                       
            if(r!=null){
                ProjecteUsuari pru = new ProjecteUsuari(projecte, (Usuari) llista_per_assignar.getSelectedValue(),r);
                projectes_usuaris.add(pru);
                System.out.println(pru);
                System.out.println("MIDA DESPRÉS D'AFEGIR: "+projectes_usuaris.size());
                projectes_usuari_per_afegir.add(pru);
            }
        
            usuaris_assignats_projecte.add(0, llista_per_assignar.getSelectedValue());
            usuaris_assignar_projecte.remove(llista_per_assignar.getSelectedIndex());
            

            
            llista_per_assignar.setSelectedIndex(-1);
            btn_assigna.setEnabled(false);
        }

        private void treure_usuari_projecte() {
            
            //Quan s'esborra, treiem a la llista projectes_usuaris 
            
            String nom_rol = combo_rols.getSelectedItem().toString();
            Rol r = null;
            for(int i=0;i<rols.length;i++){
                if(rols[i].getNom().equals(nom_rol)){
                    r = rols[i];
                }
            }
            //Quan s'assigna, afegim a la llista projectes_usuaris amb el ROL que ha escollit
                       
            if(r!=null){
                Usuari aux = (Usuari) llista_assignats.getSelectedValue();
                for(ProjecteUsuari pr : projectes_usuaris){
                    if(pr.getUsuari().getLogin().equals(aux.getLogin())){
                        //Ens guardem id del projecte
                        try{
                            llista_ids_projectes.add(pr.getId());
                            projectes_usuaris_per_esborrar.add(new ProjecteUsuari(null,null,null));
                        }catch(Exception ex){
                            System.out.println("HOLA?");
                            System.out.println(llista_ids_projectes.size());
                        }
                        
                    }
                }
                
                
                
                usuaris_assignar_projecte.add(0, llista_assignats.getSelectedValue());
                usuaris_assignats_projecte.remove(llista_assignats.getSelectedIndex());
            }
            
            

            
            llista_assignats.setSelectedIndex(-1);
            btn_treure.setEnabled(false);
        }

        private void guardar_usuari_projecte() {
                     
            try{
                        
                //Esborrem els que estan seleccionats
                //em.getTransaction().begin(); 
                pers_mysql.comensaTransaccio();
                System.out.println("PROJECTES USUARIS PER ESBORRAR: "+projectes_usuaris_per_esborrar.size());
                int qt=0;
                for(ProjecteUsuari pru : projectes_usuaris_per_esborrar){                                                   
                    ProjecteUsuari pru_find = pers_mysql.find(ProjecteUsuari.class, llista_ids_projectes.get(qt));
                    
                    try{
                       pers_mysql.esborrar(pru_find);
                       //em.remove(pru_find); 
                    }catch(Exception ex){
                        pers_mysql.sobreEscriureCanvis(pru_find);
                        pers_mysql.esborrar(pru);
                        //em.merge(pru_find);
                        //em.remove(pru);                     
                    }
                    qt++;
                }
                
                projectes_usuaris_per_esborrar.clear();
                
                pers_mysql.commit();
                pers_mysql.neteja();
                //em.getTransaction().commit();
                //em.clear();

                //---------------------------------------
                pers_mysql.comensaTransaccio();
                //em.getTransaction().begin(); 
                //Afegim
                for(ProjecteUsuari pru : projectes_usuari_per_afegir){
                    pers_mysql.guardaCanvis(pru);
                    pers_mysql.flush();
                    //em.persist(pru);
                    //em.flush();

                }
                System.out.println("PROJECTES USUARI PER AFEGIR: "+projectes_usuari_per_afegir.size());
                projectes_usuari_per_afegir.clear();
                pers_mysql.commit();
                pers_mysql.neteja();
                //em.getTransaction().commit();
                //em.clear();
                
                llista_ids_projectes.clear();

                JOptionPane.showMessageDialog(contenidor_principal,"Assignación de usarios realizada con éxito","Modificar",JOptionPane.INFORMATION_MESSAGE);
            }catch(Exception ex){
                pers_mysql.rollback();
                System.out.println(ex.getMessage());
                JOptionPane.showMessageDialog(contenidor_principal,"Error al modificar las asignaciones de usuarios","Error al modificar",JOptionPane.ERROR_MESSAGE);
            }
            
        }
    
    }
    
    class GestioCombo implements ItemListener{

        @Override
        public void itemStateChanged(ItemEvent e) {
            
            JComboBox combo = (JComboBox) e.getSource();
            
            if(combo.getName().equals("combo_projectes")){
                if (e.getStateChange() == ItemEvent.SELECTED){
                    projecte = (Projecte) e.getItem();

                    usuaris_assignats_projecte = pers_mysql.mostrar_usuarsisPerProjecte(projecte.getId());
                    llista_assignats.setModel(usuaris_assignats_projecte);

                    usuaris_assignar_projecte = pers_mysql.mostrar_usuarsisPerProjectePendent(projecte.getId());
                    llista_per_assignar.setModel(usuaris_assignar_projecte);

                    projectes_usuaris = pers_mysql.mostrar_projecteUsuari(projecte.getId());
                }
            }else if(combo.getName().equals("combo_rols")){
                if(e.getStateChange() == ItemEvent.SELECTED){
                    rol_selected = true;

                    btn_assigna.setEnabled(llista_assignar_selected && rol_selected);
                }
            }
            
            

           
            
        }


    
    
    }
    
    
    
}
