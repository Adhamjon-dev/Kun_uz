package dasturlash.uz.service;

import dasturlash.uz.dto.CategoryDTO;
import dasturlash.uz.entitiy.article.ArticleCategoryEntity;
import dasturlash.uz.repository.ArticleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryService {
    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    private void create(String articleId, Integer categoryId) {
        ArticleCategoryEntity articleCategoryEntity = new ArticleCategoryEntity();
        articleCategoryEntity.setArticleId(articleId);
        articleCategoryEntity.setCategoryId(categoryId);
        articleCategoryRepository.save(articleCategoryEntity);
    }

    public void merge(String articleId, List<CategoryDTO> dtoList) {
        List<Integer> newList = dtoList.stream().map(CategoryDTO::getId).toList();
        List<Integer> oldList = articleCategoryRepository.getCategoryIdListByArticleId(articleId);

        newList.stream().filter(n -> !oldList.contains(n)).forEach(catId -> create(articleId, catId));
        oldList.stream().filter(old -> !newList.contains(old)).forEach(catId -> articleCategoryRepository.deleteByArticleIdAndCategoryId(articleId, catId));
    }
}