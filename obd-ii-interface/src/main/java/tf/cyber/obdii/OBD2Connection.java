package tf.cyber.obdii;

import jssc.SerialPort;
import jssc.SerialPortException;
import tf.cyber.obdii.commands.debug.ELMVersionInformation;
import tf.cyber.obdii.exceptions.TimeoutException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class OBD2Connection {

    public static final int TIMEOUT_MSEC = 2000;

    private SerialPort port;

    public static void main(String[] args) throws IOException, SerialPortException, InterruptedException {
        OBD2Connection conn = new OBD2Connection("/dev/ttyUSB0");
        conn.write(new ELMVersionInformation());

        String res = conn.read();
        System.out.println(res);
        conn.close();
    }

    private String read() throws SerialPortException {
        final StringBuilder buf = new StringBuilder();
        final long start = System.currentTimeMillis();

        while (true) {
            if (start + TIMEOUT_MSEC < System.currentTimeMillis()) {
                throw new TimeoutException("Timed out while reading from serial console!");
            }

            // Is new data available on the serial console?
            // If no, wait and go do something else.
            if (port.getInputBufferBytesCount() == 0) {
                Thread.yield();
            } else {
                byte[] data = port.readBytes();
                String str = new String(data);

                buf.append(str);

                // Has the terminal symbol appeared?
                // If so, we're done reading.
                // Only keep the last line, though.
                if (str.endsWith(">")) {
                    buf.setLength(buf.length() - 1);

                    String[] intermediate = buf.toString().trim().split("\r");
                    return intermediate[intermediate.length - 1];
                }
            }
        }
    }

    public void write(OBD2Command command) throws SerialPortException {
        port.writeBytes((command.getCommand()+ "\r").getBytes(StandardCharsets.US_ASCII));
    }

    public void close() throws SerialPortException {
        port.closePort();
    }

    public OBD2Connection(String portIdentifier) throws IOException, SerialPortException, InterruptedException {
        this.port = new SerialPort("/dev/ttyUSB0");
        port.openPort();
        port.setParams(SerialPort.BAUDRATE_38400,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        port.setDTR(true);
        port.setRTS(true);
    }
}
