package tn.esprit.pi.usermanagement.suermanagementmicroservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserRequestDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserResponseDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;

import java.util.List;

@Mapper(componentModel = "spring") // "spring" = auto-register as bean
public interface UserMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "espritId", target = "espritId")
    @Mapping(source = "className", target = "className")
    @Mapping(source = "active", target = "active")
    User userRequestDTOToUser(UserRequestDTO userRequestDTO);
    User userResposneDTOToUser(UserResponseDTO userResponseDTO);
    UserResponseDTO userToUserResponseDTO(User user);
    List<UserResponseDTO> usersListToUserResponseDTO(List<User> user);

}
