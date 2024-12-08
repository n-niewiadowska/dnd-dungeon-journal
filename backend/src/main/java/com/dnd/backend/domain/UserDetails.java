package com.dnd.backend.domain;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDetails {
    @NotNull
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters long")
    private String username;

    @NotNull
    @Email
    private String email;

    @NotNull
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).+$", 
             message = "Password must contain at least one lowercase letter, one uppercase letter, one number, and one special character.")
    private String password;
}
