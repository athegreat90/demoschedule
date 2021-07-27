package com.globant.demoschedule.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@Log4j2
@RequiredArgsConstructor
public class DemoController
{
    @Scheduled(cron = "#{@getConfigRefreshValue}")
    public void validateChangeCronTab()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        Date now = new Date();
        String strDate = sdf.format(now);
        log.info("\nFixed Rate scheduler:: " + strDate);
    }

    @GetMapping("/")
    public String getData()
    {
        return "OK";
    }

}
