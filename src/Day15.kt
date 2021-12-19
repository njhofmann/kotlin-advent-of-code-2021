import java.util.*
import kotlin.collections.ArrayDeque

typealias Node = Pair<Int, Int>

fun main() {

    fun addCols(cols: List<List<Int>>, fodder: List<List<Int>>, i: Int) : List<List<Int>> {
        return cols.mapIndexed { idx, row ->
            val newRow = row.toMutableList()
            newRow.addAll(fodder[idx].map {
                val newVal = it + i
                if (newVal > 9) newVal % 9 else newVal
            })
            newRow
        }
    }

    fun addRows(rows: List<List<Int>>, fodder: List<List<Int>>, i: Int) : List<List<Int>> {
        val newRows = rows.toMutableList()
        fodder.forEachIndexed{ idx, col ->
            newRows.add(col.map {
                val newVal = it + i
                if (newVal > 9) newVal % 9 else newVal
            })
        }
        return newRows
    }

    fun expandCols(nums: List<List<Int>>, n: Int) : List<List<Int>> {
        return (1 until n).fold(nums) { acc, i -> addCols(acc, nums, i) }
    }

    fun expandRows(nums: List<List<Int>>, n: Int) : List<List<Int>> {
        return (1 until n).fold(nums) { acc, i -> addRows(acc, nums, i) }
    }

    fun readGraph(lines: List<String>, n: Int = 0): Pair<ArrayDeque<Node>, Map<Pair<Node, Node>, Int>> {
        var nums = lines.map { line -> line.split("").filter { it.isNotEmpty() }.map { it.toInt() } }

        if (n > 0) {
            nums = expandCols(nums, n)
            nums = expandRows(nums, n)
        }

        val nodes = ArrayDeque<Node>()
        val edgeWeights = HashMap<Pair<Node, Node>, Int>()
        nums.forEachIndexed { x, row ->
            row.forEachIndexed { y, weight ->
                val node = Pair(x, y)
                nodes.add(node)

                if (y > 0) {
                    edgeWeights[Pair(Pair(x, y - 1), node)] = weight
                    edgeWeights[Pair(node, Pair(x, y - 1))] = nums[x][y - 1]
                }

                if (x > 0) {
                    edgeWeights[Pair(Pair(x - 1, y), node)] = weight
                    edgeWeights[Pair(node, Pair(x - 1, y))] = nums[x - 1][y]
                }
            }
        }
        return Pair(nodes, edgeWeights)
    }

    fun findShortestPath(
        nodes: ArrayDeque<Node>,
        edgeWeights: Map<Pair<Node, Node>, Int>,
        end: Node
    ): Int {
        val start = Pair(0, 0)
        val visitedNodes = HashSet<Node>()
        val dists = HashMap<Node, Int>()
        val queue = PriorityQueue<Node>(nodes.size) { a, b -> dists[a]!!.compareTo(dists[b]!!) }
        queue.add(start)
        dists[start] = 0
        while (queue.isNotEmpty()) {
            val curNode = queue.poll()

            if (curNode == end)
                return dists[curNode]!!

            if (!visitedNodes.contains(curNode)) {
                visitedNodes.add(curNode)
                val (x, y) = curNode

                val children =
                    listOf(Pair(x - 1, y), Pair(x + 1, y), Pair(x, y - 1), Pair(x, y + 1))
                children.forEach {
                    if (nodes.contains(it)) {
                        val weight = edgeWeights[Pair(curNode, it)]!!
                        val newDist = dists[curNode]!! + weight
                        if (!dists.contains(it) || newDist < dists[it]!!)
                            dists[it] = newDist

                        if (!visitedNodes.contains(it))
                            queue.add(it)
                    }
                }
            }
        }
        return 0
    }

    fun part1(lines: List<String>): Int {
        val (nodes, edgeWeights) = readGraph(lines)
        val end = nodes.last()
        return findShortestPath(nodes, edgeWeights, end)
    }

    fun part2(lines: List<String>): Int {
        val (nodes, edgeWeights) = readGraph(lines, 5)
        val end = nodes.last()
        return findShortestPath(nodes, edgeWeights, end)
    }

    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part2(testInput) == 315)

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}