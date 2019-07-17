package com.dornu.hsd.server.domain.commands.template.builder;

import com.dornu.hsd.server.domain.commands.AbstractCommand;

import java.util.Set;

public class BaseCommandTemplate<T, R> {

    private AbstractCommand<T, R> command;
    private final T request;
    private R response;
    private R violationsErrorResponse;

    private BaseCommandTemplate(
            AbstractCommand<T, R> command,
            T request
    ) {
        this.command = command;
        this.request = request;
    }

    public static <E, F> BaseCommandTemplate<E,F> templateBuilder(
            AbstractCommand<E, F> command,
            E requestModel
    ) {
        return new BaseCommandTemplate<E,F>(command, requestModel);
    }

    public BaseCommandTemplate<T,R> preHandle() {
        command.preHandle(request);
        return this;
    }

    public BaseCommandTemplate<T,R> doDataValidation() throws Exception {
        Set<String> violations = command.doDataValidation(request);
        if (!violations.isEmpty()) {
            violationsErrorResponse = command.handleValidationError(request, violations);
        }
        return this;
    }

    public BaseCommandTemplate<T,R> doBusinessValidation() throws Exception {
        if (violationsErrorResponse == null){
            Set<String> violations = command.doBusinessValidation(request);
            if (!violations.isEmpty()) {
                violationsErrorResponse = command.handleValidationError(request, violations);
            }
        }
        return this;
    }

    public BaseCommandTemplate<T,R> handle() {
        if (violationsErrorResponse == null){
            response = command.handle(request);
        }
        return this;
    }

    public BaseCommandTemplate<T,R> postHandle() {
        if (violationsErrorResponse == null){
            command.postHandle(request, response);
        }
        return this;
    }

    public R resolve() {
        return violationsErrorResponse == null ? response : violationsErrorResponse;
    }
}
