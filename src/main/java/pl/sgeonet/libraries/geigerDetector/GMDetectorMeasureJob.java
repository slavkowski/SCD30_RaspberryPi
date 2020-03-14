package pl.sgeonet.libraries.geigerDetector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

public class GMDetectorMeasureJob implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(GMDetectorMeasureJob.class);
    public Thread t;
    private Connection conn;

    public GMDetectorMeasureJob() {
        t = new Thread(this, "GMDetector");
        t.start();
    }
    public GMDetectorMeasureJob(Connection conn) {
        this.conn = conn;
        t = new Thread(this, "GMDetector");
        t.start();
    }

    public void run() {
        LOG.info("Beginning of thread: " + Thread.currentThread().getName());
        GeigerDetectorDriver geigerDetectorDriver = new GeigerDetectorDriver(60);
        System.out.println(geigerDetectorDriver.getGmDetectorResponse().toString());

    }
}
