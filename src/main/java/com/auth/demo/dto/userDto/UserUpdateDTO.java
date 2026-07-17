package com.auth.demo.dto.userDto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserUpdateDTO {


    @Size(max = 50)
    private String firstname;

    @Size(max = 50)
    private String lastname;
}