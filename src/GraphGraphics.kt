import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints.*
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import java.awt.image.BufferedImage

class GraphGraphics(
    val nodeRadius: Double = 20.0,
    val fillColor: Color = Color.blue,
    val borderColor: Color = Color.black,
    val xPadding: Double = 80.0,
    val yPadding: Double = 100.0,
    val nodeTitle: Boolean = true,
    val grid: Boolean = false
) {
    private lateinit var image: BufferedImage
    private lateinit var graphics: Graphics2D
    private lateinit var nodes: Map<Node, Position>

    fun drawTree(nodesInfo: Map<Node, Position>): BufferedImage {
        nodes = nodesInfo
        prepareCanvas()
        nodes.forEach { drawNode(it.key, it.value) }
        return image
    }

    fun drawNode(node: Node, pos: Position) {
        val (id, children) = node
        children.forEach { drawEdge(pos, nodes[it]!!) }
        val nodeShape = nodeShape(pos.x, pos.y)
        graphics.color = borderColor
        graphics.draw(nodeShape)
        graphics.color = fillColor
        graphics.fill(nodeShape)
        if (nodeTitle) drawTitle(id, pos)
    }

    fun drawEdge(from: Position, to: Position) {
        val edgeShape = edgeShape(from.x, from.y, to.x, to.y)
        graphics.color = borderColor
        graphics.draw(edgeShape)
    }

    fun drawTitle(title: String, pos: Position) {
        graphics.color = borderColor
        graphics.drawString(title, (pos.x * xPadding).toFloat(), (pos.y * yPadding - nodeRadius).toFloat())
    }

    private fun nodeShape(x: Double, y: Double) = Ellipse2D.Double(
        x * xPadding - nodeRadius / 2,
        y * yPadding - nodeRadius / 2,
        nodeRadius,
        nodeRadius
    )

    private fun edgeShape(sourceX: Double, sourceY: Double, destX: Double, destY: Double) = Line2D.Double(
        sourceX * xPadding,
        sourceY * yPadding,
        destX * xPadding,
        destY * yPadding
    )

    private fun prepareCanvas() {
        val width = nodes.values.map { it.x }.max()!! + 1
        val height = nodes.values.map { it.y }.max()!! + 1

        image = BufferedImage((width * xPadding).toInt(), (height * yPadding).toInt(), BufferedImage.TYPE_INT_RGB)
        graphics = image.createGraphics()
        graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY)
        graphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC)
        graphics.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY)

        graphics.font = java.awt.Font.getFont("Courier")
        graphics.color = Color.white
        graphics.fillRect(0, 0, (width * xPadding).toInt(), (height * yPadding).toInt())
        if (grid) drawGrid()
    }

    private fun drawGrid() {
        fun drawVerticalGridLine(x: Int) = graphics.drawLine(x, 0, x, image.height)
        fun drawHorizontalGridLine(y: Int) = graphics.drawLine(0, y, image.width, y)
        graphics.color = Color.GRAY
        if (grid) {
            (0 .. image.width step xPadding.toInt()).forEach(::drawVerticalGridLine)
            (0 .. image.height step yPadding.toInt()).forEach(::drawHorizontalGridLine)
        }
    }
}