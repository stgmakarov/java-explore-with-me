package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.state.RequestState;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventInDto {
    private Collection<Integer> requestIds;
    private String status;

    public RequestState getStatus() {
        if (status != null) {
            if (!status.isEmpty())
                return RequestState.valueOf(status);
            else return RequestState.PENDING;
        } else return RequestState.PENDING;
    }
}
