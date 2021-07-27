package com.globant.demoschedule.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
@Data
public class Config
{
    @Id
    private String id;

    private String name;
    private String value;
}
