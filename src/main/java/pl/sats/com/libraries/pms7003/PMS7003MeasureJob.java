package pl.sats.com.libraries.pms7003;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PMS7003MeasureJob implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(PMS7003MeasureJob.class);
    public Thread t;
    private PMS7003Driver pms7003Driver;
    private PMS7003Response pms7003Response;

    public PMS7003MeasureJob(){
        t = new Thread(this, "PMS7003");
        t.start();
    }
    @Override
    public void run() {
        LOG.info("Beginning of run" + Thread.currentThread().getName());
        try {
            pms7003Driver = new PMS7003Driver();
            pms7003Driver.getDataFromSensor();
            pms7003Response = pms7003Driver.getPms7003Response();
        } catch (IOException | InterruptedException e) {
            LOG.warn("IOException or InterruptedException -> {}", e.getMessage());
        }
        LOG.info("End of run" + Thread.currentThread().getName());
    }

    public PMS7003Response getPms7003Response() {
        return pms7003Response;
    }
}
