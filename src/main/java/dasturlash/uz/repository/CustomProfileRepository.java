package dasturlash.uz.repository;

import dasturlash.uz.dto.CustomFilterResultDTO;
import dasturlash.uz.dto.ProfileFilterDTO;
import dasturlash.uz.entitiy.ProfileEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomProfileRepository {
    @Autowired
    private EntityManager entityManager;

    public CustomFilterResultDTO<ProfileEntity> filter(ProfileFilterDTO filter, int page, int size) {
        StringBuilder conditionBuilder = new StringBuilder("where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        if (filter.getName() != null) {
            conditionBuilder.append("and p.name like :name ");
            params.put("name", filter.getName());
        }
        if (filter.getSurname() != null) {
            conditionBuilder.append("and p.surname like :surname ");
            params.put("surname", filter.getSurname());
        }
        if (filter.getRole() != null) {
            conditionBuilder.append("and pr.role = :role ");
            params.put("role", filter.getRole());
        }
        if (filter.getUsername() != null) {
            conditionBuilder.append("and p.username = :username ");
            params.put("username", filter.getUsername());
        }
        if (filter.getCreatedDateFrom() != null) {
            LocalDateTime dateFrom = LocalDateTime.of(filter.getCreatedDateFrom(), LocalTime.MIN);
            conditionBuilder.append("and p.createdDate >= :createdDateFrom ");
            params.put("createdDateFrom", dateFrom);
        }
        if (filter.getCreatedDateTo() != null) {
            LocalDateTime dateTo = LocalDateTime.of(filter.getCreatedDateTo(), LocalTime.MAX);
            conditionBuilder.append("and p.createdDate <= :createdDateTo ");
            params.put("createdDateTo", dateTo);
        }
        StringBuilder selectBuilder = new StringBuilder("Select p From ProfileRoleEntity pr inner join pr.profile p ");
        selectBuilder.append(conditionBuilder);

        Query selectQuery = entityManager.createQuery(selectBuilder.toString());
        selectQuery.setFirstResult(page * size); // offset
        selectQuery.setMaxResults(size); // limit

        StringBuilder countBuilder = new StringBuilder("Select count(p) From ProfileRoleEntity pr inner join pr.profile p ");
        countBuilder.append(conditionBuilder);
        Query countQuery = entityManager.createQuery(countBuilder.toString());


        for (Map.Entry<String, Object> entry : params.entrySet()) {
            selectQuery.setParameter(entry.getKey(), entry.getValue());
            countQuery.setParameter(entry.getKey(), entry.getValue());
        }
//        params.forEach(selectQuery::setParameter);

        List<ProfileEntity> entityList = selectQuery.getResultList();
        Long totalElements = (Long) countQuery.getSingleResult();

        return new CustomFilterResultDTO<>(entityList, totalElements);
    }
}
