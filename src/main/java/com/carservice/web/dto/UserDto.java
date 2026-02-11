package com.carservice.web.dto;

import com.carservice.data.entities.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserDto {
    private Long user_id;
    @NotEmpty(message = "Username field is required!")
    private String username;
    @NotEmpty(message = "Email field is required!")
    private String email;
    @NotEmpty(message = "Password field is required!")
    private String password;
    @NotEmpty(message = "First Name field is required!")
    @Column(name = "firstName")
    private String firstName;
    @NotEmpty(message = "Last Name field is required!")
    private String lastName;
    @NotEmpty(message = "PhoneNumber field is required!")
    @Pattern(regexp = "(^$|[0-9]{10})", message = "Phone number is not in the correct format.")
    private String phoneNumber;
    private Timestamp creationTime;
    private Boolean isEmployee;
    private Role role_id;
}
