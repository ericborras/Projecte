
package org.milaifontanals;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import org.milaifontanals.persistence.Persistencia;


public class gestioUsuarisProjectes{

    
    private gestioUsuarisProjectes nm;
    
    private UI ui;
    
    
    
    
    
    public static void main(String[] args) {
          
        gestioUsuarisProjectes nm = new gestioUsuarisProjectes();
        
        nm.gui();    
    }
    
    private void gui(){
        ui = new UI();
    }
    
    



    
}
