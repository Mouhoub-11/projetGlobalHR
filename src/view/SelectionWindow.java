package view;

import javax.swing.*;
import java.awt.event.*;

public class SelectionWindow extends JFrame {
    public SelectionWindow() {
        super("Sélection d'action");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JButton addButton = new JButton("Ajouter un employé");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AddEmployeeWindow().setVisible(true);
            }
        });
        
        JButton deleteButton = new JButton("Supprimer un employé");
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DeleteEmployeeWindow().setVisible(true);
            }
        });
        
        JPanel panel = new JPanel();
        panel.add(addButton);
        panel.add(deleteButton);
        
        add(panel);
        setSize(600, 300);
        setLocationRelativeTo(null);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new SelectionWindow().setVisible(true);
            }
        });
    }
}
