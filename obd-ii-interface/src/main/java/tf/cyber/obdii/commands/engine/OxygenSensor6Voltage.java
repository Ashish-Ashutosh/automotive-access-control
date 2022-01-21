package tf.cyber.obdii.commands.engine;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;

public class OxygenSensor6Voltage extends OBD2Command<Double>{
    @Override
    public String command() {
        return "01 19";
    }

    @Override
    public Double result() {
        int[] bytes = ByteUtils.extractBytes(rawData);
        return bytes[bytes.length - 2] / 200d;
    }

    @Override
    public String getFriendlyName() {
        return "Oxygen Sensor 6 - Voltage";
    }
}
