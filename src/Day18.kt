import kotlin.math.ceil
import kotlin.math.floor

fun findRawPairsSplit(rawPairs: String): Int {
    val stack = ArrayDeque<Char>()
    rawPairs.forEachIndexed { idx, it ->
        if (stack.isEmpty() && idx != 0)
            return idx
        else if (stack.isNotEmpty() && stack.last() == '[' && it == ']')
            stack.removeLast()
        else if (it == ']' || it == '[')
            stack.add(it)
    }
    return -1
}

interface SnailfishPair {
    fun splitLeftMostOverTen(): Boolean
    fun split(): SnailfishPair
    fun add(other: SnailfishPair): SnailfishPair
    fun magnitude(): Int
    fun isLeaf(): Boolean
    fun reduce()
    fun explodeLeftmostFourDeep(depth: Int): Triple<Boolean, Int?, Int?>
    fun zeroLeftChild()
    fun zeroRightChild()
    fun incLeftmost(inc: Int): Boolean
    fun incRightmost(inc: Int): Boolean
    fun inc(inc: Int): SnailfishPair
}

class LeafPair(val value: Int) : SnailfishPair {

    override fun splitLeftMostOverTen(): Boolean = false

    override fun split(): SnailfishPair {
        return NodePair(LeafPair(floor(value / 2.0).toInt()), LeafPair(ceil(value / 2.0).toInt()))
    }

    override fun add(other: SnailfishPair): SnailfishPair = this

    override fun magnitude(): Int = value

    override fun toString(): String = value.toString()

    override fun isLeaf(): Boolean = true

    override fun reduce() {}

    override fun zeroLeftChild() {}

    override fun zeroRightChild() {}

    override fun explodeLeftmostFourDeep(depth: Int): Triple<Boolean, Int?, Int?> = Triple(false, null, null)

    override fun inc(inc: Int): SnailfishPair = LeafPair(inc + value)

    override fun incLeftmost(inc: Int): Boolean = false

    override fun incRightmost(inc: Int): Boolean = false
}

class NodePair(private var left: SnailfishPair, private var right: SnailfishPair) : SnailfishPair {

    override fun add(other: SnailfishPair): SnailfishPair {
        val new = NodePair(this, other)
        new.reduce()
        return new
    }

    override fun magnitude(): Int = (3 * left.magnitude()) + (2 * right.magnitude())

    override fun toString(): String = "[%s,%s]".format(left.toString(), right.toString())

    override fun isLeaf(): Boolean = false

    override fun explodeLeftmostFourDeep(depth: Int): Triple<Boolean, Int?, Int?> {
        if (depth == 3) {
            if (!left.isLeaf()) {
                val leftNode = left as NodePair
                val leftNodeLeftVal: Int = (leftNode.left as LeafPair).value
                var leftNodeRightVal: Int? = (leftNode.right as LeafPair).value

                if (right.isLeaf()) {
                    right = right.inc(leftNodeRightVal!!)
                    leftNodeRightVal = null
                } else if (right.incLeftmost(leftNodeRightVal!!))
                    leftNodeRightVal = null

                zeroLeftChild()
                return Triple(true, leftNodeLeftVal, leftNodeRightVal)
            } else if (!right.isLeaf()) {
                val rightNode = right as NodePair
                var rightNodeLeftVal: Int? = (rightNode.left as LeafPair).value
                val rightNodeRightVal: Int = (rightNode.right as LeafPair).value

                if (left.isLeaf()) {
                    left = left.inc(rightNodeLeftVal!!)
                    rightNodeLeftVal = null
                } else if (left.incRightmost(rightNodeLeftVal!!))
                    rightNodeLeftVal = null

                zeroRightChild()
                return Triple(true, rightNodeLeftVal, rightNodeRightVal)
            }
        }

        var (leftExploded, leftSideLeftVal, leftSideRightVal) = left.explodeLeftmostFourDeep(depth + 1)
        if (leftExploded) {
            if (leftSideRightVal != null) {
                if (right.isLeaf()) {
                    right = right.inc(leftSideRightVal)
                    leftSideRightVal = null
                } else if (right.incLeftmost(leftSideRightVal))
                    leftSideRightVal = null
            }

            return Triple(true, leftSideLeftVal, leftSideRightVal)
        }

        var (rightExploded, rightSideLeftVal, rightSideRightVal) = right.explodeLeftmostFourDeep(depth + 1)
        if (rightExploded) {
            if (rightSideLeftVal != null) {
                if (left.isLeaf()) {
                    left = left.inc(rightSideLeftVal)
                    rightSideLeftVal = null
                } else if (left.incRightmost(rightSideLeftVal))
                    rightSideLeftVal = null
            }

            return Triple(true, rightSideLeftVal, rightSideRightVal)
        }

        return Triple(false, null, null)
    }

    override fun incLeftmost(inc: Int): Boolean {
        if (left.isLeaf()) {
            left = left.inc(inc)
            return true
        }

        if (left.incLeftmost(inc))
            return true

        if (right.isLeaf()) {
            right = right.inc(inc)
            return true
        }

        return right.incLeftmost(inc)
    }

    override fun incRightmost(inc: Int): Boolean {
        if (right.isLeaf()) {
            right = right.inc(inc)
            return true
        }

        if (right.incRightmost(inc))
            return true

        if (left.isLeaf()) {
            left = left.inc(inc)
            return true
        }

        return left.incRightmost(inc)
    }

    override fun inc(inc: Int): SnailfishPair {
        TODO("Not yet implemented")
    }

    override fun zeroLeftChild() {
        left = LeafPair(0)
    }

    override fun zeroRightChild() {
        right = LeafPair(0)
    }

    override fun reduce() {
        var cont = true
        while (cont) {
            cont = false
            if (explodeLeftmostFourDeep(0).first)
                cont = true
            else if (splitLeftMostOverTen())
                cont = true
        }
    }

    override fun splitLeftMostOverTen(): Boolean {
        if (left.isLeaf()) {
            val leftLeaf = left as LeafPair
            if (leftLeaf.value > 9) {
                left = left.split()
                return true
            }
        }

        val leftSplit = left.splitLeftMostOverTen()
        if (leftSplit)
            return true

        if (right.isLeaf()) {
            val rightLeaf = right as LeafPair
            if (rightLeaf.value > 9) {
                right = right.split()
                return true
            }
        }

        return right.splitLeftMostOverTen()
    }

    override fun split(): SnailfishPair {
        TODO("Not yet implemented")
    }
}

fun constructSnailfishPair(rawPair: String): SnailfishPair {
    if (rawPair.length == 1)
        return LeafPair(rawPair[0].digitToInt())

    val rawPairs = rawPair.substring(1, rawPair.length - 1)
    val splitIdx = findRawPairsSplit(rawPairs)

    return NodePair(
        constructSnailfishPair(rawPairs.substring(0, splitIdx)),
        constructSnailfishPair(rawPairs.substring(splitIdx + 1, rawPairs.length))
    )
}

fun main() {

    fun part1(lines: List<String>): Int {
        val snailfishPair = lines.map { constructSnailfishPair(it) }
        val adds = snailfishPair.subList(1, snailfishPair.size)
        val finalPair = adds.fold(snailfishPair[0]) { acc, pair -> acc.add(pair) }
        return finalPair.magnitude()
    }

    fun part2(lines: List<String>): Int {
        val snailfishPairs = lines.map { constructSnailfishPair(it) }
        var largest = -1
        (lines.indices).forEach { x ->
            (lines.indices).forEach { y ->
                if (x != y) {
                    var a = constructSnailfishPair(lines[x]).add(constructSnailfishPair(lines[y])).magnitude()
                    var b = constructSnailfishPair(lines[y]).add(constructSnailfishPair(lines[x])).magnitude()
                    largest = listOf(a, b, largest).maxOrNull()!!
                }
            }
        }
        return largest
    }

    val testInput = readInput("Day18_test")
    check(part1(testInput) == 4140)
    check(part2(testInput) == 3993)

    val input = readInput("Day18")
    println(part1(input))
    println(part2(input))
}