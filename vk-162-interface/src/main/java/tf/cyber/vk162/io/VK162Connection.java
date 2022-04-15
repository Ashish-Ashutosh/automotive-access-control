package tf.cyber.vk162.io;

import com.github.jkschneider.netty.jssc.simple.SimpleLineBasedSerialChannel;
import tf.cyber.vk162.data.GPSData;
public class VK162Connection {
    private static VK162Connection connection;

    private SimpleLineBasedSerialChannel channel;
    private GPSData gpsData = GPSData.builder()
                .header("HEADER")
                .antennaAltitudeUnit(GPSData.AltitudeUnit.METER)
                .antennaAltitude(0d)
                .usedSatellites(0)
                .quality(GPSData.GPSQuality.UNCORRECTED_COORDINATE)
                .longitude(0d)
                .latitude(0d)
                .timestamp(0d)
                .build();;

    public static void main(String[] args) {
        init("/dev/ttyACM0");
    }

    public static void init(String portIdentifier) {
        if (connection == null) {
            connection = new VK162Connection(portIdentifier);
        }
    }

    public static VK162Connection getConnection() {
        return connection;
    }

    private VK162Connection(String portIdentifier) {
        // Set update rate to 10Hz (highest supported by this dongle).
        // It actually cannot do 10Hz stable, it appears.
        //byte[] updateRateCmd = {(byte) 0xB5, 0x62, 0x06, 0x08, 0x06, 0x00,
        //        0x64, 0x00, 0x01, 0x00, 0x01, 0x00, 0x7A, 0x12};

        this.channel = new SimpleLineBasedSerialChannel(portIdentifier,
                                                        (ctx, msg) -> {
                                                            synchronized (gpsData) {
                                                                if (msg.startsWith("$GPGGA")) {
                                                                    try {
                                                                        handleGPSData(msg);
                                                                    } catch (Exception e) {
                                                                        System.err.println("Failed to parse GPS data. Check signal.");
                                                                    }
                                                                }
                                                            }
                                                        });

        //this.channel.write(Unpooled.wrappedBuffer(updateRateCmd));
    }

    private void handleGPSData(String rawData) {
        String[] fragments = rawData.split(",");

        String header = fragments[0];
        double timestamp = Double.parseDouble(fragments[1]);

        double latitude = -1.0d;
        if (!fragments[2].equals("")) {
            latitude = calculateLatLonFromNMEA(fragments[2]);
        }

        double longitude = -1.0d;
        if (!fragments[4].equals("")) {
            longitude = calculateLatLonFromNMEA(fragments[4]);
        }

        GPSData.GPSQuality quality =
                GPSData.GPSQuality.fromResponse(Integer.parseInt(fragments[6]));
        int satelliteCount = Integer.parseInt(fragments[7]);

        double antennaAltitude = -1;
        GPSData.AltitudeUnit antennaAltitudeUnit = null;

        if (!fragments[9].equals("")) {
            antennaAltitude = Double.parseDouble(fragments[9]);
            antennaAltitudeUnit = GPSData.AltitudeUnit.fromResponse(fragments[10]);
        }

        gpsData.setHeader(header);
        gpsData.setTimestamp(timestamp);
        gpsData.setLatitude(latitude);
        gpsData.setLongitude(longitude);
        gpsData.setQuality(quality);
        gpsData.setUsedSatellites(satelliteCount);
        gpsData.setAntennaAltitude(antennaAltitude);
        gpsData.setAntennaAltitudeUnit(antennaAltitudeUnit);
    }

    public synchronized GPSData getGpsData() {
        return this.gpsData;
    }

    public void close() {
        this.channel.close();
        connection = null;
    }

    private static double calculateLatLonFromNMEA(String input) {
        String[] parts = input.split("\\.");
        String first = parts[0];
        String second = parts[1];

        // Two last second digits in first are minutes, rest are hours
        int digitCount = first.length();
        int digitSplit = digitCount == 4 ? 2 : 3;

        int hours = Integer.parseInt(first.substring(0, digitSplit));
        double minutes = Integer.parseInt(first.substring(digitSplit));

        // Second part are minute decimal places.
        minutes += Double.parseDouble("0." + second);

        return hours + (minutes / 60);
    }
}
