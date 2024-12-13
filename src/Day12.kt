val directions = listOf(
    Coordinate(0, 1),
    Coordinate(0, -1),
    Coordinate(1, 0),
    Coordinate(-1, 0)
)
fun findContinuousRegions(map: Map<Coordinate, Char>): List<List<Coordinate>> {
    val visited = mutableSetOf<Coordinate>()
    val regions = mutableListOf<List<Coordinate>>()
    fun dfs(start: Coordinate, char: Char): List<Coordinate> {
        val dfsStack = ArrayDeque<Coordinate>()
        val currentRegion = mutableListOf<Coordinate>()
        dfsStack.add(start)
        visited.add(start)
        while (dfsStack.isNotEmpty()) {
            val current = dfsStack.removeLast()
            currentRegion.add(current)
            for (direction in directions) {
                val neighbor = Coordinate(current.x + direction.x, current.y + direction.y)
                if (neighbor !in visited && map[neighbor] == char) {
                    dfsStack.add(neighbor)
                    visited.add(neighbor)
                }
            }
        }
        return currentRegion
    }
    for ((coordinate, char) in map) {
        if (coordinate !in visited) {
            val region = dfs(coordinate, char)
            regions.add(region)
        }
    }

    return regions
}
fun getPerimeterLength(map: Map<Coordinate, Char>, region: List<Coordinate>) : Int {
    var len = 0
    var regionName = map[region[0]]!!
    for (coord in region) {
        for (direction in directions) {
            val nextCoord = Coordinate(coord.x + direction.x, coord.y + direction.y)
            if (!map.containsKey(nextCoord) || map[nextCoord]!! != regionName) {
                len ++
            }
        }
    }
    return len
}
fun getNumberSides(map: Map<Coordinate, Char>, region: List<Coordinate>) : Int {
    var fencesAt = mutableSetOf<Pair<Coordinate, Coordinate>>()
    var regionName = map[region[0]]!!
    for (coord in region) {
        for (direction in directions) {
            val nextCoord = Coordinate(coord.x + direction.x, coord.y + direction.y)
            if (!map.containsKey(nextCoord) || map[nextCoord]!! != regionName) {
                fencesAt.add(coord to direction)
            }
        }
    }
    var sides = 0
    while (fencesAt.isNotEmpty()) {
        var current = fencesAt.random()
        fencesAt.remove(current)
        sides ++
        for (direction in directions) {
            if (
                (direction.x != 0 && current.second.x != 0)
                ||
                (direction.y != 0 && current.second.y != 0)
            ) {
                // fences to the north can only be adjacent east or west, and so on
                continue
            }
            var currentCoord = current.first
            while (map.containsKey(currentCoord) && region.contains(currentCoord)) {
                currentCoord = Coordinate(currentCoord.x + direction.x, currentCoord.y + direction.y)
                if (fencesAt.contains(currentCoord to current.second)) {
                    fencesAt.remove(currentCoord to current.second)
                } else {
                    break
                }
            }

        }
    }
    println("Region ${map[region[0]]} $sides")
    return sides
}
fun main() {
    fun part1(input: List<String>): Long {
        val map = mutableMapOf<Coordinate,Char>()
        for ((row, line) in input.withIndex()) {
            for ((col,c) in line.withIndex()) {
                val coord = Coordinate(col, row)
                map[coord] = c
            }
        }
        val regs = findContinuousRegions(map)
        var sum = 0L
        for (r in regs) {
            val perimeterLength = getPerimeterLength(map, r)
            sum = sum + perimeterLength.toLong() * r.size.toLong()
        }
        return sum
    }
    fun part2(input: List<String>): Long {
        val map = mutableMapOf<Coordinate,Char>()
        for ((row, line) in input.withIndex()) {
            for ((col,c) in line.withIndex()) {
                val coord = Coordinate(col, row)
                map[coord] = c
            }
        }
        val regs = findContinuousRegions(map)
        var sum = 0L
        for (r in regs) {
            val numberOfSides = getNumberSides(map, r)
            sum = sum + numberOfSides.toLong() * r.size.toLong()
        }
        return sum
    }
    //val testInput = readInput("Day12_test")
    //check(part1(testInput) == 1930L)
    //check(part2(testInput) == xx)

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}

