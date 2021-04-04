
data class Node(
    val id: String,
    val children: MutableList<Node> = mutableListOf()
) {
    fun isLeaf() = children.isEmpty()
    fun allChildren(): List<Node> = children + children.flatMap(Node::allChildren)
}

data class Position(val y: Double, val x: Double) {
    fun shift(shift: Double) = this.copy(x = this.x - shift)
}
