package snake.swing.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DataMenuActionListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        e.getSource();
        System.out.println(e);
    }
}
