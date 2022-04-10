package tf.cyber.gps.microservice.gpsmicroservice.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import tf.cyber.vk162.data.GPSData;

@Service
@Profile("debug")
public class DebugGPSDataService implements GPSDataService {
    @Override
    public GPSData getGpsData() {
        return GPSData.builder()
                .header("DEBUG")
                .antennaAltitudeUnit(GPSData.AltitudeUnit.METER)
                .antennaAltitude(400d)
                .usedSatellites(5)
                .quality(GPSData.GPSQuality.UNCORRECTED_COORDINATE)
                .longitude(13.43122d)
                .latitude(48.5665d)
                .timestamp(0d)
                .build();
    }
}
