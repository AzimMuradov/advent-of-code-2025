import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign


fun main() {
    fun area(a: PosLong, b: PosLong): Long =
        (abs(a.x - b.x) + 1) * (abs(a.y - b.y) + 1)

    fun part1(redSquares: List<PosLong>): Long {
        val areas = sequence {
            redSquares.iterateOrderedCombinations { a, b ->
                yield(area(a, b))
            }
        }
        return areas.max()
    }

    fun part2(redSquares: List<PosLong>): Long {
        val (vertPolygonSides, horPolygonSides) = (redSquares + redSquares.first())
            .zipWithNext()
            .partition { (a, b) -> a.x == b.x }

        fun PosLong.isInsidePolygon(): Boolean {
            if (vertPolygonSides.any { (a, b) -> a.x == x && (a.y - y).sign * (b.y - y).sign <= 0 }) {
                return true
            }
            if (horPolygonSides.any { (a, b) -> a.y == y && (a.x - x).sign * (b.x - x).sign <= 0 }) {
                return true
            }

            val cnt = horPolygonSides.count { (a, b) ->
                val (minX, maxX) = min(a.x, b.x) to max(a.x, b.x)
                x in minX..<maxX && y < a.y
            }
            return cnt % 2 == 1
        }

        val areasToCorners = TreeMap<Long, Pair<PosLong, PosLong>>().apply {
            redSquares.iterateOrderedCombinations { a, b ->
                put(area(a, b), a to b)
            }
        }

        val xsToCheck: Set<Long> = run {
            val xs = redSquares.mapTo(mutableSetOf()) { it.x }
            xs.flatMapTo(mutableSetOf()) { listOf(it - 1, it + 1) } - xs
        }
        val ysToCheck: Set<Long> = run {
            val ys = redSquares.mapTo(mutableSetOf()) { it.y }
            ys.flatMapTo(mutableSetOf()) { listOf(it - 1, it + 1) } - ys
        }

        return areasToCorners.toList().last { (_, corners) ->
            val (a, b) = corners
            sequenceOf(
                positionsSeq(a, b.copy(x = a.x)).filter { it.y in ysToCheck },
                positionsSeq(a, b.copy(y = a.y)).filter { it.x in xsToCheck },
                positionsSeq(a.copy(x = b.x), b).filter { it.y in ysToCheck },
                positionsSeq(a.copy(y = b.y), b).filter { it.x in xsToCheck },
            ).flatten().all(PosLong::isInsidePolygon)
        }.first
    }


    val redSquares = readInputLines("day-09-input").map { line ->
        line.toLongs(",").toPair().toPosLong()
    }

    part1(redSquares).println()
    part2(redSquares).println()
}
