package model;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = servers.get(0).getWaitingPeriod().intValue();
        for (Server s : servers) {
            if (s.getWaitingPeriod().intValue() < min) {
                min = s.getWaitingPeriod().intValue();
            }
        }
        for (Server s : servers) {
            if (s.getWaitingPeriod().intValue() == min) {
                s.addTask(t);
                break;
            }
        }
    }
}
