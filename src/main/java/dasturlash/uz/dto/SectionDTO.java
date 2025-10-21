package dasturlash.uz.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionDTO {
    private Integer id;

    @NotNull(message = "Order number required")
    private Integer orderNumber;
    @NotBlank(message = "NameUz required")
    private String nameUz;
    @NotBlank(message = "NameRu required")
    private String nameRu;
    @NotBlank(message = "NameEn required")
    private String nameEn;
    @NotBlank(message = "SectionKey required")
    private String sectionKey;

    private Boolean visible;
    private LocalDateTime createdDate;
    private Integer imageId;
}
