package com.globant.demoschedule.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
@Data
@Builder
public class People
{
    @Id
    private String id;

    private String fistName;

    private String lastName;

    private State state;
}
