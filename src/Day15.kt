fun main() {
    val directions = mutableMapOf(
        Pair('^', Coordinate(0, -1)),
        Pair('v', Coordinate(0, 1)),
        Pair('<', Coordinate(-1, 0)),
        Pair('>', Coordinate(1, 0)),
    )
    fun MutableSet<Coordinate>.contains2(coordinate: Coordinate) : Boolean {
        if (this.contains(coordinate) || this.contains(Coordinate(coordinate.x - 1, coordinate.y))) {
            return true
        }
        return false
    }
    fun MutableSet<Coordinate>.getCoordinates2(coordinate: Coordinate) : List<Coordinate> {
        if (this.contains(coordinate)) {
            return listOf(coordinate, Coordinate(coordinate.x + 1, coordinate.y))
        }
        if (this.contains(Coordinate(coordinate.x - 1, coordinate.y))) {
            return listOf(Coordinate(coordinate.x - 1, coordinate.y), coordinate)
        }
        return listOf()
    }
    class Warehouse(
        val boxes : MutableSet<Coordinate>,
        val walls : MutableSet<Coordinate>,
        val size: Pair<Int,Int>,
        val startPos : Coordinate
    ) {
        fun canMove(from: Coordinate, direction: Coordinate): Boolean {
            val testPos = from + direction
            if (walls.contains(testPos)) {
                return false
            }
            if (!boxes.contains(testPos)) {
                return true
            }
            if (canMove(testPos, direction)) {
                return true
            }
            return false
        }
        fun canMove2(from: Coordinate, direction: Coordinate): Boolean {
            val testPos = from + direction
            if (walls.contains(testPos)) {
                return false
            }
            val cs = boxes.getCoordinates2(testPos)
            if (cs.isEmpty()) {
                return true
            }
            if (direction.x == 1) {
                if (boxes.contains(testPos)) {
                    if (canMove2(testPos + direction, direction)) {
                        return true
                    }
                }
                return false
            }
            if (direction.x == -1) {
                if (boxes.contains(testPos + direction)) {
                    if (canMove2(testPos + direction, direction)) {
                        return true
                    }
                }
                return false
            }
            if (direction.x == 0) {
                var canAll = true
                if (!canMove2(cs[0], direction)) {
                    canAll = false
                }
                if (!canMove2(cs[1], direction)) {
                    canAll = false
                }
                return canAll
            }
            return false
        }
        fun move(from: Coordinate, direction: Coordinate) : Coordinate {
            val testPos = from + direction
            if (boxes.contains(testPos)) {
                move(testPos, direction)
            }
            if (boxes.contains(from)) {
                boxes.remove(from)
                boxes.add(testPos)
            }
            return testPos
        }
        fun move2(from: Coordinate, direction: Coordinate) : Coordinate {
            val testPos = from + direction
            val cs = boxes.getCoordinates2(testPos)
            if (!cs.isEmpty()) {
                if (direction.x == 1) {
                    if (boxes.contains(testPos + direction)) {
                        move2(testPos + direction, direction)
                    }
                    if (boxes.contains(testPos)) {
                        move2(testPos, direction)
                    }
                    if (boxes.remove(from)) {
                        boxes.add(testPos)
                    }
                    return testPos
                }
                if (direction.x == -1) {
                    if (boxes.contains(testPos + direction)) {
                        move2(testPos + direction, direction)
                    }
                    if (boxes.remove(from)) {
                        boxes.add(testPos)
                    }
                }
                if (direction.x == 0) {
                    for (i in 0..1) {
                        if (boxes.contains(cs[i])) {
                            // first push away those affected by the coordinate not actually in "boxes" to avoid
                            // conflicts with any already new positioned boxes
                            move2(cs[i].copy(x = cs[i].x + 1), direction)
                            move2(cs[i], direction)
                        }
                    }
                }
            }
            if (boxes.remove(from)) {
                boxes.add(testPos)
            }
            return testPos
        }
        fun widen() : Warehouse {
            val newBoxes = mutableSetOf<Coordinate>()
            val newWalls = mutableSetOf<Coordinate>()
            for (box in boxes) {
                newBoxes.add(Coordinate(box.x * 2, box.y))
            }
            for (wall in walls) {
                newWalls.add(Coordinate(wall.x * 2, wall.y))
                newWalls.add(Coordinate(wall.x * 2 + 1, wall.y))
            }
            return Warehouse(newBoxes, newWalls, Pair(size.first * 2, size.second), Coordinate(startPos.x * 2, startPos.y))
        }
        fun toString(start: Coordinate): String {
            val sb = StringBuilder()
            for (y in 0..size.second-1) {
                for (x in 0..size.first-1) {
                    if (walls.contains(Coordinate(x,y))) {
                        sb.append('#')
                    } else if (boxes.contains(Coordinate(x,y))) {
                        sb.append('O')
                    } else if (x == start.x && y == start.y) {
                        sb.append('@')
                    } else {
                        sb.append('.')
                    }
                }
                sb.append('\n')
            }
            return sb.toString()
        }
        fun toString2(start: Coordinate): String {
            val sb = StringBuilder()
            for (y in 0..size.second-1) {
                for (x in 0..size.first-1) {
                    if (walls.contains(Coordinate(x,y))) {
                        sb.append('#')
                    } else if (boxes.contains(Coordinate(x,y))) {
                        sb.append('[')
                    } else if (boxes.contains(Coordinate(x-1,y))) {
                        sb.append(']')
                    } else if (x == start.x && y == start.y) {
                        sb.append('@')
                    } else {
                        sb.append('.')
                    }
                }
                sb.append('\n')
            }
            return sb.toString()
        }
    }
    fun read(input: List<String>): Pair<Warehouse,List<Coordinate>> {
        var readMap = true
        var size = Pair(0,0)
        val boxes = mutableSetOf<Coordinate>()
        val walls = mutableSetOf<Coordinate>()
        var y = 0
        var startPos : Coordinate? = null
        val path = mutableListOf<Coordinate>()
        for (l in input) {
            if (l.isEmpty()) {
                readMap = false
                continue
            }
            if (readMap) {
                size = size.copy(second = size.second + 1)
                if (size.first == 0) {
                    size = size.copy(first = l.length)
                }
                for ((i,c) in l.withIndex()) {
                    if (c == 'O') {
                        boxes.add(Coordinate(i, y))
                    }
                    if (c == '#') {
                        walls.add(Coordinate(i, y))
                    }
                    if (c == '@') {
                        startPos = Coordinate(i, y)
                    }
                }
                y++
            } else {
                for (d in l) {
                    path.add(directions.filter { it.key == d }.values.first())
                }
            }
        }
        return Pair(Warehouse(boxes, walls, size, startPos!!), path)
    }
    fun part1(input: List<String>): Long {
        val (warehouse, path) = read(input)
        var runPos = warehouse.startPos
        for (d in path) {
            if (warehouse.canMove(runPos, d)) {
                runPos = warehouse.move(runPos, d)
            }
        }

        var sum = 0L
        for (y in 0..warehouse.size.second-1) {
            for (x in 0..warehouse.size.first-1) {
                if (warehouse.boxes.contains(Coordinate(x,y))) {
                    sum += ( x + y * 100)
                }
            }
        }
        return sum
    }
    fun part2(input: List<String>): Long {
        val (warehouse, path) = read(input)
        val warehouse2 = warehouse.widen()
        //println(warehouse2.toString2(warehouse2.startPos))
        var runPos = warehouse2.startPos
        for (d in path) {
            if (warehouse2.canMove2(runPos, d)) {
                runPos = warehouse2.move2(runPos, d)
            }
            //println(warehouse2.toString2(runPos))
        }
        var sum = 0L
        for (y in 0..warehouse2.size.second-1) {
            for (x in 0..warehouse2.size.first-1) {
                if (warehouse2.boxes.contains(Coordinate(x,y))) {
                    sum += ( x + y * 100)
                }
            }
        }
        return sum
    }
    val testInput = readInput("Day15_test2")
    //check(part1(testInput) == 10092L)
    //check(part2(testInput) == 9021L)

    val input = readInput("Day15")
    part1(input).println()
    part2(input).println()
}