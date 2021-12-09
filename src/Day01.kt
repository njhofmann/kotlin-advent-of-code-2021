fun main() {
    fun readDepths(input: List<String>) : List<Int> {
        return input.map { it.toInt() }
    }

    fun part1(depths: List<Int>): Int {
        return depths.mapIndexed { i, it -> if (i == 0 || it <= depths[i - 1]) 0 else 1 }.sum()
    }

    fun part2(depths: List<Int>): Int {
        return depths.mapIndexed { i, it -> if (i < 3 || it <= depths[i - 3]) 0 else 1 }.sum()
    }

    val testInput = readDepths(readInput("Day01_test"))
    check(part1(testInput) == 7)
    check(part2(testInput) == 5)

    val input = readDepths(readInput("Day01_test"))
    println(part1(input))
    println(part2(input))
}