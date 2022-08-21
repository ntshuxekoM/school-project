package com.phishing.app.bean;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
@Component
public class CommandLineRunnerBean implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CommandLineRunnerBean.class);	
    public void run(String... args) {
    	String strArgs = Arrays.stream(args).collect(Collectors.joining("|"));
    	logger.info("Application started with arguments:" + strArgs);
    }
}