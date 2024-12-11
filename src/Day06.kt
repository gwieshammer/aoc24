
fun <S,T> MutableMap<S, MutableMap<S,T>>.set(i1: S, i2: S, c: T) {
    this.getOrPut(i1) { mutableMapOf() }[i2] = c
}
fun <S,T> MutableMap<S, MutableMap<S,T>>.set(point: Pair<S,S>, c: T) {
    this.set(point.first, point.second, c)
}

fun MutableMap<Int, MutableMap<Int, Char>>.getChar(point: Pair<Int,Int>): Char {
    if (!this.containsKey(point.first)) {
        return '.'
    }
    if (!this[point.first]!!.containsKey(point.second)) {
        return '.'
    }
    return this[point.first]!![point.second]!!
}
fun <T> MutableMap<Int, MutableMap<Int, T>>.get(point: Pair<Int,Int>): T? {
    if (!this.containsKey(point.first)) {
        return null
    }
    if (!this[point.first]!!.containsKey(point.second)) {
        return null
    }
    return this[point.first]!![point.second]!!
}
val DIRS = listOf(
    Pair(-1, 0), // 0 ... ^
    Pair(0, 1),  // 1 ... >
    Pair(1, 0),  // 2 ... v
    Pair(0, -1)  // 3 ... <
)

fun Pair<Int,Int>.move(direction: Pair<Int,Int>): Pair<Int,Int> {
    return Pair(first + direction.first, second + direction.second)
}
fun Pair<Int,Int>.move(direction: Int): Pair<Int,Int> {
    return Pair(first + DIRS[direction].first, second + DIRS[direction].second)
}

class Situation(input: List<String>) {
    var countPositionsVisited = 0
    var possibleLoopOpportunities = 0
    private val m : MutableMap<Int, MutableMap<Int, Char>> = mutableMapOf()
    lateinit var startPoint : Pair<Int, Int> // row, column
    var size : Pair<Int, Int> // rows, columns
    init {
        var maxRow = 0;
        var maxColumn = 0;
        input.forEachIndexed { rowNumber, rowString ->
            rowString.forEachIndexed { columnNumber, char ->
                if (char == '^') {
                    startPoint = Pair(rowNumber, columnNumber)
                } else if (char == '#') {
                    m.set(rowNumber, columnNumber, char)
                }
                if (columnNumber >= maxColumn) {
                    maxColumn = columnNumber + 1
                }
            }
            if (rowNumber >= maxRow) {
                maxRow = rowNumber + 1
            }
        }
        size = Pair(maxRow, maxColumn)
    }
    fun inMap(point: Pair<Int,Int>): Boolean {
        if (point.first < 0 || point.first >= size.first) return false
        if (point.second < 0 || point.second >= size.second) return false
        return true
    }
    fun patrolToEndOfMap() {
        var currentPoint = startPoint
        var dir = 0
        while (inMap(currentPoint)) {
            val c = m.getChar(currentPoint)
            if (c == '#') {
                println(this)
                throw Exception("Oh no")
            }
            if (c == '.') {
                countPositionsVisited ++;
            }
            m.set(currentPoint, 'X')
            var newPoint : Pair<Int, Int>
            do {
                newPoint = currentPoint.move(dir)
                if (m.getChar(newPoint) == '#') {
                    dir = (dir + 1) % 4
                }
            } while (m.getChar(newPoint) == '#')

            currentPoint = newPoint
        }
    }
    val usedDirections : MutableMap<Int,MutableMap<Int,MutableList<Int>>> = mutableMapOf()
    val possibleLoopPositions : MutableSet<Pair<Int,Int>> = mutableSetOf()
    fun findLoopsWhilePatrolToEndOfMap() {
        var currentPoint = startPoint
        var dir = 0
        while (inMap(currentPoint)) {
            val c = m.getChar(currentPoint)
            if (c == '#') {
                throw Exception("Oh no")
            }
            m.set(currentPoint, 'X')
            var newPoint : Pair<Int, Int>
            do {
                newPoint = currentPoint.move(dir)
                if (m.getChar(newPoint) == '#') {
                    dir = (dir + 1) % 4
                }
            } while (m.getChar(newPoint) == '#')
            if (inMap(currentPoint.move(dir)) && tryMakeLoop(currentPoint, dir)) {
                possibleLoopPositions.add(currentPoint.move(dir))
                possibleLoopOpportunities ++
            }
            usedDirections.getOrPut(currentPoint.first) { mutableMapOf() }
                .getOrPut(currentPoint.second) { mutableListOf() }
                .add(dir)
            currentPoint = newPoint
        }
    }
    fun tryMakeLoop(point: Pair<Int, Int>, dir: Int, callDepth: Int = 0): Boolean {
        var runDir = (dir+1)%4
        var mayBeTurnPoint = getCoordBeforeHash(point, runDir)
        if (mayBeTurnPoint == null) {
            return false
        }
        if (usedDirections.get(mayBeTurnPoint)?.contains(runDir) == true
            || usedDirections.get(mayBeTurnPoint)?.contains((runDir+1)%4) == true
            ) {
            return true
        }
        if (callDepth < 250) {
            return tryMakeLoop(mayBeTurnPoint, runDir, callDepth+1)
        }
        return false
    }
    fun getCoordBeforeHash(point: Pair<Int, Int>, dir: Int) : Pair<Int, Int>? {
        var runPoint = point
        while (inMap(runPoint.move(dir)) && m.getChar(runPoint.move(dir))!='#') {
            runPoint=runPoint.move(dir)
        }
        if (inMap(runPoint.move(dir)) && m.getChar(runPoint.move(dir))=='#') {
            return runPoint
        }
        return null
    }
}
fun main() {
    fun part1(input: List<String>): Int {
        val map = Situation(input)
        map.patrolToEndOfMap()
        return map.countPositionsVisited
    }
    fun part2(input: List<String>): Int {
        val map = Situation(input)
        map.findLoopsWhilePatrolToEndOfMap()
        return map.possibleLoopPositions.size
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

