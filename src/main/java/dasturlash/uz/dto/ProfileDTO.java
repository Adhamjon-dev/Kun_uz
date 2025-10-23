package dasturlash.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.enums.ProfileStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProfileDTO {
    private Integer id;

    //    @NotBlank(message = "Ism bo‘sh bo‘lmasligi kerak")
    private String name;

    //    @NotBlank(message = "Familiya bo‘sh bo‘lmasligi kerak")
    private String surname;

    //    @NotBlank(message = "Username  bo‘sh bo‘lmasligi kerak")
    private String username;

    //    @NotBlank(message = "Parol bo‘sh bo‘lmasligi kerak")
    private String password;

    //    @NotEmpty(message = "Role bo‘sh bo‘lmasligi kerak")
    private List<ProfileRoleEnum> roleList;

    private LocalDateTime createdDate;
    private ProfileStatus status;
}
