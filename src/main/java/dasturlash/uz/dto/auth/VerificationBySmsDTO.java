package dasturlash.uz.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationBySmsDTO {
    @NotBlank(message = "userName required")
    private String userName;
    @NotBlank(message = "Code required")
    private String code;
}
