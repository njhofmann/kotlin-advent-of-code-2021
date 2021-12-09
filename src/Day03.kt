import kotlin.math.floor
import kotlin.math.pow

fun main() {

    fun BooleanArray.boolArrayToInt() : Double {
        return this.mapIndexed { i, it -> if (it) 2.0.pow(this.size - i - 1)  else 0.0 }.sum()
    }

    fun part1(input: List<String>): Int {
        val colCounts = IntArray(input[0].length)
        val colThreshold = floor(input.size / 2.0)
        input.forEach { it.forEachIndexed { i, bit -> if (bit == '1') colCounts[i] += 1 } }
        val oneMostCommon = colCounts.map { it > colThreshold }.toBooleanArray()
        val zeroMostCommon = oneMostCommon.map { !it }.toBooleanArray()
        return (oneMostCommon.boolArrayToInt() * zeroMostCommon.boolArrayToInt()).toInt()
    }

    fun mostCommonBits(boolLists: List<List<Boolean>>) : List<Int> {
        val oneCounts = IntArray(boolLists[0].size)
        boolLists.forEach { it.forEachIndexed { i, bool -> if (bool) oneCounts[i] += 1 } }
        val zeroCounts = oneCounts.map { boolLists.size - it }
        return (0 until boolLists[0].size).map { i ->
            if (oneCounts[i] == zeroCounts[i])
                2
            else if (oneCounts[i] > zeroCounts[i])
                1
            else
                0
        }
    }

    fun List<Boolean>.boolArrayToInt() : Double {
        return this.mapIndexed { i, it -> if (it) 2.0.pow(this.size - i - 1)  else 0.0 }.sum()
    }

    fun filterBoolLists(boolLists: List<List<Boolean>>,
                        filt: (List<Boolean>, List<Int>, Int) -> Boolean,
                        colIdx: Int = 0) : List<Boolean> {
        val mostCommonBits = mostCommonBits(boolLists)
        val filteredLists = boolLists.filter { filt(it, mostCommonBits, colIdx) }
        if (filteredLists.size == 1)
            return filteredLists[0]
        return filterBoolLists(filteredLists, filt, colIdx + 1)
    }

    fun part2(input: List<String>): Int {
        val boolLists = input.map { it.map { char -> char == '1' } }

        val oxGenRating = filterBoolLists(boolLists, { boolList, mostCommon, i ->
            (boolList[i] && (mostCommon[i] == 1 || mostCommon[i] == 2)) ||
                    (!boolList[i] && mostCommon[i] == 0)
        })

        val co2ScrubRating = filterBoolLists(boolLists, { boolList, mostCommon, i ->
            (boolList[i] && mostCommon[i] == 0) ||
                    (!boolList[i] && (mostCommon[i] == 1 || mostCommon[i] == 2))
        })

        return (oxGenRating.boolArrayToInt() * co2ScrubRating.boolArrayToInt()).toInt()
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}