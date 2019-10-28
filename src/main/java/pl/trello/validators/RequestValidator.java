package pl.trello.validators;

import pl.trello.dto.request.Request;

import pl.trello.validators.ValidatedRequest;

public interface RequestValidator<T extends Request> {

     ValidatedRequest validate(T request);

}
