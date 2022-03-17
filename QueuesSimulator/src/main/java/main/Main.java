package main;

import controller.Controller;
import view.SimulationSetupView;

public class Main {
    public static void main(String[] args) {
        SimulationSetupView mainView = new SimulationSetupView();
        Controller controller = new Controller(mainView);
        mainView.setVisible(true);
    }
}
