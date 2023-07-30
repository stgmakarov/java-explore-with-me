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
    private String description;
    @Size(min = 20, max = 2000)
    private String annotation;
    private Integer category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120)
    private String title;

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
