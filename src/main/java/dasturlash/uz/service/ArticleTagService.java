package dasturlash.uz.service;

import dasturlash.uz.dto.TagDTO;
import dasturlash.uz.entitiy.article.ArticleTagEntity;
import dasturlash.uz.repository.ArticleTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTagService {
    @Autowired
    private ArticleTagRepository articleTagRepository;

    private void create(String articleId, Integer tagId) {
        ArticleTagEntity articleTagEntity = new ArticleTagEntity();
        articleTagEntity.setArticleId(articleId);
        articleTagEntity.setTagId(tagId);
        articleTagRepository.save(articleTagEntity);
    }

    public void merge(String articleId, List<TagDTO> dtoList) {
        List<Integer> newList = dtoList.stream().map(TagDTO::getId).toList();
        List<Integer> oldList = articleTagRepository.getTagIdListByArticleId(articleId);

        newList.stream().filter(n -> !oldList.contains(n)).forEach(catId -> create(articleId, catId));
        oldList.stream().filter(old -> !newList.contains(old)).forEach(catId -> articleTagRepository.deleteByArticleIdAndTagId(articleId, catId));
    }
}
