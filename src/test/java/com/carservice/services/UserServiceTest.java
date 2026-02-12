package com.carservice.services;

import com.carservice.data.entities.CarService;
import com.carservice.data.entities.Role;
import com.carservice.data.repositories.CarServiceRepository;
import com.carservice.web.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Mock
    private CarServiceRepository carServiceRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private RoleService roleService;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSucceedIfUserIsPresent() {
        UserDto user = buildUserDto();

        when(carServiceRepository.getCarServiceByName(anyString())).thenReturn(new CarService());
        when(roleService.getRoleByAuthority(anyString())).thenReturn(new Role());
        when(modelMapper.map(any(), any())).thenReturn(new com.carservice.data.entities.User());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("encoded_password");

        userService.createUser(user);
    }

    private UserDto buildUserDto() {
        UserDto dto = new UserDto();
        dto.setEmail("test@test.com");
        dto.setPassword("password");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPhoneNumber("1234567890");
        dto.setIsEmployee(false);
        dto.setCarServiceName("Test Car Service");
        return dto;
    }

}