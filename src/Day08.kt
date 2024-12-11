class Lot(
    val antenna : Char?
) {

}
fun Array2D<Lot>.findAntennaTypes() : List<Char> {
    return data.mapNotNull {
        when(it) {
            is Lot -> it.antenna
            else -> null
        }
    }.distinct()
}
fun Array2D<Lot>.getAntennaCoordinates(a: Char) : List<Coordinate> {
    val coords = mutableListOf<Coordinate>()
    for ((index, lot) in data.withIndex()) {
        if (get(index).antenna == a) {
            coords.add(this.getCoord(index))
        }
    }
    return coords
}
fun <E> Iterable<E>.pairs(): Sequence<Pair<E, E>> {
    return sequence {
        this@pairs.forEachIndexed { index1, e ->
            this@pairs.drop(index1+1).forEach { e2 ->
                yield(e to e2)
            }
        }
    }
}
fun main() {
    var map : Array2D<Lot> = Array2D(listOf(listOf(Lot(null))))
    fun part1(input: List<String>): Int {
        val l = input.map { it.toList().map { a -> Lot(if (a == '.') { null } else { a } ) } }
        map = Array2D(l)
        var mapOfAntinodes = mutableSetOf<Coordinate>()
        val antennaTypes = map.findAntennaTypes()
        for (type in antennaTypes) {
            val pairs = map.getAntennaCoordinates(type)
                .pairs()
            for ((first,second) in pairs) {
                val diff = first - second
                sequenceOf(first + diff, second - diff)
                    .filter { coord -> map.contains(coord) }
                    .forEach {
                        coord -> mapOfAntinodes.add(coord)
                    }
            }
        }
        return mapOfAntinodes.size
    }
    fun getAntinodesInLine(first: Coordinate, second: Coordinate) : Sequence<Coordinate> {
        //sequenceOf(first + diff, second - diff)
        val diff = first - second
        val plusSeq = sequence<Coordinate> {
            var run = first
            do {
                yield(run)
                run += diff
            } while (1 + 1 == 2)
        }.takeWhile { map.contains(it) }
        val minusSeq = sequence<Coordinate> {
            // Walking in the other direction...
            var run = second
            do {
                yield(run)
                run -= diff
            } while (1 + 1 == 2)
        }.takeWhile { map.contains(it) }
        return plusSeq + minusSeq

    }
    fun part2(input: List<String>): Int {
        val l = input.map { it.toList().map { a -> Lot(if (a == '.') { null } else { a } ) } }
        map = Array2D(l)
        var mapOfAntinodes = mutableSetOf<Coordinate>()
        val antennaTypes = map.findAntennaTypes()
        for (type in antennaTypes) {
            val pairs = map.getAntennaCoordinates(type)
                .pairs()
            for ((first,second) in pairs) {
                getAntinodesInLine(first, second)
                    .filter { coord -> map.contains(coord) }
                    .forEach {
                            coord -> mapOfAntinodes.add(coord)
                    }
            }
        }
        return mapOfAntinodes.size
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    part2(testInput).println()
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}