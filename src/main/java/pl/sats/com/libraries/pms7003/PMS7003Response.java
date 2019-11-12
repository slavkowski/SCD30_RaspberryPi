package pl.sats.com.libraries.pms7003;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sats.com.libraries.xyz.PMS7003Measurement;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class PMS7003Response {

    private static final Logger LOG = LoggerFactory.getLogger(PMS7003Response.class);

    private float pm1_0_atmAM;
    private int pm1_0_atmRMS;
    private float pm2_5_atmAM;
    private int pm2_5_atmRMS;
    private float pm10_0_atmAM;
    private int pm10_0_atmRMS;
    private float pm0_3_countAM;
    private int pm0_3_countRMS;
    private float pm0_5_countAM;
    private int pm0_5_countRMS;
    private float pm1_0_countAM;
    private int pm1_0_countRMS;
    private float pm2_5_countAM;
    private int pm2_5_countRMS;
    private float pm5_0_countAM;
    private int pm5_0_countRMS;
    private float pm10_0_countAM;
    private int pm10_0_countRMS;

    private int numberOfObservations = 0;
    private List<String> dataSet = new ArrayList<>();
    private List<PMS7003ReturnObject> pms7003Objects = new ArrayList<>();


    public int getNumberOfObservations() {
        return numberOfObservations;
    }

    void addStringDataRecord(String dataRow) {
        dataSet.add(dataRow);
        numberOfObservations++;
    }

    void calculatePMS7003Data() {

        for (String line : dataSet) {
            PMS7003ReturnObject pms7003ReturnObject = new PMS7003ReturnObject(line);
            pms7003Objects.add(pms7003ReturnObject);
            pm1_0_atmAM += pms7003ReturnObject.getPm1_0_atm();
            pm2_5_atmAM += pms7003ReturnObject.getPm2_5_atm();
            pm10_0_atmAM += pms7003ReturnObject.getPm10_0_atm();

            pm0_3_countAM += pms7003ReturnObject.getPm0_3_count();
            pm0_5_countAM += pms7003ReturnObject.getPm0_5_count();
            pm1_0_countAM += pms7003ReturnObject.getPm1_0_count();
            pm2_5_countAM += pms7003ReturnObject.getPm2_5_count();
            pm5_0_countAM += pms7003ReturnObject.getPm5_0_count();
            pm10_0_countAM += pms7003ReturnObject.getPm10_0_count();

        }
        pm1_0_atmAM /= (float) numberOfObservations;
        pm2_5_atmAM /= (float) numberOfObservations;
        pm10_0_atmAM /= (float) numberOfObservations;

        pm0_3_countAM /= (float) numberOfObservations;
        pm0_5_countAM /= (float) numberOfObservations;
        pm1_0_countAM /= (float) numberOfObservations;
        pm2_5_countAM /= (float) numberOfObservations;
        pm5_0_countAM /= (float) numberOfObservations;
        pm10_0_countAM /= (float) numberOfObservations;

    }

    public void printPMS7003Data() {
        for (PMS7003ReturnObject o : pms7003Objects) {
            LOG.info(o.toString());
        }

    }

    void printRawData() {
        for (String line : dataSet) {
            LOG.info(line);
        }
    }

    public float getPm1_0_atmAM() {
        return pm1_0_atmAM;
    }

    public float getPm2_5_atmAM() {
        return pm2_5_atmAM;
    }

    public float getPm10_0_atmAM() {
        return pm10_0_atmAM;
    }

}
