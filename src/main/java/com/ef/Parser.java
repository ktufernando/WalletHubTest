package com.ef;

import com.ef.model.DurationTypes;
import com.ef.service.ParserService;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

@ComponentScan(basePackages="com.ef")
@SpringBootApplication
public class Parser implements ApplicationRunner{

	private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

	@Autowired
	private ParserService parserService;

	public static void main(String[] args) {
		SpringApplication.run(Parser.class, args);
	}

	public void run(ApplicationArguments args){
		LOGGER.info("Application started with command-line arguments: {}", Arrays.toString(args.getSourceArgs()));
		for (String name : args.getOptionNames()){
			LOGGER.info(String.format("arg-%s=%s", name, args.getOptionValues(name)));
		}
		if (validateArgs(args)){
			parserService.accessProcess(
					args.getOptionValues("accessLog").get(0),
					args.getOptionValues("startDate").get(0),
					args.getOptionValues("duration").get(0),
					args.getOptionValues("threshold")!=null?args.getOptionValues("threshold").get(0):null);
		}
	}

	private boolean validateArgs(ApplicationArguments args){
		boolean valid = true;
		List<String> messages = Lists.newArrayList();
		if(!args.containsOption("accessLog")){
			messages.add("Constraint violation. Empty parameter: accesslog");
			valid = false;
		}
		if(!args.containsOption("startDate")){
			messages.add("Constraint violation. Empty parameter: startDate");
			valid = false;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
			try {
				sdf.parse( args.getOptionValues("startDate").get(0));
			} catch (ParseException ex) {
				messages.add("Constraint violation. startDate parameter must be: " + "yyyy-MM-dd.HH:mm:ss");
				valid = false;
			}
		}
		if(!args.containsOption("duration")){
			messages.add("Constraint violation. Empty parameter: duration");
			valid = false;
		}else {
			try{
				DurationTypes.valueOf(args.getOptionValues("duration").get(0).toUpperCase());
			}catch (IllegalArgumentException ex){
				messages.add("Constraint violation. Duration must be: daily or hourly");
				valid = false;
			}
		}
		if(args.containsOption("threshold")){
			try{
				Integer.parseInt(args.getOptionValues("threshold").get(0));
			}catch (Exception ex){
				messages.add("Constraint violation. Threshold must be a number");
				valid = false;
			}
		}
		messages.stream().forEach((m) -> LOGGER.error(m));
		return valid;
	}

}
