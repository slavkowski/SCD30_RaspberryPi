import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.sgeonet.libraries.scd30.SCD30DriverImpl;


class CRC8CheckSumTest {
    private SCD30DriverImpl scd30 = new SCD30DriverImpl(true);

    CRC8CheckSumTest(){
    }

    @Test
    void shouldReturnCorrectChecksum() {
        int[] data = {0xbe, 0xef};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("92", hexCRC8Checksum);
    }
    @Test
    void shouldReturnCorrectChecksum2() {
        int[] data = {0x00, 0x01};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("b0", hexCRC8Checksum);
    }
    @Test
    void shouldReturnCorrectChecksum3() {
        int[] data = {0x43, 0xdb};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("cb", hexCRC8Checksum);
    }
    @Test
    void shouldReturnCorrectChecksum4() {
        int[] data = {0x42, 0x43};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("bf", hexCRC8Checksum);
    }
    @Test
    void shouldReturnCorrectChecksum5() {
        int[] data = {0x00, 0x00};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("81", hexCRC8Checksum);
    }
    @Test
    void shouldReturnCorrectChecksum6() {
        int[] data = {0x44, 0x1d};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("84", hexCRC8Checksum);
    }
    @Test
    void shouldReturnCorrectChecksum7() {
        int[] data = {0x43, 0x81};
        int length = 2;
        int offset = 0;
        int decCRC8Checksum = scd30.checkCRC8(data, length,offset);
        String hexCRC8Checksum = Integer.toHexString(decCRC8Checksum);
        Assertions.assertEquals("6e", hexCRC8Checksum);
    }
}
