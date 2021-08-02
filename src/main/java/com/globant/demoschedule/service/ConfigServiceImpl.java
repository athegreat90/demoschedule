package com.globant.demoschedule.service;

import com.globant.demoschedule.entity.Config;
import com.globant.demoschedule.repo.ConfigRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ConfigServiceImpl implements ConfigService
{
    private final ConfigRepo repo;



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
