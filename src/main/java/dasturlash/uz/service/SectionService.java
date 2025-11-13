package dasturlash.uz.service;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.dto.SectionDTO;
import dasturlash.uz.entitiy.SectionEntity;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public Boolean delete(Integer id) {
        return sectionRepository.updateVisibleById(id) == 1;
    }

    public List<SectionDTO> getAll() {
        Iterable<SectionEntity> iterable = sectionRepository.findAllOrder();
        List<SectionDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity -> dtoList.add(toDto(entity)));
        return dtoList;
    }

    public PageImpl<SectionDTO> getAllPagination(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<SectionEntity> pageResult = sectionRepository.findAll(pageable);

        List<SectionEntity> list = pageResult.getContent();
        long totalCount = pageResult.getTotalElements();

        List<SectionDTO> dtoList = new LinkedList<>();
        list.forEach(entity -> dtoList.add(toDto(entity)));
        return new PageImpl<>(dtoList, pageable, totalCount);
    }

    private SectionDTO toDto(SectionEntity entity) {
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
        return dto;
    }

    public List<LanguageMapper> getAllByLang(AppLanguageEnum lang) {
        return sectionRepository.getByLang(lang.name());
    }

    public SectionEntity get(Integer id) {
        return sectionRepository.findByIdAndVisibleTrue(id).orElseThrow(() -> {
            throw new AppBadException("Item not found");
        });
    }

    public List<SectionDTO> getSectionListByArticleId(String articleId, AppLanguageEnum lang) {
        List<LanguageMapper> mapperList = sectionRepository.getListByArticleIdAndLang(articleId, lang.name());
        List<SectionDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> {
            SectionDTO dto = new SectionDTO();
            dto.setId(mapper.getId());
            dto.setOrderNumber(mapper.getOrderNumber());
            dto.setSectionKey(mapper.getKey());
            dto.setName(mapper.getName());

            dtoList.add(dto);
        });
        return dtoList;
    }
}
