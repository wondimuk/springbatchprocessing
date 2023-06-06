package com.wk.batchprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessApplication {

	public static void main(String[] args) {
		/*Note that SpringApplication.exit() and System.exit() ensure that the JVM exits upon job completion.*/
		System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessApplication.class, args)));
	}

}
