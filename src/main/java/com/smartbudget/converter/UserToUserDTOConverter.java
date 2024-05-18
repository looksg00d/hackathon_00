package com.smartbudget.converter;

import com.smartbudget.DTO.UserDTO;
import com.smartbudget.model.User;
import org.springframework.stereotype.Service;


@Service
public class UserToUserDTOConverter implements Converter<User, UserDTO> {
    @Override
    public UserDTO convert(User source) {
        UserDTO UserDTO = new UserDTO();

        UserDTO.setId(source.getId());
        UserDTO.setUsername(source.getUsername());
        UserDTO.setEmail(source.getEmail());
        UserDTO.setFirstName(source.getFirstName());
        UserDTO.setSurname(source.getSurname());

        return UserDTO;
    }
}
