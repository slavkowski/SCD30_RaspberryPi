package pl.sgeonet.libraries.scd30;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class SCD30DriverImpl implements SCD30Driver {

    private static final Logger LOG = LoggerFactory.getLogger(SCD30DriverImpl.class);

    private I2CDevice device;
    private byte[] bufferForTesting;
    private boolean testMode = false;

    public SCD30DriverImpl() throws IOException, I2CFactory.UnsupportedBusNumberException {
        initializeSCD30();
    }

    public SCD30DriverImpl(boolean testMode) {
        bufferForTesting = new byte[5];
        this.testMode = testMode;
    }

    @Override
    public void initializeSCD30() throws IOException, I2CFactory.UnsupportedBusNumberException {
        I2CBus i2CBus = I2CFactory.getInstance(I2CBus.BUS_1);
        device = i2CBus.getDevice(SCD30_I2C_ADDRESS);
    }

    @Override
    public void triggerContinuousMeasurements() throws IOException {
        byte[] command = getArgumentsFromCommand(SCD30_CONTINUOUS_MEASUREMENT);
        byte[] buffer = {command[0], command[1], 0x00, 0x00, (byte) 0x81};
        writeBuffer(buffer);
    }

    @Override
    public void triggerContinuousMeasurementsWithOptionalAmbientPressureCompensation(int pressure) throws IOException {
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
    public void stopContinuousMeasurement() throws IOException {
        byte[] command = getArgumentsFromCommand(SCD30_STOP_MEASUREMENT);
        byte[] buffer = {command[0], command[1]};
        writeBuffer(buffer);
    }

    @Override
    public void setMeasurementInterval(int interval) throws IOException {

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
    public boolean getDataReadyStatus() throws IOException {
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
    public float[] readMeasurement() throws IOException {
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
    public void activateContinuousCalculation(boolean isActive) throws IOException {
        byte[] command = getArgumentsFromCommand(SCD30_AUTOMATIC_SELF_CALIBRATION);
        byte[] argument;
        if (isActive) {
            argument = createArgumentWithCRC(1);
        } else {
            argument = createArgumentWithCRC(0);
        }
        byte[] buffer = {command[0], command[1], argument[0], argument[1], argument[2]};
        writeBuffer(buffer);
    }


    @Override
    public void setExternalReferenceValueForForcedRecalibration(int refValue) throws IOException {
        if (400 <= refValue && refValue <= 2000) {
            byte[] argument = createArgumentWithCRC(refValue);
            byte[] command = getArgumentsFromCommand(SCD30_SET_FORCED_RECALIBRATION_FACTOR);

            byte[] buffer = {command[0], command[1], argument[0], argument[1], argument[2]};
            writeBuffer(buffer);
        }
    }

    @Override
    public void setTemperatureOffsetForOnboardRHTSensor(int offset) throws IOException {
        byte[] command = getArgumentsFromCommand(SCD30_SET_TEMPERATURE_OFFSET);
        byte[] argument = createArgumentWithCRC(offset);

        byte[] buffer = {command[0], command[1], argument[0], argument[1], argument[2]};
        writeBuffer(buffer);
    }

    @Override
    public void setAltitudeCompensation(int altitudeCompensation) throws IOException {
        byte[] command = getArgumentsFromCommand(SCD30_SET_ALTITUDE_COMPENSATION);
        byte[] argument = createArgumentWithCRC(altitudeCompensation);

        byte[] buffer = {command[0], command[1], argument[0], argument[1], argument[2]};
        writeBuffer(buffer);
    }

    @Override
    public void softReset() throws IOException {
        byte[] command = getArgumentsFromCommand(SCD30_SOFT_RESET);
        byte[] buffer = {command[0], command[1]};
        writeBuffer(buffer);
    }

    private void writeBuffer(byte[] buffer) throws IOException {
        if (testMode) {
            bufferForTesting = buffer;
        } else {
            device.write(buffer);
        }
    }

    private int[] readBuffer(int length) throws IOException {
        byte[] readData = new byte[length];
        int[] readDataUInt = new int[length];
        device.read(readData, 0, length);
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

    public byte[] getBufferForTesting() {
        return bufferForTesting;
    }
}
