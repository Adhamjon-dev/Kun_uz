package dasturlash.uz.service;

import dasturlash.uz.dto.VerificationBySmsDTO;
import dasturlash.uz.dto.profile.RegistrationDTO;
import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.enums.ProfileStatus;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ProfileRoleService profileRoleService;
    @Autowired
    private SmsSenderService smsSenderService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private SmsHistoryService smsHistoryService;

    public String registration(RegistrationDTO dto) { //
        // name = Toshmat, username = mazgi
        // name = Eshmat, username = mazgi
        Optional<ProfileEntity> optional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (optional.isPresent()) { //
            ProfileEntity existsProfile = optional.get();
            if (existsProfile.getStatus().equals(ProfileStatus.NOT_ACTIVE)) {
                profileRoleService.deleteRolesByProfileId(existsProfile.getId());
                profileRepository.deleteById(existsProfile.getId()); // delete
            } else {
                throw new AppBadException("Username already exists");
            }
        }
        // create profile
        ProfileEntity profile = new ProfileEntity();
        profile.setName(dto.getName());
        profile.setSurname(dto.getSurname());
        profile.setUsername(dto.getUsername());
        profile.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        profile.setVisible(true);
        profile.setStatus(ProfileStatus.NOT_ACTIVE);
        profileRepository.save(profile);
        // save roles
        profileRoleService.create(profile.getId(), List.of(ProfileRoleEnum.ROLE_USER));
        // send verification code
        smsSenderService.sendRegistrationSMS(profile.getUsername());

        return "Sms code jo'natildi mazgi.";
    }

    public String verificationBySms(VerificationBySmsDTO dto) {
        if (smsHistoryService.isSmsSendToPhone(dto.getPhoneNumber(), dto.getCode())) {
            profileService.setStatusByUsername(ProfileStatus.ACTIVE, dto.getPhoneNumber());
            return "Verification Success!";
        }
        throw new AppBadException("Wrong sms code");
    }

}
