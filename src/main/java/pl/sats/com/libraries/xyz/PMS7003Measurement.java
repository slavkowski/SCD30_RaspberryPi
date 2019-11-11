package pl.sats.com.libraries.xyz;

import java.time.Instant;

public class PMS7003Measurement {
    private Instant time;

    private int pm1_0_cf1;
    private int pm2_5_cf1;
    private int pm10_0_cf1;
    private int pm1_0_atmo;
    private int pm2_5_atmo;
    private int pm10_0_atmo;
    private int pm0_3_count;
    private int pm0_5_count;
    private int pm1_0_count;
    private int pm2_5_count;
    private int pm5_0_count;
    private int pm10_0_count;

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public int getPm1_0_cf1() {
        return pm1_0_cf1;
    }

    public void setPm1_0_cf1(int pm1_0_cf1) {
        this.pm1_0_cf1 = pm1_0_cf1;
    }

    public int getPm2_5_cf1() {
        return pm2_5_cf1;
    }

    public void setPm2_5_cf1(int pm2_5_cf1) {
        this.pm2_5_cf1 = pm2_5_cf1;
    }

    public int getPm10_0_cf1() {
        return pm10_0_cf1;
    }

    public void setPm10_0_cf1(int pm10_0_cf1) {
        this.pm10_0_cf1 = pm10_0_cf1;
    }

    public int getPm1_0_atmo() {
        return pm1_0_atmo;
    }

    public void setPm1_0_atmo(int pm1_0_atmo) {
        this.pm1_0_atmo = pm1_0_atmo;
    }

    public int getPm2_5_atmo() {
        return pm2_5_atmo;
    }

    public void setPm2_5_atmo(int pm2_5_atmo) {
        this.pm2_5_atmo = pm2_5_atmo;
    }

    public int getPm10_0_atmo() {
        return pm10_0_atmo;
    }

    public void setPm10_0_atmo(int pm10_0_atmo) {
        this.pm10_0_atmo = pm10_0_atmo;
    }

    public int getPm0_3_count() {
        return pm0_3_count;
    }

    public void setPm0_3_count(int pm0_3_count) {
        this.pm0_3_count = pm0_3_count;
    }

    public int getPm0_5_count() {
        return pm0_5_count;
    }

    public void setPm0_5_count(int pm0_5_count) {
        this.pm0_5_count = pm0_5_count;
    }

    public int getPm1_0_count() {
        return pm1_0_count;
    }

    public void setPm1_0_count(int pm1_0_count) {
        this.pm1_0_count = pm1_0_count;
    }

    public int getPm2_5_count() {
        return pm2_5_count;
    }

    public void setPm2_5_count(int pm2_5_count) {
        this.pm2_5_count = pm2_5_count;
    }

    public int getPm5_0_count() {
        return pm5_0_count;
    }

    public void setPm5_0_count(int pm5_0_count) {
        this.pm5_0_count = pm5_0_count;
    }

    public int getPm10_0_count() {
        return pm10_0_count;
    }

    public void setPm10_0_count(int pm10_0_count) {
        this.pm10_0_count = pm10_0_count;
    }
}
