package balahnin.entity

import javax.persistence.*

@Entity(name = "Company")
@Table(name = "company")
data class Company(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    var name: String = "",

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
    var stocks: MutableList<Stock> = mutableListOf(),

    var stockPrice: Int = 0
)
