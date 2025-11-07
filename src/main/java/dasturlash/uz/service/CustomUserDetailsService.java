package dasturlash.uz.service;

import dasturlash.uz.config.CustomUserDetails;
import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    ProfileRoleRepository profileRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username = login or phone or email
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        ProfileEntity profile = optional.get();
        List<ProfileRoleEnum> roleList = profileRoleRepository.getRoleListByProfileId(profile.getId());
        return new CustomUserDetails(profile.getId(),
                profile.getName(),
                profile.getSurname(),
                profile.getUsername(),
                profile.getPassword(),
                roleList,
                profile.getStatus());
    }
}