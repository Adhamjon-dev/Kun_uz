package dasturlash.uz.util;

import dasturlash.uz.config.CustomUserDetails;
import dasturlash.uz.enums.ProfileRoleEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SpringSecurityUtil {

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        // System.out.println(user.getUsername());
        return user;
    }

    public static Integer getCurrentUserId() {
        CustomUserDetails user = getCurrentUser();
        return user.getId();
    }

    public static boolean checkRoleExist(ProfileRoleEnum role) {
        CustomUserDetails user = getCurrentUser();

//       return user.getAuthorities().stream().filter(item -> item.getAuthority().equals(role.name())).findAny().isPresent();
        return user.getAuthorities().stream().anyMatch(item -> item.getAuthority().equals(role.name()));
    }
}