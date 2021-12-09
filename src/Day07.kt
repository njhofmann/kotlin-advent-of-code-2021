import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

fun main() {

    fun part1(input: List<String>): Int {
        val nums = input[0].split(",").map { it.toInt() }.sorted()
        val median = nums[floor(nums.size / 2.0).toInt()]
        return nums.sumOf { abs(it - median) }
    }

    fun part2(input: List<String>): Int {
        val nums = input[0].split(",").map { it.toInt() }
        val avg = nums.sum() / nums.size
        return nums.sumOf {
            val n = abs(it - avg).toDouble()
            (n.pow(2) + n) / 2
        }.toInt()
    }

    val testInput = readInput("Day07_test")
    check(part1(testInput) == 37)
    print(part2(testInput))
    //check(part2(testInput) == 168.0)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}