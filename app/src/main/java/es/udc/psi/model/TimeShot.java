package es.udc.psi.model;

public class TimeShot {
    private int minutes;
    private int hours;
    private int seconds;

    public TimeShot() {}

    public TimeShot(int hours, int minutes)
    {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = 0;
    }

    public TimeShot(int hours, int minutes, int seconds)
    {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    // Getters
    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    // Setters
    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    // Methods

    @Override
    public String toString()
    {
        return String.format("%02d:%02d", hours, minutes);
    }
}
