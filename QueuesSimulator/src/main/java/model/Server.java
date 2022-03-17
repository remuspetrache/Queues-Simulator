package model;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private final BlockingQueue<Task> tasks;
    private final AtomicInteger waitingPeriod;
    private final AtomicInteger clientsInServer;
    private boolean isActive;

    public Server(int maxTasksPerServer) {
        this.tasks = new ArrayBlockingQueue<>(maxTasksPerServer);
        this.waitingPeriod = new AtomicInteger(0);
        this.clientsInServer = new AtomicInteger(0);
        this.isActive = true;
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }


    public AtomicInteger getClientsInServer() {
        return clientsInServer;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
        waitingPeriod.addAndGet(newTask.getProcessingTime());
        clientsInServer.incrementAndGet();
    }

    @Override
    public void run() {
        while (isActive) {
            while (!tasks.isEmpty()) {
                try {
                    int currentProcessingTime = tasks.peek().getProcessingTime();
                    for (int i = 0; i < currentProcessingTime; i++) {
                        Thread.sleep(1000);
                        waitingPeriod.decrementAndGet();
                    }
                    clientsInServer.decrementAndGet();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String toString() {
        String result = "";
        if (tasks.isEmpty() || tasks.peek().toString().length() == 0) {
            return "closed";
        } else {
            for (Task t : tasks) {
                result += t.toString() + "; ";
            }
        }
        if (result.length() == 0) {
            return "closed";
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }
}
