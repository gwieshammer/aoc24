fun makeOperatorMutations(operators: List<Char>, length: Int): List<List<Char>> {
    val result = mutableListOf<List<Char>>()
    fun generate(current: List<Char>, length: Int) {
        if (current.size == length) {
            result.add(current)
            return
        }
        for (op in operators) {
            generate(current + op, length)
        }
    }
    generate(emptyList(), length)
    return result
}
class MaybeEquation(val result: Long, val operands: List<Long>) {
    fun trySolve(operators: List<Char>) : Long? {
        var opMutations = makeOperatorMutations(operators, operands.size - 1)
        for (mut in opMutations) {
            if (calc(mut) == result) {
                return result
            }
        }
        return null
    }
    fun calc(operators: List<Char>) : Long {
        var r = operands[0]
        for (i in 1 until operands.size) {
            when (operators[i-1]) {
                '+' -> r += operands[i]
                '*' -> r *= operands[i]
                '|' -> r = r.toString().plus(operands[i].toString()).toLong()
            }
        }
        return r
    }
}
class MaybeEquations(input: List<String>, val operators: List<Char>) {
    var mayBeEquations: MutableList<MaybeEquation> = mutableListOf()
    init {
        for (l in input) {
            val parts = l.split(":").map { it.trim() }
            val result = parts[0].toLong()
            val operands = mutableListOf<Long>()
            for (o in parts[1].split(" ").map { it.trim().toLong() }) {
                operands.add(o)
            }
            mayBeEquations.add(MaybeEquation(result, operands))
        }
    }
    fun trySolve() : Long {
        var s = 0L
        for (eq in mayBeEquations) {
            s += eq.trySolve(operators) ?: 0L
        }
        return s
    }
}
fun main() {
    fun part1(input: List<String>): Long {
        return MaybeEquations(input, listOf('+', '*')).trySolve()
    }
    fun part2(input: List<String>): Long {
        return MaybeEquations(input, listOf('+', '*', '|')).trySolve()
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    part1(testInput).println()
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
