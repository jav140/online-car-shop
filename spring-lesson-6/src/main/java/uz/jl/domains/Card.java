package uz.jl.domains;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String number;
    @Column(nullable = false)
    private String expiry;
    @Column(nullable = false)
    private Long balance;

//    @OneToMany
//    private AuthUser user;

}
