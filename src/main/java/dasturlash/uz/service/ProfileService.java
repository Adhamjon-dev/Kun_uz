package dasturlash.uz.service;

import dasturlash.uz.dto.ProfileDTO;
import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.enums.ProfileStatus;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;

    public ProfileDTO create(ProfileDTO profile) {
        // checking
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(profile.getUsername());
        if (optional.isPresent()) {
            throw new AppBadException("User exists");
        }
        ProfileEntity entity = new ProfileEntity();
        entity.setName(profile.getName());
        entity.setSurname(profile.getSurname());
        entity.setPassword(bCryptPasswordEncoder.encode(profile.getPassword()));
        entity.setUsername(profile.getUsername());
        entity.setStatus(ProfileStatus.ACTIVE);
        entity.setVisible(Boolean.TRUE);
        profileRepository.save(entity); // save
        // save roles
        profileRoleService.create(entity.getId(), profile.getRoleList());

        profile.setId(entity.getId());
        profile.setCreatedDate(entity.getCreatedDate());
        return profile;
    }
}
