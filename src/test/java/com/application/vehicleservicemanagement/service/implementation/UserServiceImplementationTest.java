package com.application.vehicleservicemanagement.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.RegisterRequest;
import com.application.vehicleservicemanagement.dto.ServiceAdvisorDto;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.UserRepository;

public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImplementation userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setPassword("password");

        User user = new User();
        user.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encodedPassword"); // Mock PasswordEncoder method

        ApiResponse expectedResponse = ApiResponse.builder().message("User registered successfully !!").status("Success").build();
        ApiResponse actualResponse = userService.createUser(registerRequest);
        assertEquals(expectedResponse, actualResponse);

        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password");
    }

    @Test
    void testGetUserById(){
        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        UserDto expectedUserDto = new UserDto();
        expectedUserDto.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(modelMapper.map(user, UserDto.class)).thenReturn(expectedUserDto);

        UserDto actualUserDto = userService.getUserById(userId);

        assertEquals(expectedUserDto, actualUserDto);
    }

    @Test
    void testGetAllUsers(){
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());

        List<UserDto> expectedUserDtoList = new ArrayList<>();
        expectedUserDtoList.add(new UserDto());
        expectedUserDtoList.add(new UserDto());

        when(userRepository.findAll()).thenReturn(userList);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

        List<UserDto> actualUserDtoList = userService.getAllUsers();

        assertEquals(expectedUserDtoList.size(), actualUserDtoList.size());
    }

    @Test
    void testGetAllServiceAdvisors(){
        List<User> serviceAdvisorList = new ArrayList<>();
        User user1=new User();
        user1.setId(1L);
        user1.setFirstName("Advisor1");
        user1.setRole(Role.SERVICE_ADVISOR);
        serviceAdvisorList.add(user1);

        when(userRepository.findAllByRole(Role.SERVICE_ADVISOR)).thenReturn(serviceAdvisorList);
        when(modelMapper.map(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

        List<ServiceAdvisorDto> actualServiceAdvisorDtoList = userService.getAllServiceAdvisors();

        assertEquals(1, actualServiceAdvisorDtoList.size());

    }

    @Test
    void testUpdateUserById(){
        UserDto userDto = new UserDto();
        userDto.setId(1L);

        User existingUser = new User();
        existingUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);
        when(modelMapper.map(any(UserDto.class), eq(User.class))).thenReturn(existingUser);

        ApiResponse expectedResponse = ApiResponse.builder().message("User Updated successfully.").status("Success").build();
        ApiResponse actualResponse = userService.updateUserById(1L, userDto);
        assertEquals(expectedResponse, actualResponse);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUserById(){
        Long userId = 1L;
        User userToDelete = new User();
        userToDelete.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(userToDelete));

        ApiResponse response = userService.deleteUserById(userId);

        verify(userRepository, times(1)).delete(userToDelete);
        assertEquals("User deleted successfully.", response.getMessage());
        assertEquals("Success", response.getStatus());
    }


    @Test
    public void testDeleteUserByIdNotExixting() {
        Long userId = 100L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUserById(userId));
        verify(userRepository, never()).delete(any());
    }
}
