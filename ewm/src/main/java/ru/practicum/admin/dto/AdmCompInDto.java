package ru.practicum.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdmCompInDto {
    List<Integer> events;
    Boolean pinned;
    @Size(max=50)
    String title;
}
