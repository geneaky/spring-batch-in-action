package me.study.springbatchinaction.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.lang.Nullable;

public class CustomJobParameterValidator implements JobParametersValidator {

    @Override
    public void validate(@Nullable JobParameters parameters) throws JobParametersInvalidException {
        if(parameters == null) {
            throw new JobParametersInvalidException("Job parameters cannot be null");
        }

        if(parameters.getString("name") == null) {
            throw new JobParametersInvalidException("name parameter is required");
        }

    }
}
