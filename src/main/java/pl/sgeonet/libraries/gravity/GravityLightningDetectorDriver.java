package pl.sgeonet.libraries.gravity;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.sql.Timestamp;

public class GravityLightningDetectorDriver {

//    private static final Logger LOG = LoggerFactory.getLogger(GravityLightningDetectorDriver.class);

    public void runMeasurement() {
        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));
        final GpioController gpio = GpioFactory.getInstance();
        GpioPinDigitalInput lightningEvent = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "GM_DETECTOR", PinPullResistance.PULL_DOWN);

        GpioPinListenerDigital gpioPinListenerDigital = event -> {
            if (event.getEdge() == PinEdge.RISING) {
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getEdge() + " TIME:" + new Timestamp(System.currentTimeMillis()));
//                LOG.info(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getEdge() + " TIME:" + new Timestamp(System.currentTimeMillis()));
            }
        };

        lightningEvent.addListener((gpioPinListenerDigital));
    }
}
