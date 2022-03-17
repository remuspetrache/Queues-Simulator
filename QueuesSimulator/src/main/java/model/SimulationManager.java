package model;

import view.SimulationLogView;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class SimulationManager implements Runnable {
    private final int timeLimit;
    private final int maxProcessingTime;
    private final int minProcessingTime;
    private final int numberOfClients;
    private final int minArrivalTime;
    private final int maxArrivalTime;
    private SelectionPolicy selectionPolicy;
    private final Scheduler scheduler;
    private int totalWaitingTime;
    private int maxNumberOfClients;
    private final FileWriter fileWriter;
    private final SimulationLogView logView;
    private List<Task> generatedTasks;

    public SimulationManager(int timeLimit, int minProcessingTime, int maxProcessingTime, int minArrivalTime, int maxArrivalTime,
                             int numberOfClients, int numberOfServers, SelectionPolicy selectionPolicy, SimulationLogView logView) throws IOException {
        this.timeLimit = timeLimit;
        this.selectionPolicy = selectionPolicy;
        this.numberOfClients = numberOfClients;
        this.minArrivalTime = minArrivalTime;
        this.maxArrivalTime = maxArrivalTime;
        this.minProcessingTime = minProcessingTime;
        this.maxProcessingTime = maxProcessingTime;
        this.scheduler = new Scheduler(numberOfServers, numberOfClients);
        this.scheduler.changeStrategy(this.selectionPolicy);
        this.logView = logView;
        this.fileWriter = new FileWriter("LogOfEvents.txt");
        this.totalWaitingTime = 0;
        this.maxNumberOfClients = 0;
        this.generateNRandomTasks();
    }

    private void generateNRandomTasks() {
        generatedTasks = new ArrayList<>(numberOfClients);
        Random r = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int processingTime = minProcessingTime + r.nextInt(maxProcessingTime - minProcessingTime + 1);
            int arrivalTime = minArrivalTime + r.nextInt(maxArrivalTime - minArrivalTime + 1);
            Task t = new Task(i + 1, arrivalTime, processingTime);
            generatedTasks.add(t);
        }
        Collections.sort(generatedTasks);
    }

    private int getMaxWaitingTime() {
        if (scheduler.getServers().isEmpty()) {
            return 0;
        }
        int max = scheduler.getServers().get(0).getWaitingPeriod().intValue();
        for (Server s : scheduler.getServers()) {
            if (max < s.getWaitingPeriod().intValue()) {
                max = s.getWaitingPeriod().intValue();
            }
        }
        return max;
    }

    public void computeTotalWaitTime() {
        for (Server s : scheduler.getServers()) {
            totalWaitingTime += s.getTasks().size();
        }
    }

    public int getTotalServiceTime() {
        int totalServiceTime = 0;
        for (Task t : generatedTasks) {
            totalServiceTime += t.getProcessingTime();
        }
        return totalServiceTime;
    }

    public boolean isPeakHour() {
        int clientsInServers = 0;
        for (Server s : scheduler.getServers()) {
            clientsInServers += s.getTasks().size();
        }
        if (clientsInServers > maxNumberOfClients) {
            maxNumberOfClients = clientsInServers;
            return true;
        }
        return false;
    }

    public void decrementProcessingTime() {
        for (Server s : scheduler.getServers()) {
            if (s.getTasks().peek() != null) {
                s.getTasks().peek().setProcessingTime(s.getTasks().peek().getProcessingTime() - 1);
                if (s.getTasks().peek().getProcessingTime() == 0) {
                    s.getTasks().poll();
                }
            }
        }
    }

    public int getProcessedClients() {
        int nrProcessedClients = numberOfClients;
        if (!generatedTasks.isEmpty()) {
            nrProcessedClients -= generatedTasks.size();
        }
        return nrProcessedClients;
    }

    public int computeTotalServiceTime(int totalServiceTime, int maxWaitingTime) {
        int finalServiceTime = totalServiceTime;
        if (!generatedTasks.isEmpty()) {
            for (Task t : generatedTasks) {
                finalServiceTime -= t.getProcessingTime();
            }
        }
        if (maxWaitingTime > 0) {
            for (Server s : scheduler.getServers()) {
                for (Task t : s.getTasks()) {
                    finalServiceTime -= t.getProcessingTime();
                }
            }
        }
        return finalServiceTime;
    }

    public void endSimulation(int totalServiceTime, int maxWaitingTime, int peakHour) {
        for (Server s : scheduler.getServers()) {
            s.setActive(false);
        }
        int nrProcessedClients = getProcessedClients();
        totalServiceTime = computeTotalServiceTime(totalServiceTime, maxWaitingTime);
        float averageWaitingTime = (nrProcessedClients > 0) ? totalWaitingTime / (float) nrProcessedClients : 0;
        float averageServiceTime = (nrProcessedClients > 0) ? totalServiceTime / (float) nrProcessedClients : 0;
        String endingDisplay = "Average waiting time: " + String.format("%.2f", averageWaitingTime) + "\n" +
                "Average service time: " + String.format("%.2f", averageServiceTime) + "\n" +
                "Peak hour at time: " + peakHour;
        writeDisplay(endingDisplay);
        try {
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getDisplay(int currentTime) {
        String result = "Time: " + currentTime + "\n";
        result += "Waiting clients: ";
        for (Task t : generatedTasks) {
            result += t.toString() + "; ";
        }
        result = result.substring(0, result.length() - 2);
        result += "\n" + scheduler.toString() + "\n";
        return result;
    }

    public void writeDisplay(String display) {
        try {
            fileWriter.write(display);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logView.setText(display);
    }

    @Override
    public void run() {
        int currentTime = 0;
        int maxWaitingTime = getMaxWaitingTime();
        int totalServiceTime = getTotalServiceTime();
        int peakHour = 0;
        while (currentTime <= timeLimit && (!generatedTasks.isEmpty() || maxWaitingTime > 0)) {
            while (!generatedTasks.isEmpty() && generatedTasks.get(0).getArrivalTime() == currentTime) {
                scheduler.dispatchTask(generatedTasks.get(0));
                generatedTasks.remove(0);
            }
            writeDisplay(getDisplay(currentTime));
            if (isPeakHour()) {
                peakHour = currentTime;
            }
            computeTotalWaitTime();
            decrementProcessingTime();
            currentTime++;
            maxWaitingTime = getMaxWaitingTime();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        endSimulation(totalServiceTime, maxWaitingTime, peakHour);
    }
}
