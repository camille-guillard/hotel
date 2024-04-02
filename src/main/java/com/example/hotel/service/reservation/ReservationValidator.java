package com.example.hotel.service.reservation;


import com.example.hotel.dto.ReservationRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;


@Service
public class ReservationValidator {

    private Validator validator;

    @Autowired
    public ReservationValidator(Validator validator) {
        this.validator = validator;
    }

    void validate(ReservationRequestDto value) throws ReservationException {
        Errors errors = this.validator.validateObject(value);
        this.validator.validate(value, errors);
        if (errors.hasErrors()) {
            throw new ReservationException(Objects.requireNonNull(errors.getGlobalError()).getDefaultMessage());
        }
        if (value.getReservationEndDate().isBefore(value.getReservationStartDate())) {
            throw new ReservationException("Reservation end date is before reservation start date");
        }
    }
}