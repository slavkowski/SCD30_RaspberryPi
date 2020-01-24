import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.sgeonet.libraries.scd30.SCD30DriverImpl;

import java.io.IOException;

class CheckSensorCommands {
    private SCD30DriverImpl scd30 = new SCD30DriverImpl(true);
    @Test
    void shouldReturnSoftReset() throws IOException {
        scd30.softReset();
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("d3", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("4", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
    }
    @Test
    void shouldReturnAltitudeCompensation() throws IOException {
        scd30.setAltitudeCompensation(1000);
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("51", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("2", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
        Assertions.assertEquals("3", Integer.toHexString(Byte.toUnsignedInt(buffer[2])));
        Assertions.assertEquals("e8", Integer.toHexString(Byte.toUnsignedInt(buffer[3])));
        Assertions.assertEquals("d4", Integer.toHexString(Byte.toUnsignedInt(buffer[4])));
    }
    @Test
    void shouldReturnForcedRecalibrationValue() throws IOException {
        scd30.setExternalReferenceValueForForcedRecalibration(450);
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("52", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("4", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
        Assertions.assertEquals("1", Integer.toHexString(Byte.toUnsignedInt(buffer[2])));
        Assertions.assertEquals("c2", Integer.toHexString(Byte.toUnsignedInt(buffer[3])));
        Assertions.assertEquals("50", Integer.toHexString(Byte.toUnsignedInt(buffer[4])));
    }
    @Test
    void shouldReturnDeactivateASC() throws IOException {
        scd30.activateContinuousCalculation(false);
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("53", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("6", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[2])));
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[3])));
        Assertions.assertEquals("81", Integer.toHexString(Byte.toUnsignedInt(buffer[4])));
    }
    @Test
    void shouldReturnTriggerContinuousWithNoPressureCompensation() throws IOException {
        scd30.triggerContinuousMeasurements();
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("10", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[2])));
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[3])));
        Assertions.assertEquals("81", Integer.toHexString(Byte.toUnsignedInt(buffer[4])));
    }
    @Test
    void shouldReturnMeasurementInterval() throws IOException {
        scd30.setMeasurementInterval(2);
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("46", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
        Assertions.assertEquals("0", Integer.toHexString(Byte.toUnsignedInt(buffer[2])));
        Assertions.assertEquals("2", Integer.toHexString(Byte.toUnsignedInt(buffer[3])));
        Assertions.assertEquals("e3", Integer.toHexString(Byte.toUnsignedInt(buffer[4])));
    }
    @Test
    void shouldReturnStopMeasurementInterval() throws IOException {
        scd30.stopContinuousMeasurement();
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("1", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("4", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
    }
    @Test
    void shouldReturnTemperatureOffset() throws IOException {
        scd30.setTemperatureOffsetForOnboardRHTSensor(500);
        byte[] buffer = scd30.getBufferForTesting();
        Assertions.assertEquals("54", Integer.toHexString(Byte.toUnsignedInt(buffer[0])));
        Assertions.assertEquals("3", Integer.toHexString(Byte.toUnsignedInt(buffer[1])));
        Assertions.assertEquals("1", Integer.toHexString(Byte.toUnsignedInt(buffer[2])));
        Assertions.assertEquals("f4", Integer.toHexString(Byte.toUnsignedInt(buffer[3])));
        Assertions.assertEquals("33", Integer.toHexString(Byte.toUnsignedInt(buffer[4])));
    }
}
