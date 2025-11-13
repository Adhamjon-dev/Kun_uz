package dasturlash.uz.service;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.entitiy.CategoryEntity;
import dasturlash.uz.enums.AppLanguageEnum;
import dasturlash.uz.exp.AppBadException;
import dasturlash.uz.mapper.LanguageMapper;
import dasturlash.uz.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

    public Boolean delete(Integer id) {
        return categoryRepository.updateVisibleById(id) == 1;
    }

    public List<CategoryDTO> getAll() {
        Iterable<CategoryEntity> iterable = categoryRepository.findAllOrder();
        List<CategoryDTO> dtoList = new LinkedList<>();
        iterable.forEach(entity -> dtoList.add(toDto(entity)));
        return dtoList;
    }

    private CategoryDTO toDto(CategoryEntity entity) {
        CategoryDTO dto = new CategoryDTO();
        dto.setId(entity.getId());
        dto.setOrderNumber(entity.getOrderNumber());
        dto.setNameUz(entity.getNameUz());
        dto.setNameRu(entity.getNameRu());
        dto.setNameEn(entity.getNameEn());
        dto.setCategoryKey(entity.getCategoryKey().toLowerCase());
        dto.setVisible(entity.getVisible());
        dto.setCreatedDate(entity.getCreatedDate());
        return dto;
    }

    public List<LanguageMapper> getAllByLang(AppLanguageEnum lang) {
        return categoryRepository.getByLang(lang.name());
    }

    public CategoryEntity get(Integer id) {
        return categoryRepository.findById(id).orElseThrow(() -> {
            throw new AppBadException("Item not found");
        });
    }

    public List<CategoryDTO> getCategoryListByArticleId(String articleId, AppLanguageEnum lang) {
        List<LanguageMapper> mapperList = categoryRepository.getListByArticleIdAndLang(articleId, lang.name());
        List<CategoryDTO> dtoList = new LinkedList<>();
        mapperList.forEach(mapper -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(mapper.getId());
            dto.setOrderNumber(mapper.getOrderNumber());
            dto.setCategoryKey(mapper.getKey());
            dto.setName(mapper.getName());

            dtoList.add(dto);
        });
        return dtoList;
    }
}
