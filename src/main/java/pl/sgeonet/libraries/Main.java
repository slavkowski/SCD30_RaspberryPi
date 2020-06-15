package pl.sgeonet.libraries;


import pl.sgeonet.libraries.geigerDetector.GMDetectorMeasureJob;
import pl.sgeonet.libraries.geigerDetector.GeigerDetectorDriver;
import pl.sgeonet.libraries.gravity.GravityLightningDetectorDriver;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        GeigerDetectorDriver geigerDetectorDriver = new GeigerDetectorDriver(20);
//        GravityLightningDetectorDriver gravityLightningDetectorDriver = new GravityLightningDetectorDriver();
//        gravityLightningDetectorDriver.runMeasurement();
    }

}
