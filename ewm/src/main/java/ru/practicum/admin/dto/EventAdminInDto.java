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
    private String annotation;
    private Integer category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private LocationDto location;
    private boolean paid;
    private Integer participantLimit;
    private boolean requestModeration;
    private String stateAction;
    @Size(min = 3, max = 120)
    private String title;

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
