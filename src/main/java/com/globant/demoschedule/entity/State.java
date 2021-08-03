package com.globant.demoschedule.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class State
{
    private String name;
    private String city;
}
