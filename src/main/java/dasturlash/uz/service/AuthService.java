package dasturlash.uz.service;

import dasturlash.uz.dto.auth.AuthorizationDTO;
import dasturlash.uz.dto.auth.VerificationBySmsDTO;
import dasturlash.uz.dto.auth.RegistrationDTO;
import dasturlash.uz.dto.profile.ProfileDTO;
import dasturlash.uz.entitiy.ProfileEntity;
import dasturlash.uz.enums.ProfileRoleEnum;
import dasturlash.uz.enums.ProfileStatus;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.ProfileRepository;
import dasturlash.uz.util.ValidatorUtil;
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
    @Autowired
    private EmailSendingService  emailSendingService;

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
        if (ValidatorUtil.isPhoneNumber(dto.getUsername())) {
            // send verification code
            new Thread(() -> {
                smsSenderService.sendRegistrationSMS(profile.getUsername());
            }).start();
        }else { // email
            new Thread(() -> emailSendingService.sendRegistrationEmailLink(profile.getUsername(), dto.getName())).start();
        }

        return "Sms code jo'natildi mazgi.";
    }

    public String verificationBySms(VerificationBySmsDTO dto) {
        if (smsHistoryService.isSmsSendToPhone(dto.getUserName(), dto.getCode())) {
            profileService.setStatusByUsername(ProfileStatus.ACTIVE, dto.getUserName());
            return "Verification Success!";
        }
        throw new AppBadException("Wrong sms code");
    }

    public ProfileDTO login(AuthorizationDTO dto) {
        Optional<ProfileEntity> profileOptional = profileRepository.findByUsernameAndVisibleTrue(dto.getUsername());
        if (profileOptional.isEmpty()) {
            throw new AppBadException("Username or password wrong");
        }
        ProfileEntity entity = profileOptional.get();
        if (!bCryptPasswordEncoder.matches(dto.getPassword(), entity.getPassword())) {
            throw new AppBadException("Username or password wrong");
        }
        if (!entity.getStatus().equals(ProfileStatus.ACTIVE)) {
            throw new AppBadException("User in wrong status");
        }
        ProfileDTO response = new ProfileDTO();
        response.setId(entity.getId());
        response.setName(entity.getName());
        response.setSurname(entity.getSurname());
        response.setUsername(entity.getUsername());
        response.setRoleList(profileRoleService.getByProfileId(entity.getId()));
        return response;
    }

    public String verificationByLink(String id) {
        Optional<ProfileEntity> optional = profileService.getProfileById(Integer.parseInt(id));
        if (optional.isPresent()) {
            profileService.setStatusByUsername(ProfileStatus.ACTIVE, optional.get().getUsername());
            return "Verification Success!";
        }
        throw new AppBadException("Wrong sms code");
    }
}
