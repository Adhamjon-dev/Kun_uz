package dasturlash.uz.service;

import dasturlash.uz.dto.RegionDTO;
import dasturlash.uz.entitiy.RegionEntity;
import dasturlash.uz.enums.Language;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static dasturlash.uz.enums.Language.*;

@Service
public class RegionService {
    @Autowired
    private RegionRepository regionRepository;

    public RegionDTO create(RegionDTO dto) {
        // checking ...
        boolean exists = regionRepository.existsByRegionKey(dto.getRegionKey().toLowerCase());
        if (exists) {
            throw new AppBadException("Region key exists: " + dto.getRegionKey());
        }

        RegionEntity entity = new RegionEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setRegionKey(dto.getRegionKey().toLowerCase());
//        entity.setVisible(Boolean.TRUE);
//        entity.setCreatedDate(LocalDateTime.now());
        regionRepository.save(entity);

        // response
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean update(RegionDTO dto, Integer id) {
        Optional<RegionEntity> optional = regionRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Region not found: " + id);
        }
        boolean exists = regionRepository.existsByRegionKeyAndIdNot(dto.getRegionKey().toLowerCase(), id);
        if (exists) {
            throw new AppBadException("Region key exists: " + dto.getRegionKey());
        }

        RegionEntity entity =  optional.get();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setRegionKey(dto.getRegionKey().toLowerCase());
        regionRepository.save(entity);
        return true;
    }

    public void delete(Integer id) {
        Optional<RegionEntity> optional = regionRepository.findById(id);
        if (optional.isPresent()) {
            RegionEntity entity = optional.get();
            entity.setVisible(false);
            regionRepository.save(entity);
        }
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> entityList = regionRepository.findAll();
        return changeToDtoList(entityList);
    }

    private List<RegionDTO> changeToDtoList(Iterable<RegionEntity> entityList) {
        List<RegionDTO> dtoList = new LinkedList<>();
        for (RegionEntity entity : entityList) {
            RegionDTO dto = new RegionDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            dto.setNameUz(entity.getNameUz());
            dto.setNameRu(entity.getNameRu());
            dto.setNameEn(entity.getNameEn());
            dto.setRegionKey(entity.getRegionKey().toLowerCase());
            dto.setVisible(entity.getVisible());
            dto.setCreatedDate(entity.getCreatedDate());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<LanguageMapper> getByLanguage(String language) {
        try {
            if (Language.valueOf(language).equals(EN)) {
                return regionRepository.getEnLanguage();
            }
            if (Language.valueOf(language).equals(RU)) {
                return regionRepository.getRuLanguage();
            }
            return regionRepository.getUzLanguage();
        } catch (IllegalArgumentException e) {
            throw new AppBadException("Language not found: " + language);
        }
    }
}
