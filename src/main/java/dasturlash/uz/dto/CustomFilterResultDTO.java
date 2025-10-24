package dasturlash.uz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CustomFilterResultDTO<T> {
    List<T> content;
    Long totalCount;
}
