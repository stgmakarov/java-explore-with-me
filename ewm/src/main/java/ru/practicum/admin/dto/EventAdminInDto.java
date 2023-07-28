package ru.practicum.admin.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.location.dto.LocationDto;
import ru.practicum.state.ActionState;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventAdminInDto {
    @Size(min = 20, max = 2000)
    String annotation;
    Integer category;
    @Size(min = 20, max = 7000)
    String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime eventDate;
    LocationDto location;
    boolean paid;
    Integer participantLimit;
    boolean requestModeration;
    String stateAction;
    @Size(min = 3, max = 120)
    String title;

    public ActionState getStateAction() {
        try {
            return ActionState.valueOf(stateAction);
        } catch (Exception e) {
            return ActionState.PUBLISH_EVENT;
        }
    }

    public void setStateAction(String stateAction) {
        if (stateAction != null)
            this.stateAction = stateAction;
        else
            this.stateAction = ActionState.PUBLISH_EVENT.toString();
    }
}
