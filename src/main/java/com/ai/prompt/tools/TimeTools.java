package com.ai.prompt.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.time.ZoneId;

@Component
public class TimeTools {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeTools.class);

    @Tool(name="getCurrentLocalTime", description = "Get the current time in user's timezone")
    public String getCurrentLocalTime(){
        LOGGER.info("Returning the user's current timezone");
        return LocalTime.now().toString();
    }

    @Tool(name="getCurrentTimeByZone", description = "Get the current time in specified time zone")
    public String getCurrentTimeByZone(@ToolParam(description = "Value represent the time zone") String timeZone){
        LOGGER.info("Returning the currenttime in the timezone {}", timeZone);
        return LocalTime.now(ZoneId.of(timeZone)).toString();
    }
}
