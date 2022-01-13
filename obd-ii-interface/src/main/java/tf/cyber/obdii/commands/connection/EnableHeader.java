package tf.cyber.obdii.commands.connection;

import tf.cyber.obdii.commands.OBD2Command;

public class EnableHeader extends OBD2Command<String> {
    @Override
    public String command() {
        return "ath1";
    }

    @Override
    public String result() {
        return rawData;
    }
}
