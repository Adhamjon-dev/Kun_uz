package dasturlash.uz.service;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.entitiy.CategoryEntity;
import dasturlash.uz.enums.Language;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static dasturlash.uz.enums.Language.EN;
import static dasturlash.uz.enums.Language.RU;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public CategoryDTO create(CategoryDTO dto) {
        // checking ...
        boolean exists = categoryRepository.existsByCategoryKey(dto.getCategoryKey().toLowerCase());
        if (exists) {
            throw new AppBadException("Category key exists: " + dto.getCategoryKey());
        }

        CategoryEntity entity = new CategoryEntity();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setCategoryKey(dto.getCategoryKey().toLowerCase());

        categoryRepository.save(entity);

        // response
        dto.setId(entity.getId());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public Boolean update(CategoryDTO dto, Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findByIdAndVisibleTrue(id);
        if (optional.isEmpty()) {
            throw new AppBadException("Category not found: " + id);
        }
        boolean exists = categoryRepository.existsByCategoryKeyAndIdNot(dto.getCategoryKey().toLowerCase(), id);
        if (exists) {
            throw new AppBadException("Category key exists: " + dto.getCategoryKey());
        }

        CategoryEntity entity =  optional.get();
        entity.setOrderNumber(dto.getOrderNumber());
        entity.setNameUz(dto.getNameUz());
        entity.setNameRu(dto.getNameRu());
        entity.setNameEn(dto.getNameEn());
        entity.setCategoryKey(dto.getCategoryKey().toLowerCase());
        categoryRepository.save(entity);
        return true;
    }

    public void delete(Integer id) {
        Optional<CategoryEntity> optional = categoryRepository.findById(id);
        if (optional.isPresent()) {
            CategoryEntity entity = optional.get();
            entity.setVisible(false);
            categoryRepository.save(entity);
        }
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> entityList = categoryRepository.findAll();
        return changeToDtoList(entityList);
    }

    private List<CategoryDTO> changeToDtoList(Iterable<CategoryEntity> entityList) {
        List<CategoryDTO> dtoList = new LinkedList<>();
        for (CategoryEntity entity : entityList) {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(entity.getId());
            dto.setOrderNumber(entity.getOrderNumber());
            dto.setNameUz(entity.getNameUz());
            dto.setNameRu(entity.getNameRu());
            dto.setNameEn(entity.getNameEn());
            dto.setCategoryKey(entity.getCategoryKey().toLowerCase());
            dto.setVisible(entity.getVisible());
            dto.setCreatedDate(entity.getCreatedDate());
            dtoList.add(dto);
        }
        return dtoList;
    }

    public List<LanguageMapper> getByLanguage(String language) {
        try {
            if (Language.valueOf(language).equals(EN)) {
                return categoryRepository.getEnLanguage();
            }
            if (Language.valueOf(language).equals(RU)) {
                return categoryRepository.getRuLanguage();
            }
            return categoryRepository.getUzLanguage();
        } catch (IllegalArgumentException e) {
            throw new AppBadException("Language not found: " + language);
        }
    }
}
