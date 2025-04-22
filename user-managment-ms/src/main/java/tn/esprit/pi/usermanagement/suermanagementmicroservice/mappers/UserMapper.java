package tn.esprit.pi.usermanagement.suermanagementmicroservice.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserRequestDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserResponseDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;

@Mapper(componentModel = "spring") // "spring" = auto-register as bean
public interface UserMapper {

    User userRequestDTOToUser(UserRequestDTO userRequestDTO);
    User userResposneDTOToUser(UserResponseDTO userResponseDTO);
    UserResponseDTO userToUserResponseDTO(User user);

}
