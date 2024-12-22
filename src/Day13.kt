import kotlin.math.ceil
import kotlin.math.min
import kotlin.math.round

class Problem(
    val buttonA: Coordinate,
    val buttonB: Coordinate,
    val target: Coordinate)
fun String.extractInt() : Int {
    return this.replace(Regex("^[XY+=]+"), "").toInt()
}
fun main() {
    fun getProblems(input: List<String>): MutableList<Problem> {
        val problems = mutableListOf<Problem>()
        var buttonA: Coordinate? = null
        var buttonB: Coordinate? = null
        var target: Coordinate
        for (l in input) {
            if (l.isBlank()) {
                continue
            }
            if (l.startsWith("Button A")) {
                val parts = l.split(": ")
                val buttonAParts = parts[1].split(",")
                buttonA = Coordinate(buttonAParts[0].trim().extractInt(), buttonAParts[1].trim().extractInt())
            }
            if (l.startsWith("Button B")) {
                val parts = l.split(": ")
                val buttonBParts = parts[1].split(",")
                buttonB = Coordinate(buttonBParts[0].trim().extractInt(), buttonBParts[1].trim().extractInt())
            }
            if (l.startsWith("Prize")) {
                val parts = l.split(": ")
                val targetParts = parts[1].split(",")
                target = Coordinate(targetParts[0].trim().extractInt(), targetParts[1].trim().extractInt())
                problems.add(
                    Problem(buttonA!!, buttonB!!, target)
                )
            }
        }
        return problems
    }

    fun part1(input: List<String>): Long {
        val problems = getProblems(input)
        var sum = 0L
        for (problem in problems) {
            var maxTry = maxOf(
                ceil((problem.target.x / problem.buttonA.x).toDouble()),
                ceil((problem.target.x / problem.buttonB.x).toDouble()),
                ceil((problem.target.y / problem.buttonA.y).toDouble()),
                ceil((problem.target.y / problem.buttonB.y).toDouble()),
            ).toInt() + 1
            var max100 = min(maxTry,100)
            for (i in 0..max100) {
                val ax = problem.buttonA.x * i
                val ay = problem.buttonA.y * i
                var bTries = (problem.target.x - ax) / problem.buttonB.x
                if (bTries >= 0) {
                    if (ax + problem.buttonB.x * bTries == problem.target.x && ay + problem.buttonB.y * bTries == problem.target.y) {
                        sum += 3L * i + bTries
                    }
                }
            }
        }
        return sum
    }
    fun part2(input: List<String>): Long {
        val problems = getProblems(input)
        var sum = 0L
        for (problem in problems) {
            val ax = problem.buttonA.x.toLong()
            val ay = problem.buttonA.y.toLong()
            val bx = problem.buttonB.x.toLong()
            val by = problem.buttonB.y.toLong()
            val tx = problem.target.x.toLong() + 10000000000000L
            val ty = problem.target.y.toLong() + 10000000000000L
            val det = ax * by - bx * ay
            if (det != 0L) {
                val m = round(by * tx / det.toDouble() - bx * ty / det.toDouble()).toLong()
                val n = round(-ay * tx / det.toDouble() + ax * ty / det.toDouble()).toLong()
                if (m * ax + n * bx == tx
                    && m * ay + n * by == ty
                ) {
                    sum += (m * 3) + n
                }
            }
        }
        return sum
    }
    //val testInput = readInput("Day13_test")
    //check(part1(testInput) == 480L)
    //check(part2(testInput) == xx)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

