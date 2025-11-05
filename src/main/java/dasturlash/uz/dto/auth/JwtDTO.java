package dasturlash.uz.dto.auth;

import dasturlash.uz.enums.ProfileRoleEnum;

import java.util.List;

public class JwtDTO {
    private String username;
    private List<ProfileRoleEnum> roles;

    public JwtDTO(String username, List<ProfileRoleEnum> roles) {
        this.username = username;
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public List<ProfileRoleEnum> getRoles() {
        return roles;
    }

}
