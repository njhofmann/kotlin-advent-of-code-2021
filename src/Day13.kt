import java.util.Collections.max

fun main() {

    fun readPoints(lines: List<String>): Set<Pair<Int, Int>> {
        return lines.map { line ->
            val intLine = line.split(",").filter { it.isNotEmpty() }.map { it.toInt() }
            Pair(intLine[0], intLine[1])
        }.toSet()
    }

    fun readFolds(lines: List<String>): List<Pair<String, Int>> {
        return lines.map {
            val rawFold = it.split(" ")[2].split("=")
            Pair(rawFold[0], rawFold[1].toInt())
        }
    }

    fun List<String>.splitFirstNewLine(): Int {
        this.forEachIndexed { idx, str -> if (str.isEmpty()) return idx }
        return -1
    }

    fun readPointsAndFolds(lines: List<String>): Pair<Set<Pair<Int, Int>>, List<Pair<String, Int>>> {
        val splitIdx = lines.splitFirstNewLine()
        val rawPoints = lines.subList(0, splitIdx)
        val rawFolds = lines.subList(splitIdx + 1, lines.size)
        return Pair(readPoints(rawPoints), readFolds(rawFolds))
    }

    fun executeFold(points: Set<Pair<Int, Int>>, fold: Pair<String, Int>): Set<Pair<Int, Int>> {
        val (axis, foldLoc) = fold
        return points.map {
            when (axis) {
                "x" -> {
                    if (it.first < foldLoc)
                        it
                    else
                        Pair((2 * foldLoc) - it.first, it.second)
                }
                else -> {
                    if (it.second < foldLoc)
                        it
                    else
                        Pair(it.first, (2 * foldLoc) - it.second)
                }
            }
        }.toSet()
    }

    fun executeFolds(
        points: Set<Pair<Int, Int>>,
        folds: List<Pair<String, Int>>,
        n: Int = -1): Set<Pair<Int, Int>> {
        val numFolds = if (n < 1) folds.size else n
        return folds.subList(0, numFolds).fold(points) { acc, fold -> executeFold(acc, fold) }
    }

    fun visualizePoint(points: Set<Pair<Int, Int>>, maxX: Int, maxY: Int) {
        (0..maxY).map { y -> println((0..maxX).map { x -> if (points.contains(Pair(x, y))) "#" else "." }) }
    }

    fun part1(lines: List<String>): Int {
        val (points, folds) = readPointsAndFolds(lines)
        val foldedPoints = executeFolds(points, folds, 1)
        return foldedPoints.size
    }

    fun part2(lines: List<String>) {
        val (points, folds) = readPointsAndFolds(lines)
        val foldedPoints = executeFolds(points, folds)
        val x = max(foldedPoints.map { it.first })
        val y = max(foldedPoints.map { it.second })
        visualizePoint(foldedPoints, x, y)
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 17)
    part2(testInput)

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}