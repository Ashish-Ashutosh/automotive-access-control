package tf.cyber.obdii.commands.connection;

import tf.cyber.obdii.commands.OBD2Command;

public class DisableHeader extends OBD2Command<String> {
    @Override
    public String command() {
        return "ath0";
    }

    @Override
    public String result() {
        return rawData;
    }
}