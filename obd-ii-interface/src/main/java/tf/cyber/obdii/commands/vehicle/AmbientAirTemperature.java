package tf.cyber.obdii.commands.vehicle;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;

public class AmbientAirTemperature extends OBD2Command<Integer> {
    @Override
    public String command() {
        return "01 46";
    }

    @Override
    public Integer result() {
        int [] bytes = ByteUtils.extractBytes(rawData);

        return bytes[bytes.length - 1] - 40;
    }

    @Override
    public String getFriendlyName() {
        return "Ambient Air Temperature";
    }
}
