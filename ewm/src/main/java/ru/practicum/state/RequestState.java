package ru.practicum.state;

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue;

public enum RequestState {
    CONFIRMED, REJECTED, PENDING, CANCELED
}
