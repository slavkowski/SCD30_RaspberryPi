package pl.sgeonet.libraries.geigerDetector;

public class GMDetectorResponse {
    private int timeOfMeasurement;
    private long countsTotal;
    private float CPS;
    private float CPM;
    private final float SMB20Ratio = 2.9f;
    private float radiationPerHour;

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
        this.CPS = (float) countsTotal / timeOfMeasurement;
        this.radiationPerHour = this.CPS / SMB20Ratio;
        this.CPM = (float) countsTotal / (timeOfMeasurement / 60.0f);
    }

    public float getCPS() {
        return CPS;
    }

    public float getCPM() {
        return CPM;
    }

    public float getSMB20Ratio() {
        return SMB20Ratio;
    }

    public float getRadiationPerHour() {
        return radiationPerHour;
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
