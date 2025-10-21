package dasturlash.uz.service;

import dasturlash.uz.dto.RegionDTO;
import dasturlash.uz.entitiy.RegionEntity;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
