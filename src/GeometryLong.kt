data class PosLong(val x: Long, val y: Long) {

    override fun toString(): String = "(x=$x, y=$y)"


    companion object {

        val ZERO: PosLong = PosLong(x = 0, y = 0)
    }
}

data class VecLong(val x: Long, val y: Long) {

    override fun toString(): String = "(x=$x, y=$y)"


    companion object {

        val ZERO: VecLong = VecLong(x = 0, y = 0)

        val MOVE_UP: VecLong = VecLong(x = 0, y = -1)
        val MOVE_DOWN: VecLong = VecLong(x = 0, y = 1)
        val MOVE_RIGHT: VecLong = VecLong(x = 1, y = 0)
        val MOVE_LEFT: VecLong = VecLong(x = -1, y = 0)

        val MOVES: List<VecLong> = listOf(MOVE_UP, MOVE_DOWN, MOVE_RIGHT, MOVE_LEFT)
    }
}


// Operators

operator fun PosLong.plus(vec: VecLong) = PosLong(x + vec.x, y + vec.y)

operator fun PosLong.minus(vec: VecLong) = PosLong(x - vec.x, y - vec.y)


operator fun PosLong.minus(other: PosLong) = VecLong(x - other.x, y - other.y)

operator fun Long.times(vec: VecLong) = VecLong(this * vec.x, this * vec.y)


// Pair interop

fun PosLong.toPair(): Pair<Long, Long> = x to y

fun Pair<Long, Long>.toPosLong(): PosLong = PosLong(first, second)


fun VecLong.toPair(): Pair<Long, Long> = x to y

fun Pair<Long, Long>.toVecLong(): VecLong = VecLong(first, second)
