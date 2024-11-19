package uz.raximov.postcrud.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String username;

    @Size(max = 255)
    private String password;

    private String role;

    private String createDate;
}
