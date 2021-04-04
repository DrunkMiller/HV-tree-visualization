import kotlin.math.abs
import kotlin.math.floor

typealias Boundary = List<Pair<Int, Int>>

fun boundaryFrom(x: Int) = listOf(Pair(x, x))

fun distance(left: Boundary, right: Boundary) = left
    .zip(right) { a, b -> b.first - a.second }
    .minOf { it }

fun Boundary.shift(shift: Int): Boundary = map {
    Pair(it.first - shift, it.second - shift)
}

fun Boundary.combine(right: Boundary): Boundary {
    val left = this
    val firstPart = left.zip(right) { a, b -> Pair(a.first, b.second) }
    return when {
        left.size > right.size -> firstPart + left.subList(right.size, left.size)
        left.size < right.size -> firstPart + right.subList(left.size, right.size)
        else -> firstPart
    }
}

class LayeredTreeAlgorithm {
    private val positions: MutableMap<String, Position> = mutableMapOf()
    private var markersCounter: Int = 0

    fun place(root: Node): Map<Node, Position> {
        positions.clear()
        markersCounter = 0
        walk(root)
        val nodes = (listOf(root) + root.allChildren())
            .associateWith { positions[it.id]!! }
        return xAdjustment(nodes)
    }

    private fun xAdjustment(nodes: Map<Node, Position>): Map<Node, Position> {
        val adjustment = nodes.values.minOf { it.x }
        return if(adjustment < 1) nodes.mapValues { it.value.copy(x = it.value.x + abs(adjustment) + 1) } else nodes
    }

    private fun walk(node: Node, deep: Int = 1, isOneChild: Boolean = false): Boundary {
        val (id, children) = node
        val advanceMark = markersCounter
        val (before, after) = children.split()

        val beforeBoundary = before.map { it to walk(it, deep + 1, children.size == 1) }
        val mark = if (isOneChild) advanceMark else ++markersCounter
        positions[id] = Position(deep.toDouble(), mark.toDouble())
        if (node.isLeaf()) return boundaryFrom(mark)
        val afterBoundary = after.map { it to walk(it, deep + 1, children.size == 1) }

        val newBoundary = squeeze(beforeBoundary + afterBoundary)
        val newMark = placeBetweenChildren(node)
        return boundaryFrom(newMark) + newBoundary
    }

    private fun List<Node>.split() = Pair(subList(0, size / 2), subList(size / 2, size))

    private fun squeeze(boundary: List<Pair<Node, Boundary>>): Boundary {
        if (boundary.isEmpty()) return emptyList()
        val newRightBoundaries = boundary.reduce { (l, leftBoundaries), (rightNode, rightBoundaries) ->
            val distance = distance(leftBoundaries, rightBoundaries) - 1
            if (distance > 0) move(rightNode, distance)
            val newRightBoundaries = if (distance > 0) rightBoundaries.shift(distance) else rightBoundaries
            rightNode to leftBoundaries.combine(newRightBoundaries)
        }
        return boundary.first().second.combine(newRightBoundaries.second)
    }

    private fun move(current: Node, shift: Int) {
        val (id, children) = current
        positions[id] = positions[id]!!.shift(shift.toDouble())
        children.forEach { move(it, shift) }
    }

    private fun placeBetweenChildren(node: Node): Int {
        val (id, children) = node
        return when (children.size) {
            0 -> positions[id]!!.x.toInt()
            1 -> {
                val child = positions[children.first().id]!!
                positions[id] = positions[id]!!.copy(x = child.x)
                child.x.toInt()
            }
            else -> {
                val left = positions[children.first().id]!!
                val right = positions[children.last().id]!!
                val newX = floor((left.x + right.x) / 2)
                positions[id] = positions[id]!!.copy(x = newX)
                newX.toInt()
            }
        }
    }
}
