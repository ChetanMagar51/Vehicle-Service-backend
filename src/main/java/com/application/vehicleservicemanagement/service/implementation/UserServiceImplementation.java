package com.application.vehicleservicemanagement.service.implementation;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.application.vehicleservicemanagement.dto.AdvisorAvailabilityDto;
import com.application.vehicleservicemanagement.dto.AdvisorScheduleDto;
import com.application.vehicleservicemanagement.dto.ApiResponse;
import com.application.vehicleservicemanagement.dto.RegisterRequest;
import com.application.vehicleservicemanagement.dto.ServiceAdvisorDto;
import com.application.vehicleservicemanagement.dto.UserDto;
import com.application.vehicleservicemanagement.entity.AdvisorAvailability;
import com.application.vehicleservicemanagement.entity.AdvisorSchedule;
import com.application.vehicleservicemanagement.entity.Role;
import com.application.vehicleservicemanagement.entity.ScheduleStatus;
import com.application.vehicleservicemanagement.entity.User;
import com.application.vehicleservicemanagement.exception.ResourceNotFoundException;
import com.application.vehicleservicemanagement.repository.AdvisorAvailabilityRepository;
import com.application.vehicleservicemanagement.repository.AdvisorScheduleRepository;
import com.application.vehicleservicemanagement.repository.UserRepository;
import com.application.vehicleservicemanagement.service.AdvisorScheduleService;
import com.application.vehicleservicemanagement.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplementation implements UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;

	private final AdvisorAvailabilityRepository advisorAvailabilityRepository;
	
	private final AdvisorScheduleRepository advisorScheduleRepository;
	
	  private final AdvisorScheduleService advisorScheduleService;

//    @Override
//    public ApiResponse createUser(RegisterRequest registerRequest) {
//        User user = User.builder()
//                .firstName(registerRequest.getFirstName())
//                .lastName(registerRequest.getLastName())
//                .phone(registerRequest.getPhone())
//                .address(registerRequest.getAddress())
//                .email(registerRequest.getEmail())
//                .password(passwordEncoder.encode(registerRequest.getPassword()))
//                .scheduledVehicleCount(0)
//                .isAvailable(Boolean.TRUE)
//                .role(Role.SERVICE_ADVISOR)
//                .build();
//        userRepository.save(user);
//        return ApiResponse.builder()
//                .message("User registered successfully !!")
//                .status("Success")
//                .build();
//    }
//	@Override
//	@Transactional
//	public ApiResponse createUser(RegisterRequest registerRequest) {
//
//		User user = User.builder().firstName(registerRequest.getFirstName()).lastName(registerRequest.getLastName())
//				.phone(registerRequest.getPhone()).address(registerRequest.getAddress())
//				.email(registerRequest.getEmail()).password(passwordEncoder.encode(registerRequest.getPassword()))
//				.scheduledVehicleCount(0).isAvailable(Boolean.TRUE).role(Role.SERVICE_ADVISOR).build();
//
//		User savedUser = userRepository.save(user);
//
//		AdvisorAvailability availability = AdvisorAvailability.builder().serviceAdvisor(savedUser)
//				.workStartTime(registerRequest.getWorkStartTime()).workEndTime(registerRequest.getWorkEndTime())
//				.slotDuration(registerRequest.getSlotDuration())
//				.build();
//
//		advisorAvailabilityRepository.save(availability);
//		
//		generateMonthlySlots(savedUser, availability);
//
//		return ApiResponse.builder().message("User registered successfully !!").status("Success").build();
//	}
	  
	  
	  @Override
	  @Transactional
	  public ApiResponse createUser(RegisterRequest registerRequest) {

	      User user = User.builder()
	              .firstName(registerRequest.getFirstName())
	              .lastName(registerRequest.getLastName())
	              .phone(registerRequest.getPhone())
	              .address(registerRequest.getAddress())
	              .email(registerRequest.getEmail())
	              .password(passwordEncoder.encode(registerRequest.getPassword()))
	              .scheduledVehicleCount(0)
	              .isAvailable(Boolean.TRUE)
	              .role(Role.SERVICE_ADVISOR)
	              .build();

	      User savedUser = userRepository.save(user);

	      AdvisorAvailability availability =
	              AdvisorAvailability.builder()
	                      .serviceAdvisor(savedUser)
	                      .workStartTime(registerRequest.getWorkStartTime())
	                      .workEndTime(registerRequest.getWorkEndTime())
	                      .slotDuration(registerRequest.getSlotDuration())
	                      .build();

	      advisorAvailabilityRepository.save(availability);

	      advisorScheduleService.generateMonthlySlots(
	              savedUser,
	              availability,
	              YearMonth.now());

	      return ApiResponse.builder()
	              .message("User registered successfully !!")
	              .status("Success")
	              .build();
	  }

	@Override
	public UserDto getUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public List<UserDto> getAllUsers() {
		List<User> userList = userRepository.findAll();
		return userList.stream().map(user -> modelMapper.map(user, UserDto.class)).toList();
	}

//    @Override
//    public List<UserDto> getAllServiceAdvisors() {
//        List<User> userList = userRepository.findAllByRole(Role.SERVICE_ADVISOR);
//        return userList.stream().sorted(Comparator.comparingInt(User::getScheduledVehicleCount)).map(user -> modelMapper.map(user, UserDto.class)).toList();
//    }

	@Override
	public List<ServiceAdvisorDto> getAllServiceAdvisors() {

		List<User> userList = userRepository.findAllByRole(Role.SERVICE_ADVISOR);

		return userList.stream().map((User user) -> {

			UserDto userDto = modelMapper.map(user, UserDto.class);

			AdvisorAvailabilityDto advisorAvailabilityDto = null;
			
		
			if (user.getAdvisorAvailability() != null) {
				advisorAvailabilityDto = modelMapper.map(user.getAdvisorAvailability(), AdvisorAvailabilityDto.class);
			}
			
			List<AdvisorScheduleDto> scheduleDtos = null;

//            if (user.getSchedules() != null) {
//                scheduleDtos = user.getSchedules()
//                        .stream()
//                        .map(schedule ->
//                                modelMapper.map(
//                                        schedule,
//                                        AdvisorScheduleDto.class))
//                        .collect(Collectors.toList());
//            }
			
			
			if (user.getSchedules() != null) {

			    LocalDate today = LocalDate.now();
			    LocalTime now = LocalTime.now();

			    scheduleDtos = user.getSchedules()
			            .stream()

			            // only AVAILABLE schedules
			            .filter(schedule ->
			                    schedule.getStatus() == null ||
			                    schedule.getStatus().name().equals("AVAILABLE")
			            )

			            // remove past dates and past times
			            .filter(schedule -> {

			                LocalDate scheduleDate = schedule.getScheduleDate();

			                // future dates allowed
			                if (scheduleDate.isAfter(today)) {
			                    return true;
			                }

			                // today → check time
			                if (scheduleDate.isEqual(today)) {

			                    LocalTime startTime = schedule.getStartTime();

			                    return startTime != null &&
			                           startTime.isAfter(now);
			                }

			                // past dates not allowed
			                return false;
			            })

			            .map(schedule ->
			                    modelMapper.map(
			                            schedule,
			                            AdvisorScheduleDto.class))

			            .collect(Collectors.toList());
			}

            return ServiceAdvisorDto.builder()
                    .advisor(userDto)
                    .availability(advisorAvailabilityDto)
                    .schedules(scheduleDtos)
                    .build();

		
		}).collect(Collectors.toList());
	}

	@Override
	public ApiResponse updateUserById(Long id, UserDto userDTO) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setAddress(userDTO.getAddress());
		user.setPhone(userDTO.getPhone());
		userRepository.save(user);
		return ApiResponse.builder().message("User Updated successfully.").status("Success").build();
	}

	@Override
	public ApiResponse deleteUserById(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User", "id", id.toString()));
		userRepository.delete(user);
		return ApiResponse.builder().message("User deleted successfully.").status("Success").build();
	}

	private void generateMonthlySlots(User advisor, AdvisorAvailability availability) {

		LocalDate currentDate = LocalDate.now();

		LocalDate endOfMonth = currentDate.withDayOfMonth(currentDate.lengthOfMonth());

		while (!currentDate.isAfter(endOfMonth)) {

// Skip Sundays
			if (currentDate.getDayOfWeek() != DayOfWeek.SUNDAY) {

				generateDailySlots(advisor, availability, currentDate);
			}

			currentDate = currentDate.plusDays(1);
		}
	}

	private void generateDailySlots(
	        User advisor,
	        AdvisorAvailability availability,
	        LocalDate date) {

	    LocalTime currentTime =
	            availability.getWorkStartTime();

	    while (currentTime.plusMinutes(
	            availability.getSlotDuration())
	            .compareTo(
	                    availability.getWorkEndTime()) <= 0) {

	        if (!advisorScheduleRepository
	                .existsByServiceAdvisorAndScheduleDateAndStartTime(
	                        advisor,
	                        date,
	                        currentTime)) {

	            LocalTime endTime =
	                    currentTime.plusMinutes(
	                            availability.getSlotDuration());

	            AdvisorSchedule schedule =
	                    AdvisorSchedule.builder()
	                            .serviceAdvisor(advisor)
	                            .scheduleDate(date)
	                            .startTime(currentTime)
	                            .endTime(endTime)
	                            .status(ScheduleStatus.AVAILABLE)
	                            .build();

	            advisorScheduleRepository.save(schedule);
	        }

	        currentTime =
	                currentTime.plusMinutes(
	                        availability.getSlotDuration());
	    }
	}
	
	public List<AdvisorScheduleDto> getAvailableSlots(
	        Long advisorId,
	        LocalDate date) {

	    User advisor = userRepository.findById(advisorId)
	            .orElseThrow(() ->
	                    new ResourceNotFoundException("User", "id", advisorId.toString()));

	    return advisorScheduleRepository
	            .findByServiceAdvisorAndScheduleDateAndStatus(
	                    advisor,
	                    date,
	                    ScheduleStatus.AVAILABLE)
	            .stream()
	            .map(schedule ->
	                    modelMapper.map(
	                            schedule,
	                            AdvisorScheduleDto.class))
	            .collect(Collectors.toList());
	}

}
