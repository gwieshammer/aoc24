fun main() {
    fun evolve(stones: MutableList<Long>) : MutableList<Long> {
        var newList = mutableListOf<Long>()
        for (l in stones) {
            if (l == 0L) {
                newList.add(1L)
            } else if (l.toString().length % 2 == 0) {
                newList.add(l.toString().substring(0, l.toString().length / 2).toLong())
                newList.add(l.toString().substring(l.toString().length / 2, l.toString().length).toLong())
            } else {
                newList.add(l * 2024)
            }
        }
        return newList
    }
    fun part1(input: List<String>): Long {
        var stones : MutableList<Long> = input[0].split(" ").map { it.trim().toLong()}.toList().toMutableList()
        for (i in 1..25) {
            stones = evolve(stones)
        }
        return stones.size.toLong()
    }
    fun evolve2(stoneCounts: MutableMap<Long,Long>) : MutableMap<Long,Long> {
        var newStoneCounts : MutableMap<Long,Long> = mutableMapOf()
        for ((stone, count) in stoneCounts) {
            if (stone == 0L) {
                newStoneCounts.putIfAbsent(1L, 0L)
                newStoneCounts[1L] = newStoneCounts[1L]!! + count
            } else if (stone.toString().length % 2 == 0) {
                val left = stone.toString().substring(0, stone.toString().length / 2).toLong()
                val right = stone.toString().substring(stone.toString().length / 2, stone.toString().length).toLong()
                newStoneCounts.putIfAbsent(left, 0L)
                newStoneCounts[left] = newStoneCounts[left]!! + count
                newStoneCounts.putIfAbsent(right, 0L)
                newStoneCounts[right] = newStoneCounts[right]!! + count
            } else {
                val newStone = stone * 2024
                newStoneCounts.putIfAbsent(newStone, 0L)
                newStoneCounts[newStone] = newStoneCounts[newStone]!! + count
            }
        }
        return newStoneCounts
    }
    fun part2(input: List<String>): Long {
        var stones : MutableList<Long> = input[0].split(" ").map { it.trim().toLong()}.toList().toMutableList()
        var stonesCount : MutableMap<Long,Long> = mutableMapOf()
        for (s in stones) {
            stonesCount.putIfAbsent(s, 0L)
            stonesCount[s] = stonesCount[s]!! + 1
        }
        for (i in 1..75) {
            stonesCount = evolve2(stonesCount)
        }
        return stonesCount.values.sum()
    }
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 55312L)
    //check(part2(testInput) == xx)

    val input = readInput("Day11")
    part1(input).println()
    part2(input).println()
}
