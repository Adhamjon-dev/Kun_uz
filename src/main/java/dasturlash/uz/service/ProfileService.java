package dasturlash.uz.service;

import dasturlash.uz.dto.CustomFilterResultDTO;
import dasturlash.uz.dto.auth.JwtDTO;
import dasturlash.uz.dto.profile.*;
import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.enums.ProfileStatus;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.CustomProfileRepository;
import dasturlash.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private CustomProfileRepository  customProfileRepository;

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

    public Boolean updateAdmin(Integer profileId, UpdateProfileAdminDTO newProfile) {
        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(profileId);
        if (optional.isEmpty()) {
            throw new AppBadException("User not found");
        }
        ProfileEntity entity = optional.get();
        entity.setStatus(newProfile.getStatus());
        profileRoleService.update(profileId, newProfile.getRoleList());
        profileRepository.save(entity);
        return Boolean.TRUE;
    }

    public Boolean updateOwn(Integer profileId, UpdateProfileOwnDTO newProfile, UserDetails userDetails) {
        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(profileId);
        if (optional.isEmpty()) {
            throw new AppBadException("User not found");
        }
        if (!optional.get().getUsername().equals(userDetails.getUsername())) {
            throw new AppBadException("Username not match");
        }
        Optional<ProfileEntity> opt = profileRepository.findByUsernameAndVisibleTrueAndIdNot(newProfile.getUsername(), profileId);
        if (opt.isPresent()) {
            throw new AppBadException("Username exists");
        }
        ProfileEntity entity = optional.get();
        entity.setName(newProfile.getName());
        entity.setSurname(newProfile.getSurname());
        entity.setUsername(newProfile.getUsername());
        profileRepository.save(entity);
        return Boolean.TRUE;
    }

    public Boolean updatePassword(Integer profileId, ProfileUpdatePasswordDTO dto, UserDetails userDetails) {
        Optional<ProfileEntity> optional = profileRepository.findByIdAndVisibleTrue(profileId);
        if (optional.isEmpty()) {
            throw new AppBadException("User not found");
        }
        ProfileEntity entity = optional.get();
        if (!entity.getUsername().equals(userDetails.getUsername())) {
            throw new AppBadException("Username not match");
        }
        if (bCryptPasswordEncoder.matches(entity.getPassword(), dto.getCurrentPassword())) {
            entity.setPassword(bCryptPasswordEncoder.encode(dto.getNewPassword()));
            profileRepository.save(entity);
            return Boolean.TRUE;
        }
        throw new AppBadException("Current password does not match");
    }

    public PageImpl<ProfileDTO> pagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size,  Sort.by("createdDate").descending());
        Page<ProfileEntity> pageResult = profileRepository.findAll(pageable);

        List<ProfileEntity> entityList = pageResult.getContent();
        long totalCount = pageResult.getTotalElements();

        List<ProfileDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> dtoList.add(toDto(entity)));
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private ProfileDTO toDto(ProfileEntity entity) {
        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPassword(entity.getPassword());
        dto.setUsername(entity.getUsername());
        dto.setStatus(entity.getStatus());
        return dto;
    }

    public Boolean delete(Integer id) {
        return profileRepository.updateVisibleById(id) == 1;
    }

    public PageImpl<ProfileDTO> filter(ProfileFilterDTO filter, int page, int size) {
        CustomFilterResultDTO<ProfileEntity> result = customProfileRepository.filter(filter, page, size);
        List<ProfileEntity> entityList = result.getContent();
        long totalCount = result.getTotalCount();

        List<ProfileDTO> dtoList = new LinkedList<>();
        entityList.forEach(entity -> dtoList.add(toDto(entity)));
        return new PageImpl<>(dtoList, PageRequest.of(page, size), totalCount);
    }

    public ProfileDTO get(Integer id) {
        Optional<ProfileEntity> optional =  profileRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Item not found");
        }
        ProfileEntity entity = optional.get();
        return toDto(entity);
    }

    public Integer getIdByUsername(String username) {
        Optional<ProfileEntity> optional =  profileRepository.findByUsernameAndVisibleTrue(username);
        if (optional.isEmpty()) {
            throw new AppBadException("Username not found");
        }
        return optional.get().getId();
    }

    public void setStatusByUsername(ProfileStatus status, String username) {
        profileRepository.setStatusByUsername(status, username);
    }

    public Optional<ProfileEntity> getProfileById(Integer id) {
        return profileRepository.findByIdAndStatusAndVisibleTrue(id, ProfileStatus.NOT_ACTIVE);
    }
}
