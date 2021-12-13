import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.pow

fun main() {

    fun readSignalsAndDigits(input: List<String>) : List<Pair<List<String>, List<String>>> {
        return input.map {
            val line = it.split("|")
            val signals = line[0].split(" ").filter { it.isNotEmpty() }
            val wire = line[1].split(" ").filter { it.isNotEmpty() }
            Pair(signals, wire)
        }
    }

    fun part1(input: List<String>): Int {
        val signalsAndDigits = readSignalsAndDigits(input)
        return signalsAndDigits.sumOf { it.second.filter { digits -> setOf(2, 3, 4, 7).contains(digits.length) }.size }
    }

//    fun part2(input: List<String>): Int {
//
//    }

    val testInput = readInput("Day08_test")
    println(part1(testInput))
    check(part1(testInput) == 26)
    //check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    //println(part2(input))
}