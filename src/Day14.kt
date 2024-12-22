import java.util.function.Predicate

fun main() {
    class Robot(
        val startPos: Coordinate,
        val velocity: Coordinate
    )
    fun getRobots(input: List<String>): MutableList<Robot> {
        val robots = mutableListOf<Robot>()
        for (l in input) {
            if (l.isBlank()) {
                continue
            }
            val parts = l.split(" ")
            val pParts = parts[0]!!.split("=")
            val pCoordParts = pParts[1]!!.split(",")
            val pCoord = Coordinate(pCoordParts[0].trim().toInt(), pCoordParts[1].trim().toInt())
            val vParts = parts[1]!!.split("=")
            val vCoordParts = vParts[1]!!.split(",")
            val vCoord = Coordinate(vCoordParts[0].trim().toInt(), vCoordParts[1].trim().toInt())
            robots.add(Robot(pCoord, vCoord))
        }
        return robots
    }
    fun getQuadrant(pos: Coordinate, wide: Int, tall: Int) : Int? {
        // 12
        // 34
        val centerX = (wide - 1) / 2
        val centerY = (tall - 1) / 2
        if (pos.x == centerX || pos.y == centerY) {
            return null
        }
        if (pos.x < centerX) {
            if (pos.y < centerY) {
                return 1
            } else {
                return 3
            }
        } else {
            if (pos.y < centerY) {
                return 2
            } else {
                return 4
            }
        }
    }
    fun part1(input: List<String>, wide: Int, tall: Int): Long {
        val robots = getRobots(input)
        val quadrantCounts = mutableMapOf<Int,Int>()
        var sum = 1L
        for (robot in robots) {
            val finalPos = robot.startPos.timesTeleportingInBathroom(robot.velocity, 100, wide, tall)
            val quad = getQuadrant(finalPos, wide, tall)
            if (quad != null) {
                quadrantCounts.putIfAbsent(quad, 0)
                quadrantCounts[quad] = quadrantCounts[quad]!! + 1
            }
        }
        for (count in quadrantCounts.values) {
            sum *= count.toLong()
        }
        return sum
    }
    fun part2(input: List<String>, wide: Int, tall: Int): Long {
        val robots = getRobots(input)
        for (t in 1..100000) {
            val fBots = mutableListOf<Coordinate>()
            for (robot in robots) {
                fBots.add(robot.startPos.timesTeleportingInBathroom(robot.velocity, t, wide, tall))
            }
            if (fBots.groupBy { it.x }.filter { it.value.size > 30 }.count() > 1
                && fBots.groupBy { it.y }.filter { it.value.size > 30 }.count() > 1) {
                return t.toLong()
            }
        }
        return 0L
    }
    //val testInput = readInput("Day14_test")
    //check(part1(testInput, 11, 7) == 12L)
    //check(part2(testInput) == xx)

    val input = readInput("Day14")
    //part1(input, 101, 103).println()
    part2(input, 101, 103).println()
}

