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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false,unique = true)
    private String name;
    @Column(nullable = false)
    private Long price;
    private Timestamp createdAt;
    private String createdBy;

    @OneToOne
    private Uploads file;


}
