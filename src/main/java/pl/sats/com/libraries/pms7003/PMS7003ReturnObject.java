package pl.sats.com.libraries.pms7003;

public class PMS7003ReturnObject {
    private int pm1_0_atm;
    private int pm2_5_atm;
    private int pm10_0_atm;
    private int pm0_3_count;
    private int pm0_5_count;
    private int pm1_0_count;
    private int pm2_5_count;
    private int pm5_0_count;
    private int pm10_0_count;

    PMS7003ReturnObject(String line) {
        String[] array = line.split(",");
        pm1_0_atm = Integer.parseInt(array[10] + array[11], 16);
        pm2_5_atm = Integer.parseInt(array[12] + array[13], 16);
        pm10_0_atm = Integer.parseInt(array[14] + array[15], 16);
        pm0_3_count = Integer.parseInt(array[16] + array[17], 16);
        pm0_5_count = Integer.parseInt(array[18] + array[19], 16);
        pm1_0_count = Integer.parseInt(array[20] + array[21], 16);
        pm2_5_count = Integer.parseInt(array[22] + array[23], 16);
        pm5_0_count = Integer.parseInt(array[24] + array[25], 16);
        pm10_0_count = Integer.parseInt(array[26] + array[27], 16);
    }

    public int getPm1_0_atm() {
        return pm1_0_atm;
    }

    public int getPm2_5_atm() {
        return pm2_5_atm;
    }

    public int getPm10_0_atm() {
        return pm10_0_atm;
    }

    public int getPm0_3_count() {
        return pm0_3_count;
    }

    public int getPm0_5_count() {
        return pm0_5_count;
    }

    public int getPm1_0_count() {
        return pm1_0_count;
    }

    public int getPm2_5_count() {
        return pm2_5_count;
    }

    public int getPm5_0_count() {
        return pm5_0_count;
    }

    public int getPm10_0_count() {
        return pm10_0_count;
    }

    @Override
    public String toString() {
        return "PMS7003ReturnObject{" +
                ", pm1_0_atm=" + pm1_0_atm +
                ", pm2_5_atm=" + pm2_5_atm +
                ", pm10_0_atm=" + pm10_0_atm +
                ", pm0_3_count=" + pm0_3_count +
                ", pm0_5_count=" + pm0_5_count +
                ", pm1_0_count=" + pm1_0_count +
                ", pm2_5_count=" + pm2_5_count +
                ", pm5_0_count=" + pm5_0_count +
                ", pm10_0_count=" + pm10_0_count +
                '}';
    }
}
