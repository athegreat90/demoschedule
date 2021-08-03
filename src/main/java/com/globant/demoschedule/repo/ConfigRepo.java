package com.globant.demoschedule.repo;

import com.globant.demoschedule.entity.Config;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepo extends MongoRepository<Config, String>
{
    Config getConfigByName(String name);

    Long countConfigByName(String name);
}
