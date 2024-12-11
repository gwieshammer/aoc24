enum class KindEnum {
    FREE,
    FILE
}
fun main() {
    fun part1(input: List<String>): Long {
        val mapFiles = input[0]
        val usedSpace = mapFiles.withIndex().filter { it -> it.index % 2 == 0 }
            .map { it.value.digitToInt() }
            .sum()
        val freeSpace = mapFiles.withIndex().filter { it -> it.index % 2 == 1 }
            .map { it.value.digitToInt() }
            .sum()
        var forwardRunIdx = 0
        var sequenceFileIndicesForward = sequence<Int> {
            var fileIndex = 0
            while (forwardRunIdx < mapFiles.length - 1) {
                var fileSize = mapFiles[forwardRunIdx].digitToInt()
                while(fileSize > 0) {
                    yield(fileIndex)
                    fileSize --
                }
                fileIndex++
                forwardRunIdx += 2
            }
        }.iterator()
        var backwardsRunIdx = mapFiles.length - 1
        var backwardsBlockIdx = usedSpace + freeSpace - 1
        var sequenceFileIndicesBackwards = sequence<Int> {
            var reverseFileIndex = (mapFiles.length - 1 ) / 2

            while (backwardsRunIdx > 0) {
                var fileSize = mapFiles[backwardsRunIdx].digitToInt()
                while(fileSize > 0) {
                    yield(reverseFileIndex)
                    fileSize --
                    backwardsBlockIdx --
                }
                reverseFileIndex--
                backwardsBlockIdx -= mapFiles[backwardsRunIdx-1].digitToInt()
                backwardsRunIdx -= 2
            }
        }.iterator()
        var checkSumOperandsSeq = sequence<Pair<Int,Int>> {
            var idx = 0
            var blockIdx = 0
            var relocatedCount = 0
            sumOp@ while (sequenceFileIndicesForward.hasNext() || sequenceFileIndicesBackwards.hasNext()) {
                if (idx % 2 == 0) {
                    var size = mapFiles[idx].digitToInt()
                    for (i in 0 until size) {
                        yield(Pair(blockIdx,sequenceFileIndicesForward.next()))
                        blockIdx ++
                        if (blockIdx + relocatedCount > usedSpace + freeSpace - 1) {
                            break@sumOp;
                        }
                    }
                } else {
                    var size = mapFiles[idx].digitToInt()
                    for (i in 0 until size) {
                        yield(Pair(blockIdx,sequenceFileIndicesBackwards.next()))
                        relocatedCount ++;
                        blockIdx ++
                        if (blockIdx + relocatedCount > usedSpace + freeSpace - 1) {
                            break@sumOp;
                        }
                    }
                }
                idx ++
            }
        }.takeWhile { sequenceFileIndicesForward.hasNext() || sequenceFileIndicesBackwards.hasNext() }
        var s = 0L
        for (r in checkSumOperandsSeq) {
            if (r.first == backwardsBlockIdx) {
                break;
            }
            s = s + r.first * r.second
        }
        return s
    }

    fun initializeDiskStorage(fileDescriptors: List<Int>): Pair<ArrayList<Int>, Int> {
        val diskBlocks = ArrayList<Int>()
        var totalDiskSize = 0
        for (i in fileDescriptors.indices) {
            repeat(fileDescriptors[i]) {
                diskBlocks.add(if (i % 2 == 0) i / 2 else -1)
            }
            totalDiskSize += fileDescriptors[i]
        }
        return Pair(diskBlocks, totalDiskSize)
    }
    fun findFirstEmptyBlock(diskBlocks: List<Int>, totalDiskSize: Int): Int {
        for (i in 0 until totalDiskSize) {
            if (diskBlocks[i] == -1) return i
        }
        return -1
    }

    fun part2(input: List<String>): Long {
        val fileDescriptors = input.first().map { it.digitToInt() }
        var (diskBlocks, totalDiskSize) = initializeDiskStorage(fileDescriptors)
        var currentFileId = fileDescriptors.size / 2 + 1
        var firstEmptyDiskBlockIndex = findFirstEmptyBlock(diskBlocks, totalDiskSize)
        var runBackwardsIndex = totalDiskSize - 1
        for (runFileId in currentFileId - 1 downTo 0) {
            while (diskBlocks[runBackwardsIndex] != runFileId) {
                runBackwardsIndex --
            }
            if (firstEmptyDiskBlockIndex > runBackwardsIndex) {
                break
            }
            var availableCount = 0
            var availableSpaceStartIndex = -1
            val currentFileSize = fileDescriptors[2 * runFileId]
            for (searchEmptyIdx in firstEmptyDiskBlockIndex..<totalDiskSize) {
                if (searchEmptyIdx >= runBackwardsIndex) {
                    break
                }
                if (diskBlocks[searchEmptyIdx] == -1) {
                    availableCount ++
                } else {
                    if (availableCount >= currentFileSize) {
                        availableSpaceStartIndex = searchEmptyIdx - availableCount
                        break
                    }
                    availableCount = 0
                }
            }
            if (availableSpaceStartIndex != -1) {
                while (diskBlocks[runBackwardsIndex] == runFileId) {
                    diskBlocks[runBackwardsIndex] = -1
                    runBackwardsIndex --
                }
                for (j in availableSpaceStartIndex..<availableSpaceStartIndex+ currentFileSize) {
                    diskBlocks[j] = runFileId
                }
                firstEmptyDiskBlockIndex = findFirstEmptyBlock(diskBlocks, totalDiskSize)
            }
        }
        var sum = 0L
        for (i in 0..<totalDiskSize){
            if (diskBlocks[i] != -1) {
                sum += diskBlocks[i].toLong() * i.toLong()
            }
        }
        return sum
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
