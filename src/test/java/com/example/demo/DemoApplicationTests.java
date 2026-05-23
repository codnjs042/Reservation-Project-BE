package com.example.demo;

import com.example.demo.global.infra.vworld.VWorldClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	void connect(){
		redisTemplate.opsForValue().set("key","value");
		String value = redisTemplate.opsForValue().get("key");
		System.out.println(value);
	}

	@Autowired
	private VWorldClient vWorldClient;

	@Test
	void getAreaTest() {
		// 호출
		vWorldClient.getArea("11");
	}
}
