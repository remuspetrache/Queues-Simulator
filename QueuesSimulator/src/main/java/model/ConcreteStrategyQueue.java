package model;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = servers.get(0).getClientsInServer().intValue();
        for (Server s : servers) {
            if (s.getClientsInServer().intValue() < min) {
                min = s.getClientsInServer().intValue();
            }
        }
        for (Server s : servers) {
            if (s.getClientsInServer().intValue() == min) {
                s.addTask(t);
                break;
            }
        }
    }
}
