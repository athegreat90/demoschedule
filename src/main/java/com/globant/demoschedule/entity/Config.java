package com.globant.demoschedule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "config")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config
{
    @Id
    private String id;

    private String name;
    private String value;

    public Config(String name, String value) {
        this.name = name;
        this.value = value;

    }
}
