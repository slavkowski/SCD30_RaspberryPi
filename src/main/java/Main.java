import com.pi4j.util.Console;

public class Main {

    public static void main(String[] args) {
        Console console = new Console();
        SCD30Impl scd30 = new SCD30Impl();
        scd30.initializeSCD30();
        float[] enData = scd30.readMeasurement();


            console.println("CO2 : " + enData[0] + " ppm");
            console.println("TEMP : " + enData[1] + " C");
            console.println("HYDRO : " + enData[2] + " %");

    }
}
