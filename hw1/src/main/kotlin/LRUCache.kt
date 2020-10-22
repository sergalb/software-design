import java.util.HashMap

class LRUCache<T>(
    val CAPACITY: Int = 10
) {
    data class Node<T>(var prev: Node<T>? = null, var next: Node<T>? = null, val data: T) {
        override fun toString(): String {
            return data.toString()
        }
    }
    inner class FiniteList<T> {
        var head: Node<T>? = null
        var tail: Node<T>? = null

        var size = 0
            private set

        fun add(newElement: T) {
            if (size == CAPACITY) {
                tail = tail!!.prev
                tail!!.next = null
                size--
            }
            if (size == 0) {
                head = Node(null, null, newElement)
                tail = head
            } else {
                val newNode = Node(null, head, newElement)
                head!!.prev = newNode
                head = newNode
            }
            size++
            assert(head != null)
            assert(tail != null)

        }

        fun remove(node: Node<T>) {
            assert(contains(node))
            if (size > 1) {
                node.next?.prev = node.prev
                node.prev?.next = node.next
            } else {
                head = null
                tail = null
            }
            size--
        }

        fun first(): Node<T> {
            assert(size > 0)
            return head!!
        }

        fun last(): Node<T> {
            assert(size > 0)
            return tail!!
        }

        private fun contains(node: Node<T>): Boolean {
            var listNode = head
            while (listNode != null) {
                if (node == listNode) return true
                listNode = listNode.next
            }
            return false
        }
    }

    private val finiteStack = FiniteList<T>()
    private val hashtable: MutableMap<T, Node<T>> = HashMap()
    var size = 0
        private set
    //new size = old size or old size + 1
    fun add(newElement: T) {
        val stackSize = finiteStack.size
        val hashtableSize = hashtable.size
        assert(stackSize == hashtableSize)
        val node = hashtable[newElement]
        if (node != null) {
            finiteStack.remove(node)
            hashtable.remove(newElement)
        } else if (finiteStack.size == CAPACITY) {
            hashtable.remove(finiteStack.last().data)
        }
        finiteStack.add(newElement)
        hashtable[newElement] = finiteStack.first()
        val newStackSize = finiteStack.size
        val newHashtableSize = hashtable.size
        size = newStackSize
        assert(newStackSize == newHashtableSize)
        assert(newStackSize == stackSize || newStackSize == stackSize + 1)
    }

    fun getLRUElement(): T? {
        return finiteStack.head?.data
    }
}

