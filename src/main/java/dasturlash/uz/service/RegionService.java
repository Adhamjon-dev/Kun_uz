package dasturlash.uz.service;

import dasturlash.uz.dto.RegionDTO;
import dasturlash.uz.entitiy.RegionEntity;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public Boolean delete(Integer id) {
        return regionRepository.updateVisibleById(id) == 1;
    }

    public List<RegionDTO> getAll() {
        Iterable<RegionEntity> iterable = regionRepository.findAllOrder();
        List<RegionDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity -> dtoList.add(toDto(entity)));
        return dtoList;
    }

    private RegionDTO toDto(RegionEntity entity) {
        RegionDTO dto = new RegionDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setNameEn(entity.getNameEn());
        dto.setRegionKey(entity.getRegionKey().toLowerCase());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<LanguageMapper> getAllByLang(AppLanguageEnum lang) {
        return regionRepository.getByLang(lang.name());
    }

    public RegionEntity get(Integer id) {
        return regionRepository.findById(id).orElseThrow(() -> {
            throw new AppBadException("Item not found");
        });
    }

    public RegionDTO getByIdAndLang(Integer id, AppLanguageEnum lang) {
        LanguageMapper mapper = regionRepository.getByIdAndLang(id, lang.name());
        RegionDTO dto = new RegionDTO();
        dto.setId(mapper.getId());
        dto.setName(mapper.getName());
        dto.setOrderNumber(mapper.getOrderNumber());
        dto.setRegionKey(mapper.getKey());
        return dto;
    }

}
