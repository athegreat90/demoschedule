package com.globant.demoschedule.core;

import com.globant.demoschedule.service.IConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Log4j2
public class DemoExecute
{
    private final IConfigService configService;

    @Bean
    public String getConfigRefreshValue()
    {
        var time = configService.get("TIME_REFRESH");
        log.info("Time = " + time);
        return time == null ? "*/15 * * ? * *" : time.getValue();
    }

}
