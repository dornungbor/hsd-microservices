package com.dornu.hsd.server.domain.commands;

import com.dornu.hsd.server.domain.commands.template.builder.BaseCommandTemplate;

import java.util.Set;

public abstract class AbstractCommand<T ,R> {

    public R execute(T requestModel ) {
        try {
            return BaseCommandTemplate.
                    templateBuilder(this, requestModel)
                    .preHandle()
                    .doDataValidation()
                    .doBusinessValidation()
                    .handle()
                    .postHandle()
                    .resolve();
        } catch (Exception exception) {
            return this.handleError(requestModel, (R) null, exception);
        }
    }

    public abstract Set<String> doDataValidation(T requestModel);

    public abstract Set<String> doBusinessValidation(T requestModel);

    public abstract R handle(T requestModel);

    public abstract void preHandle(T requestModel);

    public abstract void postHandle(T requestModel, R responseModel);

    public abstract R handleError(T requestModel, R responseModel, Exception exception);

    public abstract R handleValidationError(T requestModel, Set<String> violations);

}
