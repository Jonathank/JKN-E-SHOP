package nana.shop.online;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import nana.shop.online.config.DotenvConfig;
@ComponentScan
@SpringBootApplication
public class JknEShopApplication {

	public static void main(String[] args) {
	   DotenvConfig.loadEnvToSystem();
	    SpringApplication.run(JknEShopApplication.class, args);
	}

}
