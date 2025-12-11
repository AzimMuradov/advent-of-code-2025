private data class Rotation(val direction: Int, val distance: Int)

private val Rotation.rotation get() = direction * distance

private val Rotation.fullRotationsCount get() = distance / 100

private val Rotation.partialRotation get() = direction * (distance % 100)

private fun moveDial(position: Int, rotation: Rotation) = (position + rotation.partialRotation + 100) % 100


fun main() {
    fun part1(rotations: List<Rotation>): Int = rotations
        .runningFold(initial = 50, ::moveDial)
        .count { it == 0 }

    fun part2(rotations: List<Rotation>): Int {
        var zeroCount = 0
        var dialPosition = 50
        for (rotation in rotations) {
            zeroCount += rotation.fullRotationsCount
            if (dialPosition != 0 && dialPosition + rotation.partialRotation !in 1..99) {
                zeroCount += 1
            }
            dialPosition = moveDial(dialPosition, rotation)
        }
        return zeroCount
    }


    val rotations = readInputLines("day-01-input").map { line ->
        val (dirString, distString) = line.splitAt(1)
        Rotation(
            direction = if (dirString == "L") -1 else 1,
            distance = distString.toInt()
        )
    }

    part1(rotations).println()
    part2(rotations).println()
}
