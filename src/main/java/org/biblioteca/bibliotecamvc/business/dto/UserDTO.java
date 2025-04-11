package org.biblioteca.bibliotecamvc.business.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Integer id;
    private String username;
    private Boolean admin;
}
