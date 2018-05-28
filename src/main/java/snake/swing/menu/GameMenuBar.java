package snake.swing.menu;

import javax.swing.*;

public class GameMenuBar extends JMenuBar {

    public GameMenuBar() {
        JMenu mainMenu = new JMenu("Main");
        JMenuItem startManual = new JMenuItem("Start manual game");
        JMenuItem startLearning = new JMenuItem("Start learning");
        JMenuItem runLearned = new JMenuItem("Run learned network");
        JMenuItem exit = new JMenuItem("Exit");
        mainMenu.add(startManual);
        mainMenu.add(startLearning);
        mainMenu.add(runLearned);
        mainMenu.add(exit);
        this.add(mainMenu);

        JMenu dataMenu = new JMenu("Data");
        JMenuItem algorithm = new JMenuItem("Algorithm");
        JMenuItem rlConfiguration = new JMenuItem("RL configuration");
        JMenuItem learningConfiguration = new JMenuItem("Learning configuration");
        JMenuItem layerType = new JMenuItem("Layer type");
        JMenuItem layerConfiguration = new JMenuItem("Layer configuration");
        JMenuItem networkStructure = new JMenuItem("Network structure");
        JMenuItem networkStructureLayerConfig = new JMenuItem("Network structure to layers");
        dataMenu.add(algorithm);
        dataMenu.add(rlConfiguration);
        dataMenu.add(learningConfiguration);
        dataMenu.add(layerType);
        dataMenu.add(layerConfiguration);
        dataMenu.add(networkStructure);
        dataMenu.add(networkStructureLayerConfig);
        this.add(dataMenu);

        JMenu chartsMenu = new JMenu("Charts");
        JMenuItem scoreChart = new JMenuItem("Score chart");
        JMenuItem errorChart = new JMenuItem("MSE chart");
        JMenuItem speedChart = new JMenuItem("Learning speed chart");
        chartsMenu.add(scoreChart);
        chartsMenu.add(errorChart);
        chartsMenu.add(speedChart);
        this.add(chartsMenu);

    }

}
