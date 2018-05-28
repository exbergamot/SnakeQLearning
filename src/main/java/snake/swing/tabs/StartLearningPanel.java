package snake.swing.tabs;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class StartLearningPanel extends JPanel {

    public StartLearningPanel() {
        this.setLayout(null);

        JLabel rlConfigLabel = new JLabel("Select RL configuration id:");
        rlConfigLabel.setBounds(10,10,200, 10);
        this.add(rlConfigLabel);

        JComboBox selectId = new JComboBox(new String[]{"12", "13"});
        selectId.setSelectedIndex(1);
        selectId.setBounds(10,25,200, 20);
        this.add(selectId);

        JLabel fileLabel = new JLabel("Select file to save network:");
        fileLabel.setBounds(10,60,200, 10);
        this.add(fileLabel);

        JFileChooser fieldInput = new JFileChooser();
        fieldInput.setControlButtonsAreShown(false);
        fieldInput.addChoosableFileFilter(new FileNameExtensionFilter("Neural network", "nn"));
        fieldInput.setBounds(10,70,500, 300);
        this.add(fieldInput);

        JButton start = new JButton("Start");
        start.setBounds(10, 380, 100, 20);
        this.add(start);

        this.setSize(550,410);
        this.setBounds(0,0,550, 410);
        this.setPreferredSize(new Dimension(550, 410));
    }
}