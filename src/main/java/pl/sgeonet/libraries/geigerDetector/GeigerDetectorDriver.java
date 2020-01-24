package pl.sgeonet.libraries.geigerDetector;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;

import java.sql.Timestamp;

public class GeigerDetectorDriver {
    public GeigerDetectorDriver() {
        setConfiguration();
    }

    private void setConfiguration() {
        System.out.println("<--Pi4J--> GPIO Control Example ... started.");

        GeigerTubeImpulseHandler geigerTubeImpulseHandler = new GeigerTubeImpulseHandler();

        // in order to use the Broadcom GPIO pin numbering scheme, we need to configure the
        // GPIO factory to use a custom configured Raspberry Pi GPIO provider
        GpioFactory.setDefaultProvider(new RaspiGpioProvider(RaspiPinNumberingScheme.BROADCOM_PIN_NUMBERING));

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        GpioPinDigitalInput geigerEvent = gpio.provisionDigitalInputPin(RaspiPin.GPIO_17, "MyButton", PinPullResistance.PULL_UP);

        geigerEvent.addListener((GpioPinListenerDigital) event -> {
            // display pin state on console
            if (event.getEdge() == PinEdge.FALLING) {
//                geigerTubeImpulseHandler.newImpulse();
                System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getEdge() + " TIME:" + new Timestamp(System.currentTimeMillis()));
            }

        });
        while (true){

        }
    }


}
