package dpapps.exchangecurrencyapp.exchange.entities;

import dpapps.exchangecurrencyapp.configuration.AppVariables;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User
{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String login;

    @Column(nullable=false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(
            name="users_roles",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private List<Role> roles = new ArrayList<>();

    @OneToMany
    private List<ApiKey> apiKeys = new ArrayList<>();

    private boolean isNonLocked = true;

    private int currentRequestsCount = 0;

    public void increaseNumberOfUsages() {
        if (currentRequestsCount < AppVariables.GLOBAL_LIMIT_OF_DAILY_USAGES) {
            currentRequestsCount++;
        }
    }

    public boolean isRequestLimitIsReached() {
        if (currentRequestsCount == AppVariables.GLOBAL_LIMIT_OF_DAILY_USAGES) {
            return true;
        }
        return false;
    }



}