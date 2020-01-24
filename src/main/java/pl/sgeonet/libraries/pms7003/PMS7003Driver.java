package pl.sgeonet.libraries.pms7003;

import com.pi4j.io.serial.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PMS7003Driver {

    private static final Logger LOG = LoggerFactory.getLogger(PMS7003Driver.class);

    private static final int FRAME_SIZE = 32;
    private static final byte START_BYTE_1 = 0x42;
    private static final byte START_BYTE_2 = 0x4D;
    private static final byte[] SLEEP_CMD_BYTES = {START_BYTE_1, START_BYTE_2, (byte) 0xE4, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x73};
    private static final byte[] WAKEUP_CMD_BYTES = {START_BYTE_1, START_BYTE_2, (byte) 0xE4, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x74};

    private SerialConfig config = new SerialConfig();
    private final Serial serial;
    private PMS7003Response pms7003Response;

    public PMS7003Driver() {
        config.device("/dev/ttyAMA0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        serial = SerialFactory.createInstance();
        serial.setBufferingDataReceived(false);
        LOG.info("Opening serial");
        try {
            serial.open(config);
        } catch (IOException e) {
            LOG.warn("IOException when open serial connection during PMS7003 initialization -> {}",e.getMessage());
        }

        wakeUp();
        LOG.info("Warming up [120sec]");
        try {
            Thread.sleep(120000);
        } catch (InterruptedException e) {
            LOG.warn("IOException during warming up device -> {}",e.getMessage());
        }
    }

    void setPassiveMode() throws IOException {
        byte[] command = {0x42, 0x4D, (byte) 0xE1, 0x00, 0x00, 0x01, 0x70};
        serial.open(config);
        serial.write(command);
        serial.close();
    }

    void setActiveMode() throws IOException {
        byte[] command = {0x42, 0x4D, (byte) 0xE1, 0x00, 0x01, 0x01, 0x71};
        serial.open(config);
        serial.write(command);
        serial.close();
    }

    void setSleep() {
        byte[] command = {0x42, 0x4D, (byte) 0xE4, 0x00, 0x00, 0x01, 0x73};
        LOG.info("Setting PMS7003 into sleep mode");
        try {
            serial.write(command);
        } catch (IOException e) {
            LOG.warn("IOException during setting sleep mode -> {}", e.getMessage());
        }
    }

    void wakeUp() {
        byte[] command = {0x42, 0x4D, (byte) 0xE4, 0x00, 0x01, 0x01, 0x74};
        LOG.info("PMS7003 is being waking up");
        try {
            serial.write(command);
        } catch (IOException e) {
            LOG.warn("IOException during waking up -> {}", e.getMessage());
        }
    }


    void getDataFromSensor() {
        pms7003Response = new PMS7003Response();

        SerialDataEventListener serialDataEventListener = event -> {

            try {
                if (event.length() > 0) {
                    String line = event.getHexByteString();
                    String[] hexByteArray = line.split(",");
                    if (hexByteArray.length == FRAME_SIZE) {
                        pms7003Response.addStringDataRecord(line);
                    } else {
                        LOG.warn("Response doesn't have 32 bytes");
                    }
                }
            } catch (IOException e) {
                LOG.error("Failed to read bytes from event -> {}", e.getMessage());
            }
        };

        serial.addListener(serialDataEventListener);


//        try {
//            LOG.info("Thread sleep: 0.5 sec");
//            Thread.sleep(500);
//            LOG.info("Serial reading");
//            serial.read();
//        } catch (IOException | InterruptedException e) {
//            LOG.warn("IOException during serial reading -> {}",e.getMessage());
//        }
        LOG.info("Measuring" + Thread.currentThread().getName());
        LOG.info("Measure interval: 60sec");
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            LOG.warn("InterruptedException during measuring -> {}",e.getMessage());
        }
        LOG.info("Remove listener");
        serial.removeListener(serialDataEventListener);
        setSleep();
        LOG.info("Close serial after measuring");
        try {
            serial.close();
        } catch (IOException e) {
            LOG.warn("IOException during closing after measure -> {}",e.getMessage());
        }
        LOG.info("Remove executor");
        SerialFactory.getExecutorServiceFactory().shutdown();
    }

    public PMS7003Response getPms7003Response() {
        pms7003Response.calculatePMS7003Data();
        return pms7003Response;
    }

}
