import java.util.regex.Pattern
import kotlin.math.abs

enum class DIRECTION{
    UP,
    NONE,
    DOWN;
    companion object {
        fun getDirection(num1: Long, num2: Long) : DIRECTION {
            if (num2 > num1) {
                return UP
            }
            if (num2 < num1) {
                return DOWN
            }
            return NONE
        }
    }
}
class Report(
    val levels: List<Long>
) {
    fun isSafe() : Boolean {
        var direction: DIRECTION = DIRECTION.NONE
        var lastLevel: Long? = null
        for (level in levels) {
            if (lastLevel == null) {
                lastLevel = level
                continue
            }
            if (abs(level - lastLevel) > 3 || level == lastLevel) {
                return false
            }
            var curDir = DIRECTION.getDirection(lastLevel!!, level)
            lastLevel = level
            if (curDir == DIRECTION.NONE) {
                return false;
            }
            if (direction == DIRECTION.NONE) {
                direction = curDir
                continue
            }
            if (curDir != direction) {
                return false
            }
        }
        return true
    }
    fun isBadDampenerSafe(): Boolean {
        if (isSafe()) {
            return true
        }
        // mutate over omitting one value
        for (i in 0..levels.size - 1) {
            var other = levels.filterIndexed { index, l -> index != i }
            if (Report(other).isSafe()) {
                return true
            }
        }
        return false
    }
}

fun main() {
    val spacePattern = Pattern.compile("\\s+")
    fun part1(input: List<String>): Long {
        var count = 0L
        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            val parts = line.split(spacePattern)
                .map { it.toLong() }
            val report = Report(parts)
            if (report.isSafe()) {
                count = count + 1
            }
        }
        return count
    }
    fun part2(input: List<String>): Long {
        var count = 0L
        for (line in input) {
            if (line.isEmpty()) {
                continue
            }
            val parts = line.split(spacePattern)
                .map { it.toLong() }
            val report = Report(parts)
            if (report.isBadDampenerSafe()) {
                count = count + 1
            }
        }
        return count
    }
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 2L)
    check(part2(testInput) == 4L)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()

}