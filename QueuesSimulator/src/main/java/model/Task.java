package model;

public class Task implements Comparable<Task> {
    private int arrivalTime;
    private int processingTime;
    private int idNumber;

    public Task(int idNumber, int arrivalTime, int processingTime) {
        this.idNumber = idNumber;
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(int processingTime) {
        this.processingTime = processingTime;
    }

    @Override
    public int compareTo(Task o) {
        return arrivalTime - o.arrivalTime;
    }

    public String toString() {
        return "(" + idNumber + "," + arrivalTime + "," + processingTime + ")";
    }
}
