package tn.esprit.pi.usermanagement.suermanagementmicroservice.mappers;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserRequestDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Dtos.UserResponseDTO;
import tn.esprit.pi.usermanagement.suermanagementmicroservice.Entities.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-02T12:56:35+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User userRequestDTOToUser(UserRequestDTO userRequestDTO) {
        if ( userRequestDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userRequestDTO.getId() );
        user.setName( userRequestDTO.getName() );
        user.setEmail( userRequestDTO.getEmail() );
        user.setPassword( userRequestDTO.getPassword() );
        user.setRole( userRequestDTO.getRole() );
        user.setPhone( userRequestDTO.getPhone() );
        user.setEspritId( userRequestDTO.getEspritId() );
        user.setClassName( userRequestDTO.getClassName() );
        if ( userRequestDTO.getActive() != null ) {
            user.setActive( Boolean.parseBoolean( userRequestDTO.getActive() ) );
        }
        user.setOldNotePi( userRequestDTO.getOldNotePi() );

        return user;
    }

    @Override
    public User userResposneDTOToUser(UserResponseDTO userResponseDTO) {
        if ( userResponseDTO == null ) {
            return null;
        }

        User user = new User();

        user.setId( userResponseDTO.getId() );
        user.setName( userResponseDTO.getName() );
        user.setEmail( userResponseDTO.getEmail() );
        user.setRole( userResponseDTO.getRole() );
        user.setPhone( userResponseDTO.getPhone() );
        user.setIs2FAEnabled( userResponseDTO.getIs2FAEnabled() );
        user.setToken( userResponseDTO.getToken() );
        user.setEspritId( userResponseDTO.getEspritId() );
        user.setClassName( userResponseDTO.getClassName() );
        user.setActive( userResponseDTO.isActive() );
        user.setOldNotePi( userResponseDTO.getOldNotePi() );

        return user;
    }

    @Override
    public UserResponseDTO userToUserResponseDTO(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponseDTO userResponseDTO = new UserResponseDTO();

        userResponseDTO.setId( user.getId() );
        userResponseDTO.setName( user.getName() );
        userResponseDTO.setEmail( user.getEmail() );
        userResponseDTO.setRole( user.getRole() );
        userResponseDTO.setPhone( user.getPhone() );
        userResponseDTO.setClassName( user.getClassName() );
        userResponseDTO.setIs2FAEnabled( user.getIs2FAEnabled() );
        userResponseDTO.setToken( user.getToken() );
        userResponseDTO.setEspritId( user.getEspritId() );
        userResponseDTO.setActive( user.isActive() );
        userResponseDTO.setOldNotePi( user.getOldNotePi() );

        return userResponseDTO;
    }

    @Override
    public List<UserResponseDTO> usersListToUserResponseDTO(List<User> user) {
        if ( user == null ) {
            return null;
        }

        List<UserResponseDTO> list = new ArrayList<UserResponseDTO>( user.size() );
        for ( User user1 : user ) {
            list.add( userToUserResponseDTO( user1 ) );
        }

        return list;
    }
}
