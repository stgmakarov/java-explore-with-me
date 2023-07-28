package ru.practicum.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.state.NewEventState;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEventInDto {
    @Size(min = 20, max = 7000)
    String description;
    @Size(min = 20, max = 2000)
    String annotation;
    Integer category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    Boolean paid;
    Integer participantLimit;
    Boolean requestModeration;
    String stateAction;
    @Size(min = 3, max = 120)
    String title;

    public NewEventState getStateAction() {
        try {
            return NewEventState.valueOf(stateAction);
        } catch (Exception ignored) {
            return NewEventState.SEND_TO_REVIEW;
        }
    }

    public void setStateAction(String stateAction) {
        if (stateAction != null)
            this.stateAction = stateAction;
        else this.stateAction = NewEventState.SEND_TO_REVIEW.toString();
    }
}
