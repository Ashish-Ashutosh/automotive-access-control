package tf.cyber.thesis.automotiveaccesscontrol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.net.URISyntaxException;

@SpringBootApplication
@EnableAutoConfiguration
public class AutomotiveAccessControlApplication {
	public static void main(String[] args) throws IOException, URISyntaxException {
		SpringApplication.run(AutomotiveAccessControlApplication.class, args);

		//XACMLInterceptor.interceptRequestToXACML();
	}

}