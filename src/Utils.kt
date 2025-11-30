import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText


/**
 * Reads text from the given input txt file.
 */
fun readInputText(name: String) = Path("src/$name.txt").readText().trim()

/**
 * Reads lines from the given input txt file.
 */
fun readInputLines(name: String) = readInputText(name).lines()

/**
 * Split string to ints using provided [separator].
 */
fun String.toInts(separator: String = " ") = this
    .split(separator)
    .map(String::toInt)

/**
 * Split string to longs using provided [separator].
 */
fun String.toLongs(separator: String = " ") = this
    .split(separator)
    .map(String::toLong)

/**
 * Converts list to pair.
 */
fun <T> List<T>.toPair() = Pair(this[0], this[1])

/**
 * Converts list to triple.
 */
fun <T> List<T>.toTriple() = Triple(this[0], this[1], this[2])

/**
 * Converts list to triple.
 */
fun <T> List<T>.workAsMut(block: MutableList<T>.() -> Unit): List<T> =
    toMutableList().apply(block)

/**
 * Returns the product of all elements in the collection.
 */
fun Iterable<Int>.product(): Int = fold(1, Int::times)

/**
 * Returns the product of all elements in the collection.
 */
fun Iterable<Long>.product(): Long = fold(1, Long::times)

/**
 * Returns the product of all elements in the sequence.
 *
 * The operation is _terminal_.
 */
fun Sequence<Int>.product(): Int = fold(1, Int::times)

/**
 * Returns the product of all elements in the sequence.
 *
 * The operation is _terminal_.
 */
fun Sequence<Long>.product(): Long = fold(1, Long::times)

/**
 * Returns a sequence of all occurrences of a [regular expression][regex] within the string.
 */
fun String.findAll(regex: Regex): Sequence<MatchResult> = regex.findAll(this)

inline fun <T> List<T>.indexOfFirstOrNull(
    fromIndex: Int, toIndex: Int,
    predicate: (T) -> Boolean,
): Int? = this
    .subList(fromIndex, toIndex)
    .indexOfFirst(predicate)
    .takeUnless { it == -1 }
    ?.plus(fromIndex)

inline fun <T> List<T>.indexOfFirstOrNull(
    predicate: (T) -> Boolean,
): Int? = this
    .indexOfFirst(predicate)
    .takeUnless { it == -1 }

inline fun <T> List<T>.indexOfLastOrNull(
    fromIndex: Int, toIndex: Int,
    predicate: (T) -> Boolean,
): Int? = this
    .subList(fromIndex, toIndex)
    .indexOfLast(predicate)
    .takeUnless { it == -1 }
    ?.plus(fromIndex)

inline fun <T> List<T>.indexOfLastOrNull(
    predicate: (T) -> Boolean,
): Int? = this
    .indexOfLast(predicate)
    .takeUnless { it == -1 }

fun List<String>.dim() = Rect(
    w = size,
    h = first().length,
)

fun List<String>.mirrorHor() = map(String::reversed)

fun List<String>.mirrorVer() = reversed()

fun List<String>.transpose(): List<String> {
    val (n, m) = dim()
    return List(m) { j ->
        List(n) { i ->
            this[i][j]
        }.joinToString(separator = "")
    }
}

inline fun <T> Iterable<Iterable<T>>.iterate(b: (Int, Int, T) -> Unit) =
    forEachIndexed { i, line ->
        line.forEachIndexed { j, element ->
            b(i, j, element)
        }
    }

inline fun <T> List<T>.iteratePermutations(b: (T, T) -> Unit) {
    for (i in 0..lastIndex) {
        for (j in 0..lastIndex) {
            b(this[i], this[j])
        }
    }
}

inline fun <T> List<T>.iterateOrderedCombinations(b: (T, T) -> Unit) {
    for (i in 0..<lastIndex) {
        for (j in i + 1..lastIndex) {
            b(this[i], this[j])
        }
    }
}

inline fun <T> Iterable<Iterable<T>>.iterateMap(b: (Pos, T) -> Unit) {
    forEachIndexed { y, line ->
        line.forEachIndexed { x, element ->
            b(Pos(x, y), element)
        }
    }
}

@JvmName("iterateMapString")
inline fun Iterable<String>.iterateMap(b: (Pos, Char) -> Unit) {
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            b(Pos(x, y), c)
        }
    }
}

operator fun List<String>.get(pos: Pos) = this[pos.y][pos.x]

operator fun <T> List<List<T>>.get(pos: Pos) = this[pos.y][pos.x]

operator fun <T> List<MutableList<T>>.set(pos: Pos, value: T) {
    this[pos.y][pos.x] = value
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
