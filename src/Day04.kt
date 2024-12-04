fun <S> List<S>.cartesianProductSelf() : List<Pair<S,S>> = this.flatMap { s -> List(size) { s }.zip (this) }
class SparseLetters(
    val xmasLetters: List<Char>
) {
    val letters = mutableMapOf<Int, MutableMap<Int, Char>>()
    var rows : Int = 0 // current amount of rows
    var cols : Int = 0 // ... columns
    val dirs = IntRange(-1, 1).toList().cartesianProductSelf().filter { it.first != 0 || it.second != 0 }
    fun getCountXmasStarting(row: Int, column: Int) : Int {
        var count = 0
        for (dirIndex in 0..dirs.size - 1) {
            count += getCountXmasInDirection(row, column, dirIndex, xmasLetters.subList(1, 4))
        }
        return count
    }
    fun getCountXmasInDirection(row: Int, column: Int, dirIndex: Int, letters: List<Char>) : Int {
        val nextRow = row + dirs[dirIndex].first
        val nextColumn = column + dirs[dirIndex].second
        if (get(nextRow, nextColumn) == letters[0]) {
            if (letters.size == 1) {
                return 1
            }
            return getCountXmasInDirection(nextRow, nextColumn, dirIndex, letters.subList(1, letters.size))
        }
        return 0
    }
    // Given there as an A at row, column: Check there are M and S in the diagonals (any direction)
    fun getCountXDashMasStarting(row: Int, column: Int) : Int {
        val slashLine = "".plus(get(row + 1, column - 1)).plus(get(row - 1, column + 1))
        val backslashLine = "".plus(get(row - 1, column - 1)).plus(get(row + 1, column + 1))
        return if (
            (slashLine == "MS" || slashLine == "SM")
            &&
            (backslashLine == "MS" || backslashLine == "SM")
        ) { 1 } else { 0 }

    }
    fun appendRow(line: String) {
        val newRowNumber = rows
        for ((columnNr, c) in line.toList().withIndex()) {
            if (xmasLetters.contains(c)) {
                set(newRowNumber, columnNr, c)
            }
        }
    }
    fun set(row: Int, column: Int, value: Char) {
        if (!letters.containsKey(row)) {
            letters[row] = mutableMapOf()
        }
        letters[row]!![column] = value
        rows = maxOf(rows, row + 1)
        cols = maxOf(cols, column + 1)
    }
    fun get(row: Int, column: Int) : Char {
        if (!letters.containsKey(row)) {
            return '.'
        }
        if (!letters[row]!!.containsKey(column)) {
            return '.'
        }
        return letters[row]!![column]!!
    }
    fun getCountXmas(): Int {
        var count = 0
        for (r in 0..rows - 1) {
            for (c in 0..cols - 1) {
                if (letters[r]!![c] == 'X') {
                    count += getCountXmasStarting(r, c)
                }
            }
        }
        return count
    }
    fun getCountXDashMas(): Int {
        var count = 0
        for (r in 1..rows - 2) {
            for (c in 1..cols - 2) {
                if (letters[r]!![c] == 'A') {
                    count += getCountXDashMasStarting(r, c)
                }
            }
        }
        return count
    }
}

fun main() {
    fun buildSparseLetters(input: List<String>, xmasLetters : List<Char>): SparseLetters {
        val s = SparseLetters(xmasLetters)
        for (l in input) {
            s.appendRow(l)
        }
        return s
    }

    fun part1(input: List<String>): Long {
        val xmasLetters = "XMAS".toList()
        val s = buildSparseLetters(input, xmasLetters)
        return s.getCountXmas().toLong()
    }

    fun part2(input: List<String>): Long {
        val xmasLetters = "MAS".toList()
        val s = buildSparseLetters(input, xmasLetters)
        return s.getCountXDashMas().toLong()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 18L)
    check(part2(testInput) == 9L)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}