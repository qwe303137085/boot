package cn.zilanxuan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//@ComponentScan("cn.zilanxuan.config")
@SpringBootApplication
public class StartApplication {
	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}
}
