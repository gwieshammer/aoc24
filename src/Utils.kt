import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


@Suppress("UNCHECKED_CAST")
class Array2D<T> {

    val height: Int
    val width: Int

    val data: Array<Any?>


    constructor(input: Collection<Collection<T>>) {
        this.height = input.size
        this.width = input.first().size

        val flatInput = input.flatten()
        this.data = flatInput.toTypedArray()
    }
    fun getCoord(index: Int): Coordinate {
        return Coordinate(index % width, index / width)
    }
    fun get(index: Int): T {
        return data[index] as T
    }
    fun contains(coord: Coordinate): Boolean {
        return coord.x in 0..<width && coord.y in 0..<height
    }
}
data class Coordinate(val x: Int, val y: Int) {
    operator fun plus(other: Coordinate): Pair<Int, Int> = Pair(x + other.x, y + other.y)
    operator fun minus(other: Coordinate): Pair<Int, Int> = Pair(x - other.x, y - other.y)
    operator fun plus(diff: Pair<Int, Int>) = copy(x = x + diff.first, y = y + diff.second)
    operator fun minus(diff: Pair<Int, Int>) = copy(x = x - diff.first, y = y - diff.second)
}