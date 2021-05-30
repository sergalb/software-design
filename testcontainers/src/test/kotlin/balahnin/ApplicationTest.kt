package balahnin

import balahnin.controller.StockMarketController
import balahnin.controller.UserController
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest
@Testcontainers
class ApplicationTest {
    @Autowired
    lateinit var userController: UserController

    @Autowired
    lateinit var stockMarketController: StockMarketController

    companion object {
        @Container
        var container = FixedHostPortGenericContainer<Nothing>("testcontainers:0.0.1-SNAPSHOT")
            .apply {
                withFixedExposedPort(8080, 8080)
                withExposedPorts(8080)
            }
    }

    @Test
    fun addUser() {
        val user = userController.addUser("Sergey")
        assertEquals(0, userController.getTotalMoney(user.id))
    }

    @Test
    fun addMoney(){
        val user = userController.addUser("Money")
        userController.addMoney(user.id, 17)

        assertEquals(17, userController.getTotalMoney(user.id))
    }

    @Test
    fun buyStocks(){
        val user = userController.addUser("BuyStocks")
        userController.addMoney(user.id, 1)
        assertTrue(userController.buyStock(user.id, 1))
    }

    @Test
    fun checkStocks(){
        val user = userController.addUser("CheckStocks")
        userController.addMoney(user.id, 3)
        userController.buyStock(user.id, 1)
        userController.buyStock(user.id, 2)

        assertEquals(listOf("1 1 1", "2 2 1"), userController.getStocks(user.id))
    }

    @Test
    fun checkTotalMoney(){
        val user = userController.addUser("CheckTotalMoney")
        userController.addMoney(user.id, 5)
        userController.buyStock(user.id, 1)
        userController.buyStock(user.id, 2)

        assertEquals(5, userController.getTotalMoney(user.id))
    }

    @Test
    fun failedBuyStocks(){
        val user = userController.addUser("FailedStocks")
        userController.addMoney(user.id, 1)
        assertFalse(userController.buyStock(user.id, 2))
    }

}
