import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.sats.com.libraries.scd30.SCD30Impl;


class CreateArgumentWithCRCTest {
    private SCD30Impl scd30 = new SCD30Impl(true);

    CreateArgumentWithCRCTest(){
    }
    @Test
    void shouldReturnCorrectArgumentsArray() {
        int argument = 450;
        byte[] results = scd30.createArgumentWithCRC(argument);
        String LSB = Integer.toHexString(Byte.toUnsignedInt(results[0]));
        String MSB = Integer.toHexString(Byte.toUnsignedInt(results[1]));
        String CRC = Integer.toHexString(results[2]);

        Assertions.assertEquals("1", LSB);
        Assertions.assertEquals("c2", MSB);
        Assertions.assertEquals("50", CRC);
    }
}
