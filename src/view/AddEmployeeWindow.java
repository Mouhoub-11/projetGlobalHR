package view;

import javax.swing.*;
import java.awt.*;

public class AddEmployeeWindow extends JFrame {
    public AddEmployeeWindow() {
        super("Ajouter un employé");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Ferme cette fenêtre sans quitter l'application principale
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5); // Marge
        
        // Labels à gauche, champs de texte à droite
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nom :"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Prénom :"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Âge :"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Salaire de l'employé :"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("Téléphone de l'employé :"), gbc);
        gbc.gridy++;
        panel.add(new JLabel("ID de l'entreprise :"), gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        panel.add(new JTextField(10), gbc);
        gbc.gridy++;
        panel.add(new JTextField(10), gbc);
        gbc.gridy++;
        panel.add(new JTextField(10), gbc);
        gbc.gridy++;
        panel.add(new JTextField(10), gbc);
        gbc.gridy++;
        panel.add(new JTextField(10), gbc);
        gbc.gridy++;
        panel.add(new JTextField(10), gbc);
        
        // Bouton Ajouter centré
        gbc.gridx = 0;
        gbc.gridwidth = 2; // Étendre sur 2 colonnes
        gbc.gridy++;
        gbc.anchor = GridBagConstraints.CENTER; // Alignement au centre
        JButton addButton = new JButton("Ajouter");
        panel.add(addButton, gbc);
        
        add(panel);
        setSize(800, 600);

        setLocationRelativeTo(null);
    }
}
