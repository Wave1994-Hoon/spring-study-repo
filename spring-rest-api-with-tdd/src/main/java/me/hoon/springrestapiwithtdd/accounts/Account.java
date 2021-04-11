package me.hoon.springrestapiwithtdd.accounts;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Builder @AllArgsConstructor @NoArgsConstructor
@Getter @Setter @EqualsAndHashCode(of = "id")
@Entity
public class Account {

    @Id @GeneratedValue
    private Integer id;

    private String email;

    private String password;

    @ElementCollection(fetch = FetchType.EAGER) // 여러개의 enum 타입을 가질 때 사용
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles;
}
