package com.encooked;

import com.encooked.components.AutowireHelper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.ApplicationContext;

@EnableEurekaClient
@SpringBootApplication
public class AuthorizationApplication {

	public static void main(String[] args) {
            ApplicationContext applicationContext = SpringApplication.run(AuthorizationApplication.class, args);
            AutowireHelper.getInstance().setApplicationContext(applicationContext);
	}

}
