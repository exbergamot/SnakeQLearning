package snake.swing.tabs;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataPanel extends JPanel{

    private static Map<String, List<String>> fieldMap = new HashMap<>();

    public static final String LEARNING_CONFIGURATION = "Learning configuration";

    public static final String ALGORITHM = "Algorithm";

    public static final String RL_CONFIGURATION = "RL configuration";

    public static final String LAYER_TYPE = "Layer type";

    public static final String LAYER_CONFIGURATION = "Layer configuration";

    public static final String NETWORK_STRUCTURE = "Network structure";

    public static final String NETWORK_STRUCTURE_TO_LAYERS = "Network structure to layers";

    static{
        List<String> learningConfigurationList = new ArrayList<>();
        learningConfigurationList.add("id");
        learningConfigurationList.add("max epoch step");
        learningConfigurationList.add("max step");
        learningConfigurationList.add("experience replay max size");
        learningConfigurationList.add("batch size");
        learningConfigurationList.add("target update frequency");
        learningConfigurationList.add("update start");
        learningConfigurationList.add("reward factor");
        learningConfigurationList.add("gamma");
        learningConfigurationList.add("error clamp");
        learningConfigurationList.add("min epsilon");
        learningConfigurationList.add("epsilon anneal step");
        learningConfigurationList.add("max threads");
        learningConfigurationList.add("regularization");
        learningConfigurationList.add("learning rate");
        fieldMap.put(LEARNING_CONFIGURATION, learningConfigurationList);

        List<String> algorithm = new ArrayList<>();
        algorithm.add("id");
        algorithm.add("name");
        fieldMap.put(ALGORITHM, algorithm);

        List<String> rlConfiguration = new ArrayList<>();
        rlConfiguration.add("id");
        rlConfiguration.add("algorithm");
        rlConfiguration.add("learning configuration");
        rlConfiguration.add("network structure");
        fieldMap.put(RL_CONFIGURATION, rlConfiguration);

        List<String> layerType = new ArrayList<>();
        layerType.add("id");
        layerType.add("name");
        fieldMap.put(LAYER_TYPE, layerType);

        List<String> layerConfiguration = new ArrayList<>();
        layerConfiguration.add("id");
        layerConfiguration.add("layer type");
        layerConfiguration.add("kernel size");
        layerConfiguration.add("stride");
        layerConfiguration.add("padding");
        layerConfiguration.add("output count");
        fieldMap.put(LAYER_CONFIGURATION, layerConfiguration);

        List<String> networkStructureLayerConfig = new ArrayList<>();
        networkStructureLayerConfig.add("network structure id");
        networkStructureLayerConfig.add("layer configuration id");
        networkStructureLayerConfig.add("sequence");
        fieldMap.put(NETWORK_STRUCTURE_TO_LAYERS, networkStructureLayerConfig);

        List<String> networkStructure = new ArrayList<>();
        networkStructure.add("id");
        networkStructure.add("name");
        fieldMap.put(NETWORK_STRUCTURE, networkStructure);
    }

    public DataPanel(String type, String[] existingElements) {
        this.setLayout(null);
        List<String> fields = fieldMap.get(type);

        JLabel topLabel = new JLabel(type);
        topLabel.setBounds(10,10,150, 10);
        this.add(topLabel);

        JList list = new JList();
        list.setListData(existingElements);
        list.setBounds(10,25,150, (fields.size() - 1) * 40 + 20);
        this.add(list);


        for (int i = 0; i < fields.size(); i++) {
            JLabel fieldLable = new JLabel(fields.get(i) + ":");
            JTextField fieldInput = new JTextField();

            this.add(fieldLable);
            this.add(fieldInput);
            fieldLable.setBounds(200, 10 + i * 40,200,10);
            fieldInput.setBounds(200, 25 + i * 40, 200, 20);
        }

        JButton create = new JButton("Create");
        JButton update = new JButton("Update");
        JButton delete = new JButton("Delete");

        create.setBounds(40, fields.size() * 40 + 30, 100, 20);
        update.setBounds(40 + 120, fields.size() * 40 + 30, 100, 20);
        delete.setBounds(40 + 240, fields.size() * 40 + 30, 100, 20);

        this.add(create);
        this.add(update);
        this.add(delete);

        this.setSize(410,fields.size() * 40 + 60);
        this.setBounds(0,0,410, fields.size() * 40 + 60);
        this.setPreferredSize(new Dimension(410, fields.size() * 40 + 60));
    }
}
