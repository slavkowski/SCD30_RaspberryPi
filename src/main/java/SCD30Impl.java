public class SCD30Impl implements SCD30 {
    public void test(){
        System.out.println(SCD30_I2C_ADDRESS);
    }



    int checkCRC8(int[] byteData, int length){
        byte crc = (byte)0xff;
        for (int i = 0; i < length ; i++) {
            crc ^= byteData[i];
            for (int bit = 0; bit < 8; bit++) {
                if ((crc & 0x80) != 0){
                    crc = (byte)((crc << 1)^SCD30_POLYNOMIAL);
                }else{
                    crc <<= 1;
                }
            }
        }
        return Byte.toUnsignedInt(crc);
    }
}
