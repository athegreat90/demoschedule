package com.globant.demoschedule.service;

import com.globant.demoschedule.entity.Config;
import com.globant.demoschedule.repo.IConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConfigService implements IConfigService
{
    private final IConfigRepo repo;

    @PostConstruct
    private void init()
    {
        var config = new Config();
        config.setName("TIME_REFRESH");
        config.setValue("*/15 * * * * ?");

        var result = repo.save(config);
        System.out.println("Result: " + result.getId());
    }

    @Override
    public List<Config> getAll()
    {
        return repo.findAll();
    }

    @Override
    public Config get(String property)
    {
        return repo.getConfigByName(property);
    }
}
