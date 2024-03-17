package view;

import javax.swing.*;

public class DeleteEmployeeWindow extends JFrame {
    public DeleteEmployeeWindow() {
        super("Supprimer un employé");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme cette fenêtre sans quitter l'application principale
        
        // Ajoutez ici les composants nécessaires pour supprimer un employé (labels, champs de texte, boutons, etc.)
        // Exemple :
        JPanel panel = new JPanel();
        panel.add(new JLabel("ID de l'employé à supprimer :"));
        panel.add(new JTextField(10));
        panel.add(new JButton("Supprimer"));
        
        add(panel);
        setSize(600, 300);
        setLocationRelativeTo(null);
    }
}
