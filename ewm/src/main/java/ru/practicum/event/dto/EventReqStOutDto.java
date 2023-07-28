package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.Collection;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventReqStOutDto {
    Collection<ParticipationRequestDto> confirmedRequests;
    Collection<ParticipationRequestDto> rejectedRequests;
}
