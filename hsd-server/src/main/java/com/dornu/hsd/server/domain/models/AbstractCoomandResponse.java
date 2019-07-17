package com.dornu.hsd.server.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class AbstractCoomandResponse implements Cloneable {

    private final Status status;
    private final String clientRequestId;
    private final String hsdResponseId;
    private final ErrorDetails error;
    private final String processedAt;

    public Object clone()throws CloneNotSupportedException{
        return (AbstractCoomandResponse)super.clone();
    }
}
