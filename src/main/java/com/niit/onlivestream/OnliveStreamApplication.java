package com.niit.onlivestream;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.niit.onlivestream.mapper")
public class OnliveStreamApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnliveStreamApplication.class, args);
	}

}
