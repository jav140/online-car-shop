package uz.jl.domains;



import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String body;
    @Column(nullable = false)
    private Timestamp createdAt;
    @Column(nullable = false)
    private String createdBy;
    @Column(nullable = false)
    private Long product_id;

//    private Product product;
}
