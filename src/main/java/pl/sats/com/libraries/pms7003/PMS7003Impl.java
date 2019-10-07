package pl.sats.com.libraries.pms7003;

import com.pi4j.io.serial.*;

public class PMS7003Impl {
    private SerialConfig config = new SerialConfig();

    final Serial serial = SerialFactory.createInstance();

    public PMS7003Impl() {
        config.device("/dev/ttyUSB0")
                .baud(Baud._9600)
                .dataBits(DataBits._8)
                .parity(Parity.NONE)
                .stopBits(StopBits._1)
                .flowControl(FlowControl.NONE);
    }


}
