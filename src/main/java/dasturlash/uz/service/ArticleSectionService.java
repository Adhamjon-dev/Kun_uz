package dasturlash.uz.service;

import dasturlash.uz.entitiy.article.ArticleCategoryEntity;
import dasturlash.uz.entitiy.article.ArticleSectionEntity;
import dasturlash.uz.repository.ArticleSectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleSectionService {
    @Autowired
    private ArticleSectionRepository articleSectionRepository;

    public void create(String articleId, List<Integer> sectionIds) {
        sectionIds.forEach(sectionId -> {
            ArticleSectionEntity articleSectionEntity = new ArticleSectionEntity();
            articleSectionEntity.setArticleId(articleId);
            articleSectionEntity.setSectionId(sectionId);
            articleSectionRepository.save(articleSectionEntity);
        });
    }

    private void create(String articleId, Integer sectionId) {
        ArticleSectionEntity articleSectionEntity = new ArticleSectionEntity();
        articleSectionEntity.setArticleId(articleId);
        articleSectionEntity.setSectionId(sectionId);
        articleSectionRepository.save(articleSectionEntity);
    }

    public void update(String articleId, List<Integer> newList) {
        List<Integer> oldList = articleSectionRepository.getSectionIdsByArticleId(articleId);
        newList.stream().filter(n -> !oldList.contains(n)).forEach(sectionId -> create(articleId, sectionId));
        oldList.stream().filter(old -> !newList.contains(old)).forEach(sectionId -> articleSectionRepository.deleteByArticleIdAndSectionId(articleId, sectionId));
    }
}
