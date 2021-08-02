package com.globant.demoschedule.service;

import com.globant.demoschedule.entity.Config;

import java.util.List;

public interface ConfigService
{
    List<Config> getAll();

    Config get(String property);
}
