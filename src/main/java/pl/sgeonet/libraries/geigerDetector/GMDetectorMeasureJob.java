package pl.sgeonet.libraries.geigerDetector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

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
        GeigerDetectorDriver geigerDetectorDriver = new GeigerDetectorDriver(840);

        GMDetectorResponse gmDetectorResponse = geigerDetectorDriver.getGmDetectorResponse();
        LOG.info(geigerDetectorDriver.getGmDetectorResponse().toString());
        try {
            LOG.info("Save GM into db");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO gm (counts, timeinterval, cps, cpm, ratio, radiation, ts) VALUES (?,?,?,?,?,?,?)");
            stmt.setLong(1, gmDetectorResponse.getCountsTotal());
            stmt.setInt(2, gmDetectorResponse.getTimeOfMeasurement());
            stmt.setFloat(3, gmDetectorResponse.getCPS());
            stmt.setFloat(4, gmDetectorResponse.getCPM());
            stmt.setFloat(5, gmDetectorResponse.getSMB20Ratio());
            stmt.setFloat(6, gmDetectorResponse.getRadiationPerHour());
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            LOG.warn("SQL Exception -> {}", e.getMessage());
        }

        LOG.info("End of thread: " + Thread.currentThread().getName());
    }
}
