package model;

import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private final List<Server> servers;
    private int maxNoServers;
    private int maxTasksPerServer;
    private Strategy strategy;


    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        this.servers = new ArrayList<>();
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        for (int i = 0; i < maxNoServers; i++) {
            this.servers.add(new Server(maxTasksPerServer));
            Thread t = new Thread(servers.get(i));
            t.start();
        }
    }


    public void changeStrategy(SelectionPolicy policy) {
        if (policy == SelectionPolicy.SHORTEST_QUEUE) {
            strategy = new ConcreteStrategyQueue();
        }
        if (policy == SelectionPolicy.SHORTEST_TIME) {
            strategy = new ConcreteStrategyTime();
        }
    }

    public void dispatchTask(Task t) {
        strategy.addTask(this.servers, t);
    }

    public List<Server> getServers() {
        return servers;
    }

    public String toString() {
        String result = "";
        int i = 0;
        for (Server s : servers) {
            result += "Queue " + (++i) + ": " + s.toString() + "\n";
        }
        return result;
    }
}
