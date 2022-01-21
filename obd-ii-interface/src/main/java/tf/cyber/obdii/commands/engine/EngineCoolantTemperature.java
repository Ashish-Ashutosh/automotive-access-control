package tf.cyber.obdii.commands.engine;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;

import java.util.Arrays;

public class EngineCoolantTemperature extends OBD2Command<Double> {
    @Override
    public String command() {
        return "01 05";
    }

    @Override
    public Double result() {
        System.out.println(rawData);
        System.out.println(Arrays.toString(ByteUtils.extractBytes(rawData)));
        // TODO: Implement this.
        return 0d;
    }

    @Override
    public String getFriendlyName() {
        return "Engine coolant temperature";
    }
}
