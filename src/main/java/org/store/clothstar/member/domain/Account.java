package org.store.clothstar.member.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @Column(unique = true)
    private String email;
    private String password;

    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE)
    @JsonIgnore
    private List<Authorization> authorizations = new ArrayList<>();

    public void updatePassword(String password) {
        this.password = password;
    }
}
