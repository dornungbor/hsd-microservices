package com.dornu.hsd.server.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public abstract class AbstractCoomandRequest implements Cloneable {

    @NotEmpty
    private String clientRequestId;

    public Object clone()throws CloneNotSupportedException{
        return (AbstractCoomandRequest)super.clone();
    }
}
