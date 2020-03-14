package pl.sgeonet.libraries.geigerDetector;

public class GMDetectorResponse {
    private int timeOfMeasurement;
    private long countsTotal;
    private double CPS;
    private double CPM;
    private final double SMB20Ratio = 2.9;
    private double radiationPerHour;

    public int getTimeOfMeasurement() {
        return timeOfMeasurement;
    }

    public void setTimeOfMeasurement(int timeOfMeasurement) {
        this.timeOfMeasurement = timeOfMeasurement;
    }

    public long getCountsTotal() {
        return countsTotal;
    }

    public void setCountsTotal(long countsTotal) {
        this.countsTotal = countsTotal;
        this.CPS = (double) countsTotal / timeOfMeasurement;
        this.radiationPerHour = this.CPS / SMB20Ratio;
        this.CPM = (double) countsTotal / (60.0 / timeOfMeasurement);
    }

    public double getCPS() {
        return CPS;
    }

    public double getCPM() {
        return CPM;
    }

    @Override
    public String toString() {
        return "GMDetectorResponse{" +
                "timeOfMeasurement=" + timeOfMeasurement +
                ", countsTotal=" + countsTotal +
                ", CPS=" + CPS +
                ", CPM=" + CPM +
                ", SMB20Ratio=" + SMB20Ratio +
                ", radiationPerHour=" + radiationPerHour +
                '}';
    }
}
