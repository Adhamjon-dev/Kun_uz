package dasturlash.uz.entitiy;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AttachEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
}
