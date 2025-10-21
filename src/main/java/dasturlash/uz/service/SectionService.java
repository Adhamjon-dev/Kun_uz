package dasturlash.uz.service;

import dasturlash.uz.dto.SectionDTO;
import dasturlash.uz.entitiy.SectionEntity;
import dasturlash.uz.enums.Language;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static dasturlash.uz.enums.Language.EN;
import static dasturlash.uz.enums.Language.RU;

@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    public SectionDTO create(SectionDTO dto) {
        // checking ...
        boolean exists = sectionRepository.existsBySectionKey(dto.getSectionKey().toLowerCase());
        if (exists) {
            throw new AppBadException("Section key exists: " + dto.getSectionKey());
        }

        SectionEntity entity = new SectionEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setImageId(dto.getImageId());
        entity.setSectionKey(dto.getSectionKey().toLowerCase());

        sectionRepository.save(entity);

        // response
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean update(SectionDTO dto, Integer id) {
        Optional<SectionEntity> optional = sectionRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Section not found: " + id);
        }
        boolean exists = sectionRepository.existsBySectionKeyAndIdNot(dto.getSectionKey().toLowerCase(), id);
        if (exists) {
            throw new AppBadException("Section key exists: " + dto.getSectionKey());
        }

        SectionEntity entity =  optional.get();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setSectionKey(dto.getSectionKey().toLowerCase());
        entity.setImageId(dto.getImageId());
        sectionRepository.save(entity);
        return true;
    }

    public void delete(Integer id) {
        Optional<SectionEntity> optional = sectionRepository.findById(id);
        if (optional.isPresent()) {
            SectionEntity entity = optional.get();
            entity.setVisible(false);
            sectionRepository.save(entity);
        }
    }

    public List<SectionDTO> getAll() {
        Iterable<SectionEntity> entityList = sectionRepository.findAll();
        return changeToDtoList(entityList);
    }

    public PageImpl<SectionDTO> getAllPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<SectionEntity> pageResult = sectionRepository.findAll(pageable);

        List<SectionEntity> list = pageResult.getContent();
        long totalCount = pageResult.getTotalElements();

        List<SectionDTO> dtoList = changeToDtoList(list);
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private List<SectionDTO> changeToDtoList(Iterable<SectionEntity> entityList) {
        List<SectionDTO> dtoList = new LinkedList<>();
        for (SectionEntity entity : entityList) {
            SectionDTO dto = new SectionDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            dto.setNameUz(entity.getNameUz());
            dto.setNameRu(entity.getNameRu());
            dto.setNameEn(entity.getNameEn());
            dto.setSectionKey(entity.getSectionKey().toLowerCase());
            dto.setVisible(entity.getVisible());
            dto.setCreatedDate(entity.getCreatedDate());
            dto.setImageId(entity.getImageId());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<LanguageMapper> getByLanguage(String language) {
        try {
            if (Language.valueOf(language).equals(EN)) {
                return sectionRepository.getEnLanguage();
            }
            if (Language.valueOf(language).equals(RU)) {
                return sectionRepository.getRuLanguage();
            }
            return sectionRepository.getUzLanguage();
        } catch (IllegalArgumentException e) {
            throw new AppBadException("Language not found: " + language);
        }
    }
}
