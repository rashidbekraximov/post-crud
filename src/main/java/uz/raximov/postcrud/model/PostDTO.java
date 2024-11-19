package uz.raximov.postcrud.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import uz.raximov.postcrud.domain.User;


@Getter
@Setter
public class PostDTO {

    private Long id;

    @NotNull
    @Size(max = 50000)
    private String title;

    @NotNull
    @Size(max = 50000)
    private String content;

    @NotNull
    private String updatedDate;

    @NotNull
    private String createDate;

    @NotNull
    private User user;

}
