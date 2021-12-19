fun main() {

    fun String.isUpper() : Boolean = this.all { it.isUpperCase() }

    fun List<String>.smallCaveNotVisitedTwice() : Boolean {
        val smallerCaves = this.filter { !it.isUpper() }
        return smallerCaves.size == smallerCaves.toSet().size
        //return .groupingBy { it }.eachCount().all { it.value < 2 }
    }

    fun List<String>.smallCavesVisitedTwice() : Int {
        return this.filter { !it.isUpper() }.groupingBy { it }.eachCount().map { if (it.value > 1) 1 else 0 }.sum()
    }

    fun findAllPaths(graph: Map<String, List<String>>, visitSmallCaseTwice: Boolean) : List<List<String>> {
        val paths = ArrayList<List<String>>()
        val nodesAndPaths = ArrayDeque<Pair<String, MutableList<String>>>()
        nodesAndPaths.add(Pair("start", ArrayList()))
        while (nodesAndPaths.size > 0) {
            val (curNode, curPath) = nodesAndPaths.last()
            nodesAndPaths.removeLast()

            if (curNode == "end") {
                curPath.add(curNode)
                paths.add(curPath)
            }
            else {
                graph[curNode]?.forEach { child ->
                    // is big cave or little cave hasn't been  visited in path yet
                    if (child.isUpper() ||
                        !curPath.contains(child) ||
                        (visitSmallCaseTwice && child != "start" && child != "end" && curPath.smallCaveNotVisitedTwice())) {
                        val newPath = curPath.map { it }.toMutableList()
                        newPath.add(curNode)

                        if (newPath.smallCavesVisitedTwice() < 2)
                            nodesAndPaths.add(Pair(child, newPath))
                    }
                }
            }
        }
        return paths
    }

    fun addEdge(graph: MutableMap<String, MutableList<String>>,
                key: String,
                value: String): MutableMap<String, MutableList<String>> {
        if (!graph.containsKey(key))
            graph[key] = ArrayList(listOf(value))
        else
            graph[key]?.add(value)
        return graph
    }

    fun createGraph(input: List<String>): Map<String, List<String>> {
        var graph: MutableMap<String, MutableList<String>> = HashMap()
        input.forEach {
            val (a, b) = it.split("-")
            graph = addEdge(graph, a, b)
            graph = addEdge(graph, b, a)
        }
        return graph
    }

    fun part1(lines: List<String>): Int {
        val graph = createGraph(lines)
        return findAllPaths(graph, false).size
    }

    fun part2(lines: List<String>): Int {
        val graph = createGraph(lines)
        return findAllPaths(graph, true).size
    }
    println(listOf("start", "HN", "dc", "HN", "kj", "kj","dc" ).smallCavesVisitedTwice())

    val testInput = readInput("Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 36)

    val testInput2 = readInput("Day12_test_2")
    check(part1(testInput2) == 19)
    check(part2(testInput2) == 103)

    val testInput3 = readInput("Day12_test_3")
    check(part1(testInput3) == 226)
    check(part2(testInput3) == 3509)

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}