data class TreeRegion(
    val size: Rect,
    val presentCount: List<Int>,
)


fun main() {
    fun part1(presentsShapes: List<List<String>>, treeRegions: List<TreeRegion>): Int {
        val shapesArea = presentsShapes.map { shape ->
            shape.joinToString(separator = "").count { it == '#' }
        }

        val definitelyCanFit = treeRegions.filter { (size, presentCount) ->
            val minNumberOfPresentsThatWouldFit = (size.w / 3) * (size.h / 3)
            val numberOfAllPresents = presentCount.sum()
            minNumberOfPresentsThatWouldFit >= numberOfAllPresents
        }
        val definitelyCantFit = treeRegions.filter { (size, presentCount) ->
            val regionArea = size.area()
            val minPresentsArea = (presentCount zip shapesArea).sumOf { (count, area) -> count * area }
            regionArea < minPresentsArea
        }

        return if (definitelyCanFit.size + definitelyCantFit.size == treeRegions.size) {
            definitelyCanFit.size
        } else {
            error("not intended")
        }
    }

    fun part2(): Int = 23


    val (presentsShapes, treeRegions) = run {
        val parts = readInputText("day-12-input").split("\n\n")

        val (shapesStr, regionsStr) = parts.splitAt(parts.lastIndex)

        val presentsShapes = shapesStr.map { shapeStr ->
            shapeStr.lines().drop(1)
        }
        val treeRegions = regionsStr.first().lines().map { regionStr ->
            val (sizeStr, pcStr) = regionStr.split(": ")
            TreeRegion(
                size = sizeStr.toInts("x").toPair().toRect(),
                presentCount = pcStr.toInts()
            )
        }

        presentsShapes to treeRegions
    }

    part1(presentsShapes, treeRegions).println()
    part2().println()
}
