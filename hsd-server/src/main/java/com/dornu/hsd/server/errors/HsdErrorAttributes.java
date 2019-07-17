package com.dornu.hsd.server.errors;

import com.dornu.hsd.server.domain.models.ErrorCode;
import com.dornu.hsd.server.domain.models.ErrorDetails;
import com.dornu.hsd.server.domain.models.Status;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@Component
public class HsdErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace);
        ErrorDetails.ErrorDetailsBuilder builder = ErrorDetails.builder();
        switch (HttpStatus.valueOf((int)errorAttributes.get("status"))){
            case UNAUTHORIZED:
            case FORBIDDEN:
                builder.code(ErrorCode.ACCESS_DENIED).message((String)errorAttributes.get("message"));
                break;
            case INTERNAL_SERVER_ERROR:
                builder.code(ErrorCode.SOMETHING_WENT_WRONG).message(ErrorCode.SOMETHING_WENT_WRONG.getDefaultMessage());
                break;
            default:
                return errorAttributes;
        }
        Map<String, Object> customError = new HashMap<>();
        customError.put("status", Status.FAILURE);
        customError.put("error", builder.build());
        return customError;
    }
}

