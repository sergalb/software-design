package ru.ifmo.rain.balahnin.aspect;

class ProfilingCharacteristics {
    private int count = 0;
    private long time = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double averageTime() {
        return (double) time / (double) count;
    }

    public void update(long time) {
        this.time += time;
        count++;
    }
}