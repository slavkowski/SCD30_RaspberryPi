package pl.sats.com.libraries;

import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.Console;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
//        int interval = 1000;
//
//        int byte0 = Byte.toUnsignedInt((byte) (interval));
//        int byte1 = Byte.toUnsignedInt((byte) ((interval >> 8)));
//
//        System.out.println(byte1 + " " + (byte)byte0);
//
//        System.out.println(Integer.toHexString(byte1) + " " + Integer.toHexString(byte0));

        SCD30Impl scd30 = new SCD30Impl(true);
        scd30.stopContinuousMeasurement();



//        Console console = new Console();
//        SCD30Impl scd30;
//        float[] enData = new float[3];
//        try {
//            scd30 = new SCD30Impl();
//            enData = scd30.readMeasurement();
//        } catch (IOException | I2CFactory.UnsupportedBusNumberException e) {
//            e.printStackTrace();
//        }
//
//        console.println("CO2 : " + enData[0] + " ppm");
//        console.println("TEMP : " + enData[1] + " C");
//        console.println("HYDRO : " + enData[2] + " %");

    }
}
