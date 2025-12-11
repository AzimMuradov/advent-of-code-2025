import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.math.abs


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
 * Split string to longs using provided [separator].
 */
fun String.toLongs(separator: Regex) = this
    .split(separator)
    .map(String::toLong)

/**
 * Removes from a string both the prefix and suffix of given lengths.
 */
fun String.removeSurrounding(prefixLength: Int, suffixLength: Int): String =
    substring(prefixLength, length - suffixLength)

/**
 * Removes from a string both the prefix and suffix of given length.
 */
fun String.removeSurrounding(length: Int): String =
    removeSurrounding(length, length)

/**
 * Converts list to pair.
 */
fun <T> List<T>.toPair() = Pair(this[0], this[1])

/**
 * Converts list to triple.
 */
fun <T> List<T>.toTriple() = Triple(this[0], this[1], this[2])

fun Pair<Int, Int>.toRange() = first..second

fun Pair<Long, Long>.toRange() = first..second

val IntRange.size: Int get() = last - first + 1

val LongRange.size: Long get() = last - first + 1

/**
 * Converts list to triple.
 */
fun <T> List<T>.workAsMut(block: MutableList<T>.() -> Unit): List<T> =
    toMutableList().apply(block)

/**
 * Splits string into several at the given [indices].
 *
 * Provided [indices] would be
 * the exclusive end and inclusive start of resulting strings.
 */
fun String.splitAt(vararg indices: Int): List<String> =
    (listOf(0) + indices.asList() + listOf(length))
        .zipWithNext()
        .map { (startIndex, endIndex) ->
            substring(startIndex, endIndex)
        }

/**
 * Splits list into several at the given [indices].
 *
 * Provided [indices] would be
 * the exclusive end and inclusive start of resulting lists.
 */
fun <T> List<T>.splitAt(vararg indices: Int): List<List<T>> =
    (listOf(0) + indices.asList() + listOf(size))
        .zipWithNext()
        .map { (startIndex, endIndex) ->
            subList(startIndex, endIndex)
        }

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

@JvmName("plusIntTo")
fun <K> MutableMap<K, Int>.plusTo(key: K, number: Int) {
    put(key, getOrDefault(key, defaultValue = 0) + number)
}

@JvmName("plusLongTo")
fun <K> MutableMap<K, Long>.plusTo(key: K, number: Long) {
    put(key, getOrDefault(key, defaultValue = 0) + number)
}

@JvmName("minusIntTo")
fun <K> MutableMap<K, Int>.minusTo(key: K, number: Int) {
    plusTo(key, -number)
}

@JvmName("minusLongTo")
fun <K> MutableMap<K, Long>.minusTo(key: K, number: Long) {
    plusTo(key, -number)
}

@JvmName("incInt")
fun <K> MutableMap<K, Int>.inc(key: K) {
    plusTo(key, 1)
}

@JvmName("incLong")
fun <K> MutableMap<K, Long>.inc(key: K) {
    plusTo(key, 1)
}

@JvmName("decInt")
fun <K> MutableMap<K, Int>.dec(key: K) {
    minusTo(key, 1)
}

@JvmName("decLong")
fun <K> MutableMap<K, Long>.dec(key: K) {
    minusTo(key, 1)
}

fun List<List<*>>.rect() = Rect(
    w = first().size,
    h = size,
)

@JvmName("rectString")
fun List<String>.rect() = Rect(
    w = first().length,
    h = size,
)

fun List<String>.mirrorHor() = map(String::reversed)

fun List<String>.mirrorVer() = reversed()

fun <T> List<List<T>>.transpose(): List<List<T>> {
    val (w, h) = rect()
    return List(w) { x ->
        List(h) { y ->
            this[y][x]
        }
    }
}

@JvmName("transposeString")
fun List<String>.transpose(): List<String> {
    val (w, h) = rect()
    return List(w) { x ->
        List(h) { y ->
            this[y][x]
        }.joinToString(separator = "")
    }
}

inline fun <T> Iterable<Iterable<T>>.iterate(b: (Int, Int, T) -> Unit) =
    forEachIndexed { i, line ->
        line.forEachIndexed { j, element ->
            b(i, j, element)
        }
    }

@JvmName("iterateOverIntRanges")
inline fun iterateOverRanges(ranges: List<IntRange>, b: (List<Int>) -> Unit) {
    if (ranges.any { it.isEmpty() }) return

    val state = ranges.mapTo(mutableListOf()) { r -> r.first }
    multiloop@ while (true) {
        for (i in state.indices.reversed()) {
            if (state[i] !in ranges[i]) {
                if (i != 0) {
                    state[i] = ranges[i].first
                    state[i - 1] += 1
                } else {
                    break@multiloop
                }
            }
        }
        b(state.toList())
        state[state.lastIndex] += 1
    }
}

@JvmName("iterateOverLongRanges")
inline fun iterateOverRanges(ranges: List<LongRange>, b: (List<Long>) -> Unit) {
    if (ranges.any { it.isEmpty() }) return

    val state = ranges.mapTo(mutableListOf()) { r -> r.first }
    multiloop@ while (true) {
        for (i in state.indices.reversed()) {
            if (state[i] !in ranges[i]) {
                if (i != 0) {
                    state[i] = ranges[i].first
                    state[i - 1] += 1
                } else {
                    break@multiloop
                }
            }
        }
        b(state.toList())
        state[state.lastIndex] += 1
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

fun gcd(a: Int, b: Int): Int {
    if (a == 0) return abs(b)
    if (b == 0) return abs(a)

    var a = if (a > 0) a else -a
    var b = if (b > 0) b else -b

    while (b != 0) {
        val temp = b
        b = a % b
        a = temp
    }

    return a
}

fun gcd(a: Long, b: Long): Long {
    if (a == 0L) return abs(b)
    if (b == 0L) return abs(a)

    var a = if (a > 0) a else -a
    var b = if (b > 0) b else -b

    while (b != 0L) {
        val temp = b
        b = a % b
        a = temp
    }

    return a
}

fun gcd(vararg elements: Int): Int = elements.reduce(operation = ::gcd)

fun gcd(vararg elements: Long): Long = elements.reduce(operation = ::gcd)
