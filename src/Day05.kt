import kotlin.math.abs

class Line(rawString: String) {

    private val x1: Int

    private val y1: Int

    private val x2: Int

    private val y2: Int

    init {
        val parts = rawString.split(" ").filter { it.isNotEmpty() }
        val firstPoint = rawPoint(parts[0])
        x1 = firstPoint.first
        y1 = firstPoint.second
        val secondPoint = rawPoint(parts[2])
        x2 = secondPoint.first
        y2 = secondPoint.second
    }

    private fun rawPoint(string: String) : Pair<Int, Int> {
        val parts = string.split(",").filter { it.isNotEmpty() }.map { it.toInt() }
        return Pair(parts[0], parts[1])
    }

    fun isHorzOrVert() : Boolean {
        return x1 == x2 || y1 == y2
    }

    fun isDiag() : Boolean {
        return abs(x1 - x2) == abs(y1 - y2)
    }

    fun coveredPoints() : List<Pair<Int, Int>> {
        if (x1 == x2) {
            val (minY, maxY) = if (y1 < y2) Pair(y1, y2) else Pair(y2, y1)
            return (minY..maxY).map { Pair(x1, it) }
        }
        else if (y1 == y2) {
            val (minX, maxX) = if (x1 < x2) Pair(x1, x2) else Pair(x2, x1)
            return (minX..maxX).map { Pair(it, y1) }
        }
        val points = ArrayList<Pair<Int, Int>>()
        val diff = abs(y1 - y2)
        (0..diff).map {
            if (y2 > y1 && x2 > x1) {
                points.add(Pair(x2 - it, y2 - it))
            } else if (y2 > y1 && x2 < x1) {
                points.add(Pair(x2 + it, y2 - it))
            }
            else if (y2 < y1 && x2 > x1) {
                points.add(Pair(x2 - it, y2 + it))
            }
            else { //(y2 < y1 && x2 < x1)
                points.add(Pair(x2 + it, y2 + it))
            }
        }

        return points
    }
}


fun main() {

    fun part1(input: List<String>): Int {
        val lines = input.map { Line(it) }.filter { it.isHorzOrVert() }
        val coveredPoints = lines.map { it.coveredPoints() }.flatten()
        val multCoveredPoints = coveredPoints.groupingBy { it }.eachCount().filter { it.value > 1 }.map { it.key }
        return multCoveredPoints.size
    }

    fun part2(input: List<String>): Int {
        val lines = input.map { Line(it) }.filter { it.isHorzOrVert() || it.isDiag() }
        val coveredPoints = lines.map { it.coveredPoints() }.flatten()
        val multCoveredPoints = coveredPoints.groupingBy { it }.eachCount().filter { it.value > 1 }.map { it.key }
        return multCoveredPoints.size
    }

    val testInput = readInput("Day05_test")
    check(part1(testInput) == 5)
    check(part2(testInput) == 12)

    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}