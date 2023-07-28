package ru.practicum.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInDto {
    @NotNull
    @Size(min=2,max=250)
    @NotBlank
    String name;
    @NotNull
    @Email
    @NotBlank
    @Size(min=6,max=254)
    String email;
}
