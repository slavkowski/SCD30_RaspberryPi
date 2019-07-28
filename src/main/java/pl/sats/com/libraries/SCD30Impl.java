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

    @Override
    public void initializeSCD30() throws IOException, I2CFactory.UnsupportedBusNumberException {
            I2CBus i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
            device = i2CBus.getDevice(SCD30_I2C_ADDRESS);
    }

    @Override
    public void triggerContinuousMeasurementsWithOptionalAmbientPressureCompensation() {
        byte[] buffer = {0x00, 0x10, 0x00, 0x00, (byte) 0x81};
        writeBuffer(buffer);
    }

    @Override
    public void stopContinuousMeasurement() {
        byte[] buffer = {0x01, 0x07};
        writeBuffer(buffer);
    }

    @Override
    public void setMeasurementInterval() {

    }


    @Override
    public boolean getDataReadyStatus() {
        byte[] buffer = {0x02, 0x02};
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
        byte[] buffer = {0x03, 0x00};
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
        int co2 = (result[0] << 24) | (result[1] << 16) |  (result[3] << 8) |  result[4];
        realData[0] = Float.intBitsToFloat(co2);
        int t = (result[6] << 24) | (result[7] << 16) |  (result[9] << 8) |  result[10];
        realData[1] = Float.intBitsToFloat(t);
        int h = (result[12] << 24) | (result[13] << 16) |  (result[15] << 8) |  result[16];
        realData[2] = Float.intBitsToFloat(h);
        return realData;
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
