package pl.sgeonet.libraries.geigerDetector;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.sgeonet.libraries.pms7003.PMS7003Driver;

import java.sql.Timestamp;

public class GeigerDetectorDriver {

    private static final Logger LOG = LoggerFactory.getLogger(GeigerDetectorDriver.class);
    private GMDetectorResponse gmDetectorResponse = new GMDetectorResponse();
    private int measurementTimeInSeconds;
    private long counts = 0;

    public GeigerDetectorDriver(int timeInSeconds) {
        this.measurementTimeInSeconds = timeInSeconds;
        runMeasurement();
    }

    private void runMeasurement() {
        gmDetectorResponse.setTimeOfMeasurement(measurementTimeInSeconds);

        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
        final GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalInput geigerEvent = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, "GM_DETECTOR", PinPullResistance.PULL_DOWN);


        GpioPinListenerDigital gpioPinListenerDigital = event -> {
            if (event.getEdge() == PinEdge.RISING) {
                counts++;
//                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getEdge() + " TIME:" + new Timestamp(System.currentTimeMillis()));
//                LOG.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getEdge() + " TIME:" + new Timestamp(System.currentTimeMillis()));
            }
        };

        geigerEvent.addListener((gpioPinListenerDigital));
        LOG.info("Measuring" + Thread.currentThread().getName());
        LOG.info("Measure interval: " + measurementTimeInSeconds + "sec");
        try {
            Thread.sleep(measurementTimeInSeconds * 1000);
        } catch (InterruptedException e) {
            LOG.warn("InterruptedException during measuring -> {}", e.getMessage());
        }
        geigerEvent.removeListener(gpioPinListenerDigital);
        LOG.info("Remove listener");
        gpio.shutdown();
        LOG.info("GPIO shutdown");
        gpio.unprovisionPin(geigerEvent);
        LOG.info("GPIO unprovision Pin");
        GpioFactory.getExecutorServiceFactory().shutdown();
        gmDetectorResponse.setTimeOfMeasurement(measurementTimeInSeconds);
        gmDetectorResponse.setCountsTotal(counts);
    }

    public GMDetectorResponse getGmDetectorResponse() {
        return gmDetectorResponse;
    }
}
