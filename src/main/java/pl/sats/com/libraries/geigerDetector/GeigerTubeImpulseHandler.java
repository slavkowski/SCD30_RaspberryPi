package pl.sats.com.libraries.geigerDetector;

public class GeigerTubeImpulseHandler {
    private static long countsPerMinute = 0;

    private static long impulseA = 0;
    private static long impulseB = 0;
    private static long deltaTime;

    void newImpulse() {
        impulseA = impulseB;
        impulseB = System.currentTimeMillis();
        deltaTime = impulseB - impulseA;
        countsPerMinute = 60000 / deltaTime;
    }

    public static long getCountsPerMinute() {
        return countsPerMinute;
    }
}
