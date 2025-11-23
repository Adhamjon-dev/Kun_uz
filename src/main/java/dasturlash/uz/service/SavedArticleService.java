package dasturlash.uz.service;

import dasturlash.uz.dto.SavedArticleDTO;
import dasturlash.uz.entitiy.SavedArticleEntity;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.SavedArticleMapper;
import dasturlash.uz.repository.SavedArticleRepository;
import dasturlash.uz.util.SpringSecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class SavedArticleService {
    @Autowired
    private SavedArticleRepository savedArticleRepository;
    @Autowired
    private AttachService attachService;

    public Boolean save(String articleId) {
        Optional<SavedArticleEntity> optional = getByArticleIdAndProfileId(articleId);
        if (optional.isPresent()) {
            throw new AppBadException("Article saved already exists");
        }
        SavedArticleEntity savedArticleEntity = new SavedArticleEntity();
        savedArticleEntity.setArticleId(articleId);
        savedArticleEntity.setProfileId(SpringSecurityUtil.getCurrentUserId());
        savedArticleRepository.save(savedArticleEntity);
        return true;
    }

    public Boolean delete(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        if (savedArticleRepository.deleteByArticleIdAndProfileId(articleId, profileId) == 0) {
            throw new AppBadException("Article save not found");
        }
        return true;
    }

    public List<SavedArticleDTO> getByProfileId() {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        List<SavedArticleMapper> mapperList = savedArticleRepository.getSavedArticlesByProfileId(profileId);

        List<SavedArticleDTO> dtoList = new LinkedList<>();

        mapperList.forEach(mapper -> dtoList.add(toDto(mapper)));

        return dtoList;
    }

    private Optional<SavedArticleEntity> getByArticleIdAndProfileId(String articleId) {
        Integer profileId = SpringSecurityUtil.getCurrentUserId();
        return savedArticleRepository.getByArticleIdAndProfileId(articleId, profileId);
    }

    private SavedArticleDTO toDto(SavedArticleMapper mapper) {
        SavedArticleDTO dto = new SavedArticleDTO();
        dto.setId(mapper.getId());
        dto.setArticleId(mapper.getArticleId());
        dto.setArticleTitle(mapper.getArticleTitle());
        dto.setArticleDescription(mapper.getArticleDescription());
        dto.setSavedDate(mapper.getSavedDate());
        if (mapper.getArticleImageId() != null) {
            dto.setArticleImage(attachService.openDTO(dto.getArticleId()));
        }
        return dto;
    }
}
