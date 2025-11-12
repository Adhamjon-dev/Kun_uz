package dasturlash.uz.service;

import dasturlash.uz.dto.SectionDTO;
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

    private void create(String articleId, Integer sectionId) {
        ArticleSectionEntity articleSectionEntity = new ArticleSectionEntity();
        articleSectionEntity.setArticleId(articleId);
        articleSectionEntity.setSectionId(sectionId);
        articleSectionRepository.save(articleSectionEntity);
    }

    public void merge(String articleId, List<SectionDTO> dtoList) {
        List<Integer> newList = dtoList.stream().map(SectionDTO::getId).toList();
        List<Integer> oldList = articleSectionRepository.getSectionIdListByArticleId(articleId);

        newList.stream().filter(n -> !oldList.contains(n)).forEach(sectionId -> create(articleId, sectionId));
        oldList.stream().filter(old -> !newList.contains(old)).forEach(sectionId -> articleSectionRepository.deleteByArticleIdAndSectionId(articleId, sectionId));
    }
}
