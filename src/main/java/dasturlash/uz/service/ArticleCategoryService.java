package dasturlash.uz.service;

import dasturlash.uz.entitiy.article.ArticleCategoryEntity;
import dasturlash.uz.repository.ArticleCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleCategoryService {
    @Autowired
    private ArticleCategoryRepository articleCategoryRepository;

    public void create(String articleId, List<Integer> categoryIds) {
        categoryIds.forEach(categoryId -> {
            create(articleId, categoryId);
        });
    }

    private void create(String articleId, Integer categoryId) {
        ArticleCategoryEntity articleCategoryEntity = new ArticleCategoryEntity();
        articleCategoryEntity.setArticleId(articleId);
        articleCategoryEntity.setCategoryId(categoryId);
        articleCategoryRepository.save(articleCategoryEntity);
    }

    public void update(String articleId, List<Integer> newList) {
        List<Integer> oldList = articleCategoryRepository.getCategoryIdsByArticleId(articleId);
        newList.stream().filter(n -> !oldList.contains(n)).forEach(catId -> create(articleId, catId));
        oldList.stream().filter(old -> !newList.contains(old)).forEach(catId -> articleCategoryRepository.deleteByArticleIdAndCategoryId(articleId, catId));
    }
}