package controller;

import model.SelectionPolicy;
import model.SimulationManager;
import view.SimulationLogView;
import view.SimulationSetupView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller {
    private SimulationManager manager;
    private int timeLimit;
    private int minProcessingTime;
    private int maxProcessingTime;
    private int minArrivalTime;
    private int maxArrivalTime;
    private int numberOfClients;
    private int numberOfServers;
    private SelectionPolicy policy;

    public Controller(SimulationSetupView setupView) {
        setupView.addSubmitListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getInput(setupView);
                } catch (NumberFormatException nfex) {
                    setupView.showError("Invalid input! Please make sure you have entered the input correctly!");
                    return;
                } catch (Exception ex) {
                    setupView.showError(ex.getMessage());
                    return;
                }
                try {
                    SimulationLogView logView = new SimulationLogView();
                    logView.setVisible(true);
                    manager = new SimulationManager(timeLimit, minProcessingTime, maxProcessingTime, minArrivalTime, maxArrivalTime,
                            numberOfClients, numberOfServers, policy, logView);
                    Thread t = new Thread(manager);
                    t.start();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        });
    }

    private void getInput(SimulationSetupView setupView) throws Exception {
        timeLimit = Integer.parseInt(setupView.getSimulationTime());
        minProcessingTime = Integer.parseInt(setupView.getMinProcessingTime());
        maxProcessingTime = Integer.parseInt(setupView.getMaxProcessingTime());
        minArrivalTime = Integer.parseInt(setupView.getMinArrivalTime());
        maxArrivalTime = Integer.parseInt(setupView.getMaxArrivalTime());
        numberOfClients = Integer.parseInt(setupView.getNumberOfClients());
        numberOfServers = Integer.parseInt(setupView.getNumberOfQueues());
        policy = setupView.getPolicy();
        if (minArrivalTime > maxArrivalTime || minProcessingTime > maxProcessingTime) {
            throw new Exception("Bad input! Minimum value of arrival and processing time should be higher than maximum value!");
        }
    }
}
