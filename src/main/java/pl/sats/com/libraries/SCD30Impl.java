package pl.sats.com.libraries;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.util.Console;

import java.io.IOException;

public class SCD30Impl implements SCD30 {


    private I2CDevice device;
    private Console console = new Console();


    public SCD30Impl() throws IOException, I2CFactory.UnsupportedBusNumberException {
        initializeSCD30();
    }

    public SCD30Impl(boolean testMode) {

    }

    @Override
    public void initializeSCD30() throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
        device = i2CBus.getDevice(SCD30_I2C_ADDRESS);
    }

    @Override
    public void triggerContinuousMeasurements() {
        byte[] command = getArgumentsFromCommand(SCD30_CONTINUOUS_MEASUREMENT);
        byte[] buffer = {command[0], command[1], 0x00, 0x00, (byte) 0x81};
        writeBuffer(buffer);
    }

    @Override
    public void triggerContinuousMeasurementsWithOptionalAmbientPressureCompensation(int pressure) {
        if (pressure < 700) {
            pressure = 700;
        } else if (pressure > 1200) {
            pressure = 1200;
        }
        byte[] command = getArgumentsFromCommand(SCD30_CONTINUOUS_MEASUREMENT);
        byte[] argument = createArgumentWithCRC(pressure);
        byte[] buffer = {command[0], command[1], argument[0], argument[1], argument[2]};
        writeBuffer(buffer);
    }


    @Override
    public void stopContinuousMeasurement() {
        byte[] command = getArgumentsFromCommand(SCD30_STOP_MEASUREMENT);
        byte[] buffer = {command[0], command[1]};
        writeBuffer(buffer);
    }

    @Override
    public void setMeasurementInterval(int interval) {

        if (interval < 2) {
            interval = 2;
        } else if (interval > 1800) {
            interval = 1800;
        }

        byte[] argument = createArgumentWithCRC(interval);
        byte[] command = getArgumentsFromCommand(SCD30_SET_MEASUREMENT_INTERVAL);

        byte[] buffer = {command[0], command[1], argument[0], argument[1], argument[2]};
        writeBuffer(buffer);
    }


    @Override
    public boolean getDataReadyStatus() {
        byte[] command = getArgumentsFromCommand(SCD30_GET_DATA_READY);
        byte[] buffer = {command[0], command[1]};
        writeBuffer(buffer);
        int[] result = readBuffer(3);
        if (result[2] != checkCRC8(result, 2, 0)) {
            return false;
        }
        int status = (result[0] << 8) | result[1];
        return status == 1;
    }

    @Override
    public float[] readMeasurement() {
        byte[] command = getArgumentsFromCommand(SCD30_READ_MEASUREMENT);
        byte[] buffer = {command[0], command[1]};
        float[] realData = {-1.0F, -1.0F, -1.0F};
        if (!getDataReadyStatus()) {
            return realData;
        }
        writeBuffer(buffer);
        int[] result = readBuffer(18);
        if (checkCRC8(result, 2, 0) != result[2] || checkCRC8(result, 2, 3) != result[5] ||
                checkCRC8(result, 2, 6) != result[8] || checkCRC8(result, 2, 9) != result[11] ||
                checkCRC8(result, 2, 12) != result[14] || checkCRC8(result, 2, 15) != result[17]) {
            return realData;
        }
        int co2 = (result[0] << 24) | (result[1] << 16) | (result[3] << 8) | result[4];
        realData[0] = Float.intBitsToFloat(co2);
        int t = (result[6] << 24) | (result[7] << 16) | (result[9] << 8) | result[10];
        realData[1] = Float.intBitsToFloat(t);
        int h = (result[12] << 24) | (result[13] << 16) | (result[15] << 8) | result[16];
        realData[2] = Float.intBitsToFloat(h);
        return realData;
    }

    @Override
    public void activateContinuousCalculation() {

    }

    @Override
    public void setExternalReferenceValueForForcedRecalibration() {

    }

    @Override
    public void setTemperatureOffsetForOnboardRHTSensor() {

    }

    @Override
    public void setAltitudeCompensation() {

    }

    @Override
    public void softReset() {

    }

    private void writeBuffer(byte[] buffer) {
        try {
            device.write(buffer);
        } catch (IOException e) {
            console.println(e);
        }
    }

    private int[] readBuffer(int length) {
        byte[] readData = new byte[length];
        int[] readDataUInt = new int[length];
        try {
            device.read(readData, 0, length);
        } catch (IOException e) {
            console.println(e);
        }
        for (int i = 0; i < length; i++) {
            readDataUInt[i] = Byte.toUnsignedInt(readData[i]);
        }
        return readDataUInt;
    }

    private byte[] getArgumentsFromCommand(int argument) {
        byte[] results = new byte[2];
        results[0] = (byte) ((argument >> 8));
        results[1] = (byte) (argument);
        return results;
    }

    public byte[] createArgumentWithCRC(int argument) {
        byte[] results = new byte[3];
        int[] intResults = new int[2];
        intResults[0] = Byte.toUnsignedInt((byte) ((argument >> 8)));
        intResults[1] = Byte.toUnsignedInt((byte) (argument));


        results[0] = (byte) ((argument >> 8));
        results[1] = (byte) (argument);
        results[2] = (byte) checkCRC8(intResults, 2, 0);

        return results;
    }

    public int checkCRC8(int[] byteData, int length, int offset) {
        byte crc = (byte) 0xff;
        for (int i = 0; i < length; i++) {
            crc ^= byteData[i + offset];
            for (int bit = 0; bit < 8; bit++) {
                if ((crc & 0x80) != 0) {
                    crc = (byte) ((crc << 1) ^ SCD30_POLYNOMIAL);
                } else {
                    crc <<= 1;
                }
            }
        }
        return Byte.toUnsignedInt(crc);
    }

}