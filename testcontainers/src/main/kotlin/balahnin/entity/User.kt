package balahnin.entity

import com.fasterxml.jackson.annotation.JsonIdentityReference
import javax.persistence.*

@Entity(name = "User")
@Table(name = "user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    var name: String = "",
    var money: Int = 0,

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "owner")
    @JsonIdentityReference(alwaysAsId = true)
    var stocks: MutableList<Stock> = mutableListOf()
)
