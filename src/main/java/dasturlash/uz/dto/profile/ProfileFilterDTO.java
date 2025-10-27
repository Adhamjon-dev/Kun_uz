package dasturlash.uz.dto.profile;

import dasturlash.uz.enums.ProfileRoleEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ProfileFilterDTO {
    private String name;
    private String surname;
    private ProfileRoleEnum role;
    private String username;
    private LocalDate createdDateFrom;
    private LocalDate createdDateTo;
}
