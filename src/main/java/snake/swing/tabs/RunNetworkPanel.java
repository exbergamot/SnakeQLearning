package snake.swing.tabs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class RunNetworkPanel extends JPanel {

    public RunNetworkPanel() {
        this.setLayout(null);

        JLabel fileLabel = new JLabel("Select file with saved network:");
        fileLabel.setBounds(10,10,200, 10);
        this.add(fileLabel);

        JFileChooser fieldInput = new JFileChooser();
        fieldInput.setControlButtonsAreShown(false);
        fieldInput.addChoosableFileFilter(new FileNameExtensionFilter("Neural network", "nn"));
        fieldInput.setBounds(10,25,500, 300);
        this.add(fieldInput);

        JButton start = new JButton("Start");
        start.setBounds(10, 335, 100, 20);
        this.add(start);

        this.setSize(530,365);
        this.setBounds(0,0,530, 365);
        this.setPreferredSize(new Dimension(530, 365));
    }
}