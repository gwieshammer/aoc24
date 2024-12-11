fun MutableMap<Int, MutableMap<Int,Int>>.get(row: Int, col: Int) : Int =
    this.getOrDefault(row, mutableMapOf()).getOrDefault(col, -1)
fun MutableMap<Int, MutableMap<Int,Int>>.get(c: Coordinate) : Int =
    this.get(c.y, c.x)
fun main() {
    val dir = listOf(Pair(0,1), Pair(1,0), Pair(0,-1), Pair(-1,0))
    fun inMap(map: MutableMap<Int, MutableMap<Int,Int>>, c: Coordinate) : Boolean {
        if (c.x >= 0 && c.x < map[0]!!.size && c.y >= 0 && c.y < map.size) {
            return true
        }
        return false
    }
    fun getScore(map: MutableMap<Int, MutableMap<Int,Int>>, c: Coordinate) : List<Coordinate> {
        val cur = map.get(c)
        if (cur == 9) {
            return listOf(c)
        }
        var peaks = mutableSetOf<Coordinate>()
        for (d in 0..3) {
            var newCoord = c + dir[d]
            if (inMap(map, newCoord) && map.get(newCoord) == cur + 1) {
                val p = getScore(map, newCoord)
                for (p2 in p) {
                    peaks.add(p2)
                }
            }
        }
        return peaks.toList()
    }
    fun part1(input: List<String>): Int {
        val map = mutableMapOf<Int, MutableMap<Int,Int>>()
        for (r in input.withIndex()) {
            val row = mutableMapOf<Int,Int>()
            for (c in r.value.withIndex()) {
                row.put(c.index, c.value.digitToInt())
            }
            map.put(r.index, row)
        }
        // find all zero start positions
        val zeroStarts : MutableList<Coordinate> = mutableListOf()
        for ((row, rowMap) in map) {
            for ((col, value) in rowMap) {
                if (value == 0) {
                    zeroStarts.add(Coordinate(col, row))
                }
            }
        }
        var allPeaks = 0
        for (c in zeroStarts) {
            val peaks = getScore(map, c)
            allPeaks += peaks.size
        }
        return allPeaks
    }
    fun getCountTrails(map: MutableMap<Int, MutableMap<Int,Int>>, start: Coordinate, end: Coordinate) : Int {
        val cur = map.get(start)
        if (cur == 9) {
            if (start == end) {
                return 1
            }
            return 0
        }
        var count = 0
        for (d in 0..3) {
            var newCoord = start + dir[d]
            if (inMap(map, newCoord) && map.get(newCoord) == cur + 1) {
                count = count + getCountTrails(map, newCoord, end)
            }
        }
        return count
    }
    fun part2(input: List<String>): Int {
        val map = mutableMapOf<Int, MutableMap<Int,Int>>()
        for (r in input.withIndex()) {
            val row = mutableMapOf<Int,Int>()
            for (c in r.value.withIndex()) {
                row.put(c.index, c.value.digitToInt())
            }
            map.put(r.index, row)
        }
        // find all zero start positions
        val zeroStarts : MutableList<Coordinate> = mutableListOf()
        for ((row, rowMap) in map) {
            for ((col, value) in rowMap) {
                if (value == 0) {
                    zeroStarts.add(Coordinate(col, row))
                }
            }
        }
        var countTrails = 0
        var allPeaks = 0
        for (c in zeroStarts) {
            val peaks = getScore(map, c)
            for (p in peaks) {
                countTrails = countTrails + getCountTrails(map, c, p)
            }
        }
        return countTrails
    }
    val testInput = readInput("Day10_test")
    part1(testInput).println()
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
