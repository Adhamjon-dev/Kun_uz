package dasturlash.uz.service;

import dasturlash.uz.entitiy.ProfileRoleEntity;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.repository.ProfileRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileRoleService {
    @Autowired
    private ProfileRoleRepository profileRoleRepository;

    public void create(Integer profileId, List<ProfileRoleEnum> roleList) {
        for (ProfileRoleEnum roleEnum : roleList) {
            ProfileRoleEntity entity = new ProfileRoleEntity();
            entity.setProfileId(profileId);
            entity.setRole(roleEnum);
            profileRoleRepository.save(entity);
        }
    }

    public void update(Integer profileId, List<ProfileRoleEnum> roleList) {
        List<ProfileRoleEntity> list = profileRoleRepository.findByProfileId(profileId);
        for (ProfileRoleEntity profileRoleEntity : list) {
            boolean b = false;
            for (ProfileRoleEnum role : roleList) {
                if (profileRoleEntity.getRole().equals(role)) {
                    roleList.remove(role);
                    b = true;
                    break;
                }
            }
            if (!b) {
                profileRoleRepository.delete(profileRoleEntity);
            }
        }
        create(profileId, roleList);
    }

    public void deleteRolesByProfileId(Integer profileId) {
        profileRoleRepository.deleteByProfileId(profileId);
    }
}
