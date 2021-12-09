fun main() {

    fun step(timers: MutableList<Long>) : MutableList<Long> {
        val temp = timers.map { 0.toLong() }.toMutableList()
        val newFish = timers[0]
        timers.reversed().forEachIndexed { i, it ->
            val idx = timers.size - i - 1
            if (idx > 0) {
                temp[idx - 1] = it
            }
        }
        temp[6] += newFish
        temp[8] = newFish
        return temp
    }

    fun part1(input: List<String>): Long {
        val rawFishTimers = input.map { it.split(",") }.flatten().map { it.toInt() }
        val fishTimers = (0 until 9).map { 0 }.toMutableList()
        rawFishTimers.forEach { fishTimers[it] += 1 }
        var bigFishTimer = fishTimers.map { it.toLong() }.toMutableList()
        (0 until 80).forEach { bigFishTimer = step(bigFishTimer) }
        return bigFishTimer.sum()
    }

    fun part2(input: List<String>): Long {
        val rawFishTimers = input.map { it.split(",") }.flatten().map { it.toInt() }
        var fishTimers = (0 until 9).map { 0 }.toMutableList()
        rawFishTimers.forEach { fishTimers[it] += 1 }
        var bigFishTimer = fishTimers.map { it.toLong() }.toMutableList()
        (0 until 256).forEach { bigFishTimer = step(bigFishTimer) }
        return bigFishTimer.sum()
    }

    val testInput = readInput("Day06_test")
    check(part1(testInput) == 5934.toLong())
    check(part2(testInput) == 26984457539.toLong())

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}