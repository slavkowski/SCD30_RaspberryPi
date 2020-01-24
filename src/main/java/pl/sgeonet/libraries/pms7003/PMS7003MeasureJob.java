package pl.sgeonet.libraries.pms7003;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PMS7003MeasureJob implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(PMS7003MeasureJob.class);
    public Thread t;
    private PMS7003Driver pms7003Driver;
    private PMS7003Response pms7003Response;
    private Connection conn;

    public PMS7003MeasureJob(){
        t = new Thread(this, "PMS7003");
        t.start();
    }
    public PMS7003MeasureJob(Connection conn){
        this.conn = conn;
        t = new Thread(this, "PMS7003");
        t.start();
    }
    @Override
    public void run() {
        LOG.info("Beginning of thread: " + Thread.currentThread().getName());

            pms7003Driver = new PMS7003Driver();
            pms7003Driver.getDataFromSensor();
            pms7003Response = pms7003Driver.getPms7003Response();

        try {
            LOG.info("Save PMS7003 into db");
//            pms7003Response.printPMS7003Data();
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO pms7003 (pm1_0_atm, pm2_5_atm, pm10_0_atm, ts) VALUES (?,?,?,?)");
            stmt.setFloat(1, pms7003Response.getPm1_0_atmAM());
            stmt.setFloat(2, pms7003Response.getPm2_5_atmAM());
            stmt.setFloat(3, pms7003Response.getPm10_0_atmAM());
            stmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            stmt.execute();
            conn.close();
        } catch (SQLException e) {
            LOG.warn("SQL Exception -> {}", e.getMessage());
        }
        LOG.info("End of thread: " + Thread.currentThread().getName());
    }

    public PMS7003Response getPms7003Response() {
        return pms7003Response;
    }
}
