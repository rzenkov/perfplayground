package ru.rzen.perfplayground.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User extends Identifiable {
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subdivision_id", referencedColumnName = "id")
    private Subdivision subdivision;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_to_position",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "position_id")
    )
    private Set<Position> positions = new HashSet<>();

    @Formula("""
        (select count(*) > 0
            from user_to_position up
            left join position p
            on up.position_id = p.id
            where p.type = 'MANAGER' and up.user_id = id)
        """)
    private boolean manager = false;

}
