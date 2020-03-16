package pl.sgeonet.libraries.geigerDetector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class GMDetectorMeasureJob implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(GMDetectorMeasureJob.class);
    public Thread t;
    private DataSource dataSource;
    private GMDetectorResponse gmDetectorResponse;

    public GMDetectorMeasureJob() {
        t = new Thread(this, "GMDetector");
        t.start();
    }

    public GMDetectorMeasureJob(DataSource dataSource) {
        this.dataSource = dataSource;
        t = new Thread(this, "GMDetector");
        t.start();
    }

    public void run() {
        LOG.info("Beginning of thread: " + Thread.currentThread().getName());
        GeigerDetectorDriver geigerDetectorDriver = new GeigerDetectorDriver(840);

        LOG.info(geigerDetectorDriver.getGmDetectorResponse().toString());
        gmDetectorResponse = geigerDetectorDriver.getGmDetectorResponse();
        try {
            LOG.info("Save GM into db");
            LOG.info("Create connection");
            Connection conn = dataSource.getConnection();
            LOG.info("Prepare statement");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO gm (counts, timeinterval, cps, cpm, ratio, radiation, ts) VALUES (?,?,?,?,?,?,?)");
            stmt.setLong(1, gmDetectorResponse.getCountsTotal());
            stmt.setInt(2, gmDetectorResponse.getTimeOfMeasurement());
            stmt.setFloat(3, gmDetectorResponse.getCPS());
            stmt.setFloat(4, gmDetectorResponse.getCPM());
            stmt.setFloat(5, gmDetectorResponse.getSMB20Ratio());
            stmt.setFloat(6, gmDetectorResponse.getRadiationPerHour());
            stmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
            stmt.execute();
            LOG.info("Execute statement");
            conn.close();
            LOG.info("Close connection");
        } catch (SQLException e) {
            LOG.warn("SQL Exception -> {}", e.getMessage());
        }
        LOG.info("End of thread: " + Thread.currentThread().getName());
    }

}
