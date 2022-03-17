package view;

import model.SelectionPolicy;

import java.awt.Color;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.event.ActionListener;


public class SimulationSetupView extends JFrame {
    private final JTextField clientField;
    private final JTextField queueField;
    private final JTextField simulationTimeField;
    private final JTextField minArrivalTimeField;
    private final JTextField maxArrivalTimeField;
    private final JTextField minProcessingTimeField;
    private final JTextField maxProcessingTimeField;
    private final JComboBox<SelectionPolicy> policyComboBox;
    private final JButton submitButton;

    public SimulationSetupView() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Queue Simulator");
        setBounds(100, 100, 550, 270);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(47, 189, 252));

        JLabel clientLabel = new JLabel("Number of clients:");
        clientLabel.setBounds(10, 53, 105, 25);
        contentPane.add(clientLabel);

        JLabel queueLabel = new JLabel("Number of queues:");
        queueLabel.setBounds(10, 99, 120, 25);
        contentPane.add(queueLabel);

        JLabel simulationTimeLabel = new JLabel("Maximum simulation time:");
        simulationTimeLabel.setBounds(10, 145, 150, 25);
        contentPane.add(simulationTimeLabel);

        clientField = new JTextField();
        clientField.setBorder(null);
        clientField.setBounds(119, 56, 30, 20);
        clientField.setBackground(new Color(47, 216, 252));
        contentPane.add(clientField);

        queueField = new JTextField();
        queueField.setBorder(null);
        queueField.setBounds(124, 102, 30, 20);
        queueField.setBackground(new Color(47, 216, 252));
        contentPane.add(queueField);

        simulationTimeField = new JTextField();
        simulationTimeField.setBorder(null);
        simulationTimeField.setBounds(159, 148, 30, 20);
        simulationTimeField.setBackground(new Color(47, 216, 252));
        contentPane.add(simulationTimeField);

        JLabel titleLabel = new JLabel("Queue Simulator");
        titleLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(20, 10, 480, 25);
        contentPane.add(titleLabel);

        JLabel arrivalLabel = new JLabel("Arrival time between:");
        arrivalLabel.setBounds(275, 53, 130, 25);
        contentPane.add(arrivalLabel);

        minArrivalTimeField = new JTextField();
        minArrivalTimeField.setBorder(null);
        minArrivalTimeField.setBounds(400, 56, 30, 20);
        minArrivalTimeField.setBackground(new Color(47, 216, 252));
        contentPane.add(minArrivalTimeField);

        JLabel andLabel = new JLabel("and");
        andLabel.setBounds(435, 53, 45, 25);
        contentPane.add(andLabel);

        maxArrivalTimeField = new JTextField();
        maxArrivalTimeField.setBorder(null);
        maxArrivalTimeField.setBounds(461, 56, 30, 20);
        maxArrivalTimeField.setBackground(new Color(47, 216, 252));
        contentPane.add(maxArrivalTimeField);

        JLabel processingLabel = new JLabel("Processing time between:");
        processingLabel.setBounds(275, 99, 150, 25);
        contentPane.add(processingLabel);

        minProcessingTimeField = new JTextField();
        minProcessingTimeField.setBorder(null);
        minProcessingTimeField.setBounds(427, 102, 30, 20);
        minProcessingTimeField.setBackground(new Color(47, 216, 252));
        contentPane.add(minProcessingTimeField);

        JLabel andLabel2 = new JLabel("and");
        andLabel2.setBounds(461, 99, 45, 25);
        contentPane.add(andLabel2);

        maxProcessingTimeField = new JTextField();
        maxProcessingTimeField.setBorder(null);
        maxProcessingTimeField.setBounds(485, 102, 30, 20);
        maxProcessingTimeField.setBackground(new Color(47, 216, 252));
        contentPane.add(maxProcessingTimeField);

        JLabel policyLabel = new JLabel("Distribution policy:");
        policyLabel.setBounds(275, 145, 130, 25);
        contentPane.add(policyLabel);
        SelectionPolicy[] options = {SelectionPolicy.SHORTEST_TIME, SelectionPolicy.SHORTEST_QUEUE};

        policyComboBox = new JComboBox<>(options);
        policyComboBox.setBounds(385, 148, 145, 20);
        policyComboBox.setBackground(new Color(47, 216, 252));
        policyComboBox.setFocusable(false);
        contentPane.add(policyComboBox);

        submitButton = new JButton("Submit");
        submitButton.setBounds(210, 190, 80, 20);
        submitButton.setBackground(new Color(47, 216, 252));
        contentPane.add(submitButton);
    }

    public void addSubmitListener(ActionListener asl) {
        submitButton.addActionListener(asl);
    }

    public String getNumberOfClients() {
        return clientField.getText();
    }

    public String getNumberOfQueues() {
        return queueField.getText();
    }

    public String getSimulationTime() {
        return simulationTimeField.getText();
    }

    public String getMinArrivalTime() {
        return minArrivalTimeField.getText();
    }

    public String getMaxArrivalTime() {
        return maxArrivalTimeField.getText();
    }

    public String getMinProcessingTime() {
        return minProcessingTimeField.getText();
    }

    public String getMaxProcessingTime() {
        return maxProcessingTimeField.getText();
    }

    public SelectionPolicy getPolicy() {
        return (SelectionPolicy) policyComboBox.getSelectedItem();
    }

    public void showError(String errorMessage) {
        JOptionPane.showMessageDialog(this, errorMessage, "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}
