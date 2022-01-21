package tf.cyber.obdii.commands.engine;

import tf.cyber.obdii.commands.OBD2Command;
import tf.cyber.obdii.util.ByteUtils;
import tf.cyber.obdii.util.Pair;

public class OxygenSensor2 extends OBD2Command<Pair<Double, Double>> {
    @Override
    public String command() {
        return "01 15";
    }

    @Override
    public Pair<Double, Double> result() {
        int[] bytes = ByteUtils.extractBytes(rawData);

        int a = bytes[bytes.length - 2];
        int b = bytes[bytes.length - 1];

        double voltage = a / 200d;
        double stft = ((100/128d) * b) - 100;

        return Pair.of(voltage, stft);
    }

    @Override
    public String getFriendlyName() {
        return "Oxygen Sensor 2 - (Voltage, Short term fuel trim)";
    }
}
