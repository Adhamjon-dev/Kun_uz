package dasturlash.uz.service;

import dasturlash.uz.dto.TagDTO;
import dasturlash.uz.entitiy.TagEntity;
import dasturlash.uz.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    @Autowired
    private TagRepository tagRepository;

    public TagDTO create(TagDTO dto) {
        TagEntity entity = new TagEntity();
        entity.setName(dto.getName());
        tagRepository.save(entity);

        // response
        dto.setId(entity.getId());
        return dto;
    }

    public List<TagDTO> getAll() {
        Iterable<TagEntity> entityList = tagRepository.findAll();

        List<TagDTO> dtoList = new ArrayList<>();
        entityList.forEach(entity -> dtoList.add(toDto(entity)));
        return dtoList;
    }

    private TagDTO toDto(TagEntity entity) {
        TagDTO dto = new TagDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        return dto;
    }
}
