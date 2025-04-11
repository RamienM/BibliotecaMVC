package org.biblioteca.bibliotecamvc.business.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserRegisterDTO {
    private String username;
    private String password;
    private Boolean admin;
}
