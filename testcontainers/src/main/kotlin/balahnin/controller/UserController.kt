package balahnin.controller

import balahnin.entity.Stock
import balahnin.entity.User
import balahnin.repository.StockRepository
import balahnin.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/user/")
class UserController {
    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var stockRepository: StockRepository

    @PostMapping("/add-user")
    fun addUser(@RequestParam name: String): User {
        val user = User(name = name)
        userRepository.save(user)
        return user
    }

    @PostMapping("/add-money")
    fun addMoney(@RequestParam userId: Int, @RequestParam money: Int) {
        val user = userRepository.getOne(userId)
        user.money += money
        userRepository.save(user)
    }

    @GetMapping("/get-stocks")
    fun getStocks(@RequestParam userId: Int): List<String> {
        val user = userRepository.getOne(userId)
        return user.stocks.map(Stock::toString)
    }

    @GetMapping("/get-stocks-count")
    fun getStocksCount(@RequestParam userId: Int): Int {
        val user = userRepository.getOne(userId)
        return user.stocks.size
    }

    @GetMapping("/get-stocks-total-cost")
    fun getStocksTotalCost(@RequestParam userId: Int): Int {
        val user = userRepository.getOne(userId)
        return user.stocks.totalCost()
    }

    @GetMapping("/get-total-money")
    fun getTotalMoney(@RequestParam userId: Int): Int {
        val user = userRepository.getOne(userId)
        return user.stocks.totalCost() + user.money
    }

    @PostMapping("/buy-stock")
    fun buyStock(@RequestParam userId: Int, @RequestParam stockId: Int): Boolean {
        val user = userRepository.getOne(userId)
        val stock = stockRepository.getOne(stockId)
        val price = stock.price()
        if (user.money < price) {
            return false
        }
        stock.owner = user
        stockRepository.save(stock)
        user.money -= price
        userRepository.save(user)
        return true
    }

    @PostMapping("/sell-stock")
    fun sellStocks(@RequestParam userId: Int, @RequestParam stockId: Int) {
        val user = userRepository.getOne(userId)
        val stock = stockRepository.getOne(stockId)
        stock.owner = null
        stockRepository.save(stock)
        user.money += stock.price()
        userRepository.save(user)
    }

    fun Iterable<Stock>.totalCost() = sumBy { it.company.stockPrice }
}