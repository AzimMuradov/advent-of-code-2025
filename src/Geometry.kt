@JvmInline
value class Pos private constructor(private val packedValue: Long) {

    constructor(x: Int, y: Int) : this((x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFF))


    val x: Int get() = (packedValue shr 32).toInt()

    val y: Int get() = (packedValue and 0xFFFFFFFF).toInt()


    operator fun component1(): Int = x

    operator fun component2(): Int = y


    fun copy(x: Int = this.x, y: Int = this.y): Pos = Pos(x, y)


    override fun toString(): String = "(x=$x, y=$y)"


    companion object {

        val ZERO: Pos = Pos(x = 0, y = 0)
    }
}

@JvmInline
value class Vec private constructor(private val packedValue: Long) {

    constructor(x: Int, y: Int) : this((x.toLong() shl 32) or (y.toLong() and 0xFFFFFFFF))


    val x: Int get() = (packedValue shr 32).toInt()

    val y: Int get() = (packedValue and 0xFFFFFFFF).toInt()


    operator fun component1(): Int = x

    operator fun component2(): Int = y


    fun copy(x: Int = this.x, y: Int = this.y): Vec = Vec(x, y)


    override fun toString(): String = "(x=$x, y=$y)"


    companion object {

        val ZERO: Vec = Vec(x = 0, y = 0)

        val MOVE_UP: Vec = Vec(x = 0, y = -1)
        val MOVE_DOWN: Vec = Vec(x = 0, y = 1)
        val MOVE_RIGHT: Vec = Vec(x = 1, y = 0)
        val MOVE_LEFT: Vec = Vec(x = -1, y = 0)

        val MOVES: List<Vec> = listOf(MOVE_UP, MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT)
    }
}

@JvmInline
value class Rect private constructor(private val packedValue: Long) {

    constructor(w: Int, h: Int) : this((w.toLong() shl 32) or (h.toLong() and 0xFFFFFFFF))


    val w: Int get() = (packedValue shr 32).toInt()

    val h: Int get() = (packedValue and 0xFFFFFFFF).toInt()


    operator fun component1(): Int = w

    operator fun component2(): Int = h


    fun copy(w: Int = this.w, h: Int = this.h): Rect = Rect(w, h)


    override fun toString(): String = "(w=$w, h=$h)"


    companion object {

        val ZERO: Rect = Rect(w = 0, h = 0)

        fun from(map: List<String>) = Rect(map[0].length, map.size)
    }
}


// Operators

operator fun Pos.plus(vec: Vec) = Pos(x + vec.x, y + vec.y)

operator fun Pos.minus(vec: Vec) = Pos(x - vec.x, y - vec.y)


operator fun Pos.minus(other: Pos) = Vec(x - other.x, y - other.y)

operator fun Int.times(vec: Vec) = Vec(this * vec.x, this * vec.y)

operator fun Rect.contains(pos: Pos) = pos.x in 0..<w && pos.y in 0..<h


// Pair interop

fun Pos.toPair(): Pair<Int, Int> = x to y

fun Pair<Int, Int>.toPos(): Pos = Pos(first, second)


fun Vec.toPair(): Pair<Int, Int> = x to y

fun Pair<Int, Int>.toVec(): Vec = Vec(first, second)


fun Rect.toPair(): Pair<Int, Int> = w to h

fun Pair<Int, Int>.toRect(): Rect = Rect(first, second)


// Other

fun positionsList(a: Pos, b: Pos): List<Pos> {
    val (fromX, toX) = if (a.x < b.x) a.x to b.x else b.x to a.x
    val (fromY, toY) = if (a.y < b.y) a.y to b.y else b.y to a.y

    return (fromX..toX).flatMap { x -> (fromY..toY).map { y -> Pos(x, y) } }
}

fun positionsSeq(a: Pos, b: Pos): Sequence<Pos> = sequence {
    val (fromX, toX) = if (a.x < b.x) a.x to b.x else b.x to a.x
    val (fromY, toY) = if (a.y < b.y) a.y to b.y else b.y to a.y

    for (x in fromX..toX) {
        for (y in fromY..toY) {
            yield(Pos(x, y))
        }
    }
}
