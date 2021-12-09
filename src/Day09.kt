fun main() {
    fun readHeightMap(input: List<String>) : Triple<List<List<Int>>, Int, Int> {
        val heightMap = input.map { it.split("").filter { char -> char.isNotEmpty() }.map { num -> num.toInt() } }
        val rowSize = heightMap.size
        val colSize = heightMap[0].size
        return Triple(heightMap, rowSize, colSize)
    }

    fun isLowPoint(heightMap: List<List<Int>>, rowIdx: Int, colIdx: Int) : Boolean {
        val rowSize = heightMap.size
        val colSize = heightMap[0].size
        val i = heightMap[rowIdx][colIdx]
        return (colIdx == 0 || i < heightMap[rowIdx][colIdx - 1]) &&
                (colIdx == colSize - 1 || i < heightMap[rowIdx][colIdx + 1]) &&
                (rowIdx == 0 || i < heightMap[rowIdx - 1][colIdx]) &&
                (rowIdx == rowSize - 1 || i < heightMap[rowIdx + 1][colIdx])
    }

    fun part1(input: List<String>): Int {
        val (heightMap, rowSize, colSize) = readHeightMap(input)
        val lowPoints = heightMap.mapIndexed { rowIdx, row -> row.filterIndexed { colIdx, _ ->
            isLowPoint(heightMap, rowIdx, colIdx)
        } }.flatten()
        return lowPoints.sum() + lowPoints.size
    }

    fun findBasin(
        heightMap: List<List<Int>>,
        startPoint: Pair<Int, Int>,
        seen: MutableSet<Pair<Int, Int>> = LinkedHashSet()
    ): MutableSet<Pair<Int, Int>> {
        val (x, y) = startPoint
        if (x < 0 || x >= heightMap.size ||
            y < 0 || y >= heightMap[0].size ||
            heightMap[x][y] == 9 ||
            !seen.add(startPoint)
        )
            return seen

        var accSeen = seen
        accSeen = findBasin(heightMap, Pair(x + 1, y), accSeen)
        accSeen = findBasin(heightMap, Pair(x - 1, y), accSeen)
        accSeen = findBasin(heightMap, Pair(x, y + 1), accSeen)
        return findBasin(heightMap, Pair(x, y - 1), accSeen)
    }

    fun part2(input: List<String>): Int {
        val (heightMap, _, _) = readHeightMap(input)
        val lowPointPosns = heightMap.mapIndexed { rowIdx, row ->
            row.mapIndexed { colIdx, _ ->
                if (isLowPoint(heightMap, rowIdx, colIdx))
                    Pair(rowIdx, colIdx)
                else
                    Pair(-1, -1)
            }.filter { it.first > -1 }
        }.flatten()

        // assume that basins have no overlap?
        val basins = lowPointPosns.map { findBasin(heightMap, it) }.sortedBy { it.size }
        val largestBasinSizes = basins.subList(basins.size - 3, basins.size).map { it.size }
        return largestBasinSizes.fold(1) { acc, i -> acc * i }
    }

    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    println(part1(input))
    println(part2(input))
}