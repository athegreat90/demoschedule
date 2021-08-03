package com.globant.demoschedule.repo;

import com.globant.demoschedule.entity.People;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PeopleRepo extends MongoRepository<People, String>
{
    Long countPeopleByStateName(String name);

    Long countPeopleByStateNameContains(String name);

//    @Query(value = "{'state.name': {$regex: ?0, $options: 'i'}}", count = true)
    Long countPeopleByStateNameRegex(String regex);
}
