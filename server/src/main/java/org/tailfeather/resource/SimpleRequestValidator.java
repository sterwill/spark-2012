package org.tailfeather.resource;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.stereotype.Component;

@Component
public class SimpleRequestValidator {
	private Validator validator;

	public SimpleRequestValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		this.validator = factory.getValidator();
	}

	public <T> Response validate(T entity) {
		Set<ConstraintViolation<T>> violations = validator.validate(entity);
		if (violations.size() > 0) {
			StringBuilder sb = new StringBuilder();
			boolean doneOne = false;
			for (ConstraintViolation<T> v : violations) {
				if (doneOne) {
					sb.append("\n");
				}
				doneOne = true;

				sb.append(v.getMessage());
			}
			return Response.status(Status.BAD_REQUEST).entity(sb.toString()).build();
		}
		return null;
	}
}
