package pl.sats.com.libraries;

import com.pi4j.io.i2c.I2CFactory;

import java.io.IOException;

public interface SCD30 {
    int SCD30_I2C_ADDRESS = 0x61;

    int SCD30_CONTINUOUS_MEASUREMENT = 0x0010;
    int SCD30_SET_MEASUREMENT_INTERVAL = 0x4600;
    int SCD30_GET_DATA_READY = 0x0202;
    int SCD30_READ_MEASUREMENT = 0x0300;
    int SCD30_STOP_MEASUREMENT = 0x0104;
    int SCD30_AUTOMATIC_SELF_CALIBRATION = 0x5306;
    int SCD30_SET_FORCED_RECALIBRATION_FACTOR = 0x5204;
    int SCD30_SET_TEMPERATURE_OFFSET = 0x5403;
    int SCD30_SET_ALTITUDE_COMPENSATION = 0x5102;
    int SCD30_READ_SERIALNBR = 0xD033;

    int SCD30_SET_TEMP_OFFSET = 0x5403;

    byte SCD30_POLYNOMIAL = 0x31; // P(x) = x^8 + x^5 + x^4 + 1 = 100110001

    void initializeSCD30() throws IOException, I2CFactory.UnsupportedBusNumberException;

    void triggerContinuousMeasurements();

    void triggerContinuousMeasurementsWithOptionalAmbientPressureCompensation(int pressure);

    void stopContinuousMeasurement();

    void setMeasurementInterval(int interval);

    boolean getDataReadyStatus();

    float[] readMeasurement();

    void activateContinuousCalculation();

    void setExternalReferenceValueForForcedRecalibration();

    void setTemperatureOffsetForOnboardRHTSensor();

    void setAltitudeCompensation();

    void softReset();
}
