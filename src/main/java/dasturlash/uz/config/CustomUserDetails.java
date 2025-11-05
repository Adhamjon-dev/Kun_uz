package dasturlash.uz.config;

import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
public class CustomUserDetails implements UserDetails {
    private String name;
    private String surname;
    private String username;
    private String password;
    private List<ProfileRoleEnum> roleList;
    private ProfileStatus status;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> roleListResult = new LinkedList<>();
        roleList.forEach(role -> roleListResult.add(new SimpleGrantedAuthority(role.name())));
        return roleListResult;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return status.equals(ProfileStatus.ACTIVE);
    }
}