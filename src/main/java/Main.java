public class Main {
    public static void main(String[] args) {
        SCD30Impl scd30 = new SCD30Impl();
        int[] byteData = {0xbe,0xef};

        int decCRC8 = scd30.checkCRC8(byteData,2);
        String hexCRC8 = Integer.toHexString(decCRC8);
        System.out.println(hexCRC8);



    }
}
