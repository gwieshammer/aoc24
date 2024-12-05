
fun main() {
    fun processInput(input: List<String>,
                     pagesAfter: MutableMap<Int, MutableList<Int>>,
                     pagesBefore: MutableMap<Int, MutableList<Int>>,
                     updatePagesList: MutableList<List<Int>>
    ) {
        var readingPageRelations = true
        for (line in input) {
            if (line.isEmpty()) {
                readingPageRelations = false
                continue
            }
            if (readingPageRelations) {
                val (before, after) = line.split("|")
                pagesAfter.getOrPut(before.toInt()) { mutableListOf() }.add(after.toInt())
                pagesBefore.getOrPut(after.toInt()) { mutableListOf() }.add(before.toInt())
            } else {
                // second block are planned updates of pages
                val upd = line.split(",").map { it.trim().toInt() }
                updatePagesList.add(upd)
            }
        }
    }
    fun part1(input: List<String>): Int {
        val pagesAfter = mutableMapOf<Int, MutableList<Int>>()
        val pagesBefore = mutableMapOf<Int, MutableList<Int>>()
        val updatePagesList = mutableListOf<List<Int>>()
        processInput(input, pagesAfter, pagesBefore, updatePagesList)
        val chkFun = fun(updatePages: List<Int>) : Int {
            for ((idx, page) in updatePages.withIndex()) {
                for (otherPage in updatePages.subList(idx + 1, updatePages.size)) {
                    if (pagesBefore[page]?.contains(otherPage) == true
                        || pagesAfter[otherPage]?.contains(page) == true) {
                        return 0
                    }
                }
            }
            return updatePages[(updatePages.size - 1) / 2]
        }
        var s = 0
        for (updatePages in updatePagesList) {
            s = s + chkFun(updatePages)
        }
        return s
    }

    fun part2(input: List<String>): Int {
        val pagesAfter = mutableMapOf<Int, MutableList<Int>>()
        val pagesBefore = mutableMapOf<Int, MutableList<Int>>()
        val updatePagesList = mutableListOf<List<Int>>()
        processInput(input, pagesAfter, pagesBefore, updatePagesList)
        val getIndexOfFirstPageToProduce = fun(pages: List<Int>) : Int {
            outerLoop@ for ((idx, page) in pages.withIndex()) {
                for (otherPage in pages.subList(idx + 1, pages.size)) {
                    if (pagesBefore[page]?.contains(otherPage) == true
                        || pagesAfter[otherPage]?.contains(page) == true) {
                        continue@outerLoop
                    }
                }
                return idx
            }
            return -5
        }
        var s = 0
        fun correctTheOrder(pages: List<Int>) : List<Int> {
            if (pages.size == 1) {
                return pages
            }
            val firstIdx = getIndexOfFirstPageToProduce(pages)
            val firstPage = pages[firstIdx]
            val remainingPages = pages.toMutableList()
            remainingPages.removeAt(firstIdx)
            val orderedPages = mutableListOf(firstPage)
            orderedPages.addAll(correctTheOrder(remainingPages))
            return orderedPages
        }
        val chkFun = fun(updatePages: List<Int>) : Int {
            for ((idx, page) in updatePages.withIndex()) {
                for ((otherIdx, otherPage) in updatePages.subList(idx + 1, updatePages.size).withIndex()) {
                    if (pagesBefore[page]?.contains(otherPage) == true
                        || pagesAfter[otherPage]?.contains(page) == true) {
                        // correct the order and return middle number
                        val correctOrderedPages = correctTheOrder(updatePages)
                        return correctOrderedPages[(correctOrderedPages.size - 1) / 2]
                    }
                }
            }
            return 0
        }
        for (updatePages in updatePagesList) {
            s = s + chkFun(updatePages)
        }
        return s
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 143)
    check(part2(testInput) == 123)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}