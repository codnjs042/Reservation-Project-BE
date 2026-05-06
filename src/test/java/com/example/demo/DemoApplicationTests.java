package com.example.demo;

import com.example.demo.global.infra.vworld.VWorldClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private VWorldClient vWorldClient;

	@Test
	void getAreaTest() {
		// 호출
		vWorldClient.getArea("11");
	}

}
