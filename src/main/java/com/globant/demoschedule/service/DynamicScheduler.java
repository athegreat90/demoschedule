package com.globant.demoschedule.service;

import com.globant.demoschedule.entity.Config;
import com.globant.demoschedule.repo.ConfigRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Service
@RequiredArgsConstructor
@Log4j2
public class DynamicScheduler implements SchedulingConfigurer
{
    private final ConfigRepo repo;

    @PostConstruct
    private void init()
    {
        var configSeg = new Config();
        configSeg.setName("TIME_REFRESH_IN_SEG");
        configSeg.setValue("15");

        var resultSeg = repo.save(configSeg);
        log.info("Result: " + resultSeg);
    }

    @PreDestroy
    private void clean()
    {
        repo.deleteAll();
    }

    @Bean
    public TaskScheduler poolScheduler()
    {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        scheduler.setPoolSize(1);
        scheduler.initialize();
        return scheduler;
    }

    // We can have multiple tasks inside the same registrar as we can see below.
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        taskRegistrar.setScheduler(poolScheduler());

        // Next execution time is taken from DB, so if the value in DB changes, next execution time will change too.
        Config timeRefreshInSeg = repo.getConfigByName("TIME_REFRESH_IN_SEG");
        log.info(timeRefreshInSeg);
        taskRegistrar.addTriggerTask(executeRunner(), executeTrigger());
    }

    // The action to execute each time
    private Runnable executeRunner()
    {
        return () -> scheduledDatabase(repo.getConfigByName("TIME_REFRESH_IN_SEG").getValue());
    }

    // Set the next execution time
    private Trigger executeTrigger()
    {
        return t ->
        {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = t.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.SECOND, Integer.parseInt(repo.getConfigByName("TIME_REFRESH_IN_SEG").getValue()));
            return nextExecutionTime.getTime();
        };
    }

    public void scheduledDatabase(String time)
    {
        log.info("scheduledDatabase: Next execution time of this will be taken from DB -> {}", time);
    }

}
