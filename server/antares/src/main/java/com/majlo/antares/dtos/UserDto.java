package com.majlo.antares.dtos;

import com.majlo.antares.enums.MainRole;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String login;
    private String token;
    private MainRole role;
}
