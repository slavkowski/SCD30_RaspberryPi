package pl.sats.com.libraries.pms7003;

import java.io.IOException;

public class PMS7003MeasureJob implements Runnable {
    public Thread t;
    private PMS7003 pms7003;
    private PMS7003Response pms7003Response;

    public PMS7003MeasureJob(){
        t = new Thread(this, "PMS7003");
        t.start();
    }
    @Override
    public void run() {
        System.out.println("Beginning of run" + Thread.currentThread().getName());
        try {
            pms7003 = new PMS7003();
            pms7003.getDataFromSensor();
            pms7003Response = pms7003.getPms7003Response();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("End of run" + Thread.currentThread().getName());
    }

    public PMS7003Response getPms7003Response() {
        return pms7003Response;
    }
}
