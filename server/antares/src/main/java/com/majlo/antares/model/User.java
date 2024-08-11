package com.majlo.antares.model;


import com.majlo.antares.enums.MainRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@Table(name = "antares_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "First name cannot be empty")
    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    @NotNull
    @Column(name = "last_name")
    private String lastName;

//    @NotEmpty(message = "Email cannot be empty")
//    @Email(message = "Email should be valid")
//    @NotNull
//    private String email;

    @NotEmpty(message = "Login cannot be empty")
    @NotNull
    private String login;

    @NotEmpty(message = "Password cannot be empty")
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @NotNull
    private MainRole role;
}
