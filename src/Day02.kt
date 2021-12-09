fun main() {

    fun part1(input: List<String>): Int {
        var (x, y) = listOf(0, 0)
        input.forEach {
            val (move, distStr) = it.split(" ")
            val dist = distStr.toInt()
            when (move) {
                "forward" -> { x += dist }
                "up" -> { y -= dist }
                else -> { y += dist }
            }
        }
        return x * y
    }

    fun part2(input: List<String>): Int {
        var (x, y, aim) = listOf(0, 0, 0)
        input.forEach {
            val (move, distStr) = it.split(" ")
            val dist = distStr.toInt()
            when (move) {
                "forward" -> { x += dist; y += aim * dist }
                "up" -> { aim -= dist }
                else -> { aim += dist }
            }
        }
        return x * y
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}