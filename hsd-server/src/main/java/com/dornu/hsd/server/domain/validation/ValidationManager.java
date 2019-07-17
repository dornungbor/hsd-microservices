package com.dornu.hsd.server.domain.validation;

import java.util.Set;

public interface ValidationManager<T> {
    Set<String> validate(T request);
}
