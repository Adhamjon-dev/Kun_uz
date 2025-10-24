package dasturlash.uz.dto.update_dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateProfileOwn {
    @NotBlank(message = "Ism bo‘sh bo‘lmasligi kerak")
    private String name;

    @NotBlank(message = "Familiya bo‘sh bo‘lmasligi kerak")
    private String surname;

    @NotBlank(message = "Username  bo‘sh bo‘lmasligi kerak")
    private String username;

    @NotBlank(message = "Parol bo‘sh bo‘lmasligi kerak")
    private String password;
}
