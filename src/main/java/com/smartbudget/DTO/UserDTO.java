package com.smartbudget.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserDTO {
    private Long id;
    private String username;
    private String firstName;
    private String surname;
    private String email;

}
