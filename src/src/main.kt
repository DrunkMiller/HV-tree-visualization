import java.awt.Color
import java.awt.Shape
import java.awt.geom.Ellipse2D
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import java.awt.geom.Ellipse2D.Double

fun main() {
    println("gg")
    val root = readFromGraphml("tree-84n.graphml")
    val placedRoot = HVLaying(root)
    println(root)
    val width = 250
    val height = 250
    val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
    val g2d = bufferedImage.createGraphics()

    g2d.color = Color.white
    g2d.fillRect(0, 0, width, height)

    g2d.color = Color.black
    g2d.draw(nodeShape(placedRoot))
    g2d.color = Color.blue
    g2d.fill(nodeShape(placedRoot))

    g2d.color = Color.black
    g2d.draw(nodeShape(root.left!! as PlacedNode))
    g2d.color = Color.blue
    g2d.fill(nodeShape(root.left!! as PlacedNode))

    g2d.color = Color.black
    g2d.draw(nodeShape(root.right!! as PlacedNode))
    g2d.color = Color.blue
    g2d.fill(nodeShape(root.right!! as PlacedNode))

    g2d.
//    g2d.fillOval(0, 0, width, height)
//
//    g2d.color = Color.yellow
//    g2d.drawString("Java Code Geeks", 50, 120)

    g2d.dispose()
    val file = File("myimage.png")

    ImageIO.write(bufferedImage, "png", file)

}

fun HVLaying(node: Node, x: Int = 0, y: Int = 0): PlacedNode {
    val placed = PlacedNode(node, x, y)
    if (node.left != null) placed.left = HVLaying(node.left!! as Node , x, y + 1)
    if (node.right != null) placed.right = HVLaying(node.right!! as Node, x + 1, y)
    return placed
}

fun nodeShape(node: PlacedNode) = Ellipse2D.Double(node.xPos.toDouble(), node.yPos.toDouble(), 10.0, 10.0)