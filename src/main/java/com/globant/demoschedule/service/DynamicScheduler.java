package com.globant.demoschedule.service;

import com.globant.demoschedule.entity.Config;
import com.globant.demoschedule.entity.People;
import com.globant.demoschedule.entity.State;
import com.globant.demoschedule.repo.ConfigRepo;
import com.globant.demoschedule.repo.PeopleRepo;
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
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class DynamicScheduler implements SchedulingConfigurer
{
    private final ConfigRepo configRepo;
    private final PeopleRepo peopleRepo;

    @PostConstruct
    private void init()
    {
        var configSeg = new Config();
        configSeg.setName("TIME_REFRESH_IN_SEG");
        configSeg.setValue("15");

        var resultSeg = configRepo.save(configSeg);
        log.info("Result: " + resultSeg);

        var demoConfig = List.of(new Config("STATE", "Santander"), new Config("STATE", "Antioquia"));
        var result = configRepo.saveAll(demoConfig);
        log.info(result);


        var bucaramanga = State.builder().name("Santander").city("Bucaramanga").build();
        var sanAndresCity = State.builder().name("San Andres").city("San Andres city").build();
        var medellin = State.builder().name("Antioquia").city("Medellin").build();
        var peopleOne = People.builder().fistName("Pepito").lastName("Perez").state(bucaramanga).build();
        var peopleTwo = People.builder().fistName("Diego").lastName("Perez").state(bucaramanga).build();
        var peopleThree = People.builder().fistName("Juanito").lastName("Perez").state(medellin).build();
        var peopleFour = People.builder().fistName("Juanita").lastName("Perez").state(sanAndresCity).build();
        var peopleArray = List.of(peopleOne, peopleTwo, peopleThree, peopleFour);

        var peopleResult = peopleRepo.saveAll(peopleArray);

        log.info(peopleResult);
    }

    @PreDestroy
    private void clean()
    {
        configRepo.deleteAll();
        peopleRepo.deleteAll();
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
        Config timeRefreshInSeg = configRepo.getConfigByName("TIME_REFRESH_IN_SEG");
        log.info(timeRefreshInSeg);
        taskRegistrar.addTriggerTask(executeRunner(), executeTrigger());
    }

    // The action to execute each time
    private Runnable executeRunner()
    {
        return () -> scheduledDatabase(configRepo.getConfigByName("TIME_REFRESH_IN_SEG").getValue());
    }

    // Set the next execution time
    private Trigger executeTrigger()
    {
        return t ->
        {
            Calendar nextExecutionTime = new GregorianCalendar();
            Date lastActualExecutionTime = t.lastActualExecutionTime();
            nextExecutionTime.setTime(lastActualExecutionTime != null ? lastActualExecutionTime : new Date());
            nextExecutionTime.add(Calendar.SECOND, Integer.parseInt(configRepo.getConfigByName("TIME_REFRESH_IN_SEG").getValue()));
            return nextExecutionTime.getTime();
        };
    }

    public void scheduledDatabase(String time)
    {
        log.info("Size of {} is {}", "STATE in level 0", configRepo.countConfigByName("STATE"));
        log.info("Size of {} is {}", "Name of state Antioquia in level 1", peopleRepo.countPeopleByStateName("Antioquia"));
        log.info("Size of {} is {}", "Name of state Santander in level 1", peopleRepo.countPeopleByStateName("Santander"));

        log.info("Size of {} is {}", "Name of state Santander in level 1 and regex start with 'San'", peopleRepo.countPeopleByStateNameRegex("^San"));
        log.info("Size of {} is {}", "Name of state Santander in level 1 and Contains 'Santander'", peopleRepo.countPeopleByStateNameContains("Santander"));
        log.info("scheduledDatabase: Next execution time of this will be taken from DB -> {}\n", time);
    }

}
