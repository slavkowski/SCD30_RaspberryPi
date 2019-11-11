package pl.sats.com.libraries.pms7003;

import com.pi4j.io.serial.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class PMS7003 {

    private static final Logger LOG = LoggerFactory.getLogger(PMS7003.class);

    private static final int FRAME_SIZE = 32;
    private static final byte START_BYTE_1 = 0x42;
    private static final byte START_BYTE_2 = 0x4D;
    private static final byte[] SLEEP_CMD_BYTES = {START_BYTE_1, START_BYTE_2, (byte) 0xE4, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x73};
    private static final byte[] WAKEUP_CMD_BYTES = {START_BYTE_1, START_BYTE_2, (byte) 0xE4, (byte) 0x00, (byte) 0x01, (byte) 0x01, (byte) 0x74};

    private SerialConfig config = new SerialConfig();
    private final Serial serial;
    private PMS7003Response pms7003Response;

    public PMS7003() throws IOException, InterruptedException {
        config.device("/dev/ttyUSB0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
        serial = SerialFactory.createInstance();

        wakeUp();
        LOG.info("Warming up [60sec]");
        Thread.sleep(60000);
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

    void setSleep() throws IOException {
        byte[] command = {0x42, 0x4D, (byte) 0xE4, 0x00, 0x00, 0x01, 0x73};
        System.out.println("Open serial to set sleep");
        serial.open(config);
        serial.write(command);
        System.out.println("Close serial after setting sleep");
        serial.close();
    }

    void wakeUp() throws IOException {
        byte[] command = {0x42, 0x4D, (byte) 0xE4, 0x00, 0x01, 0x01, 0x74};
        System.out.println("Open serial to set wake up");
        serial.open(config);
        serial.write(command);
        System.out.println("Close serial after wake up");
        serial.close();
    }


    void getDataFromSensor() throws InterruptedException, IOException {
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
                LOG.error("Failed to read bytes from event. {}", e.getMessage());
            }
        };

        serial.addListener(serialDataEventListener);

        System.out.println("Open serial for measuring");
        serial.open(config);
        System.out.println("Serial reading");
        serial.read();
        System.out.println("Measuring" + Thread.currentThread().getName());
        System.out.println("Measure interval: 60sec");
        Thread.sleep(60000);
        System.out.println("Remove listener");
        serial.removeListener(serialDataEventListener);
        System.out.println("Close serial after measuring");
        serial.close();
        setSleep();
        System.out.println("Remove executor");
        SerialFactory.getExecutorServiceFactory().shutdown();
    }

    public PMS7003Response getPms7003Response() {
        pms7003Response.calculatePMS7003Data();
        return pms7003Response;
    }

}
