import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LRUCacheTests {
    lateinit var lruCache: LRUCache<Int>

    @BeforeEach
    fun createLRU() {
        lruCache = LRUCache(5)
    }

    @Test
    fun empty() {
        assertEquals(0, lruCache.size)
    }

    @Test
    fun emptyGetElementShouldBeNull() {
        assertEquals(null, lruCache.getLRUElement())
    }

    @Test
    fun addOneElementSizeShouldBe1() {
        lruCache.add(2)
        assertEquals(1, lruCache.size)
    }

    @Test
    fun addTwoDiferentElementsSizeShouldBe2() {
        lruCache.add(3)
        lruCache.add(4)
        assertEquals(2, lruCache.size)
    }

    @Test
    fun addAndGetElementShouldBeEquals() {
        val addedElement = 2
        lruCache.add(addedElement)
        assertEquals(addedElement, lruCache.getLRUElement())
    }

    @Test
    fun addAndGetTwoElementShouldBeEquals() {
        val firstElement = 3
        val secondElement = 4
        lruCache.add(firstElement)
        lruCache.add(secondElement)
        assertEquals(secondElement, lruCache.getLRUElement())
    }

    @Test
    fun addTwoSameAndOneDifferentElementSizeShouldBe2() {
        val firstElement = 3
        val secondElement = 4
        lruCache.add(firstElement)
        lruCache.add(secondElement)
        lruCache.add(firstElement)
        assertEquals(2, lruCache.size)
    }

    @Test
    fun addSameElementShouldBeOnTop() {
        val firstElement = 3
        val secondElement = 4
        lruCache.add(firstElement)
        lruCache.add(secondElement)
        lruCache.add(firstElement)
        assertEquals(firstElement, lruCache.getLRUElement())
    }

    @Test
    fun addCapacityPlusOneElementsSizeShouldBeCapacity() {
        for (element in 0..lruCache.CAPACITY) {
            lruCache.add(element)
        }
        assertEquals(lruCache.CAPACITY, lruCache.size)
    }
}