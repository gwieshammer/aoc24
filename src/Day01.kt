import java.util.regex.Pattern
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        val leftList = mutableListOf<Long>()
        val rightList = mutableListOf<Long>()
        for (line in input) {
            val parts = line.split(Pattern.compile("\\s+"), 2)
                .map { it.toLong() }
            leftList.add(parts[0])
            rightList.add(parts[1])
        }
        // zip the sorted location lists into pairs, then sum up distances
        return leftList.sorted()
            .zip(rightList.sorted())
            .sumOf { (a, b) -> abs(a - b) }
    }

    fun part2(input: List<String>): Long {
        val leftList = mutableListOf<Long>()
        val countOccurrences = mutableMapOf<Long, Long>()
        for (line in input) {
            val parts = line.split(Pattern.compile("\\s+"), 2)
                .map { it.toLong() }
            leftList.add(parts[0])
            val num = parts[1]
            countOccurrences.putIfAbsent(num, 0L)
            countOccurrences[num] = countOccurrences[num]!! + 1
        }
        // sum up elements of left list multiplied count occurences in right list
        return leftList.sumOf { it * countOccurrences.getOrDefault(it, 0L) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 11L)
    check(part2(testInput) == 31L)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}