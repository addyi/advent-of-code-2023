package day08

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

fun main() {
    val testInput = Path("src/day08/Day08_part1_test.txt")
    val input = Path("src/day08/Day08.txt")

    testInput.part1().also { check(it == 6) }
    input.part1().also { check(it == 13771) }

    measureTime {
        input.part1()
    }.also { println("Part 1 took $it") }
}

private fun Path.part1() = this.readLines()
    .parseInput()
    .let { (lrInstructions, nodes) ->
        // traverseNodes(nodes, "AAA", lrInstructions.iterator(), 0) // fixme: stack overflow ^^
        traverseNodesIterative(nodes, lrInstructions.iterator())
    }
    .also(::println)

private fun traverseNodesRecursive(
    nodes: Map<String, Pair<String, String>>,
    currentNode: String,
    lrInstructions: Iterator<Char>,
    numberOfNavigationSteps: Int
): Int {
    if (currentNode == "ZZZ") return numberOfNavigationSteps

    val node = nodes[currentNode] ?: error("unknown key $currentNode")

    // println("$currentNode: $node")

    val nextNode = if (lrInstructions.next() == 'L') node.first else node.second

    return traverseNodesRecursive(nodes, nextNode, lrInstructions, numberOfNavigationSteps + 1)
}


private fun traverseNodesIterative(
    nodes: Map<String, Pair<String, String>>,
    lrInstructions: Iterator<Char>
): Int {
    var currentNode = "AAA"
    var numberOfNavigationSteps = 0

    while (currentNode != "ZZZ") {
        val node = nodes[currentNode] ?: error("unknown key $currentNode")

        // println("$currentNode: $node")

        currentNode = if (lrInstructions.next() == 'L') node.first else node.second

        numberOfNavigationSteps++
    }

    return numberOfNavigationSteps
}

private fun List<String>.parseInput(): Pair<CircularString, Map<String, Pair<String, String>>> {
    val lrInstructions = this.first()

    val nodes = this.drop(2)
        .map { node ->
            val key = node.take(3)
            val lrValues = node.drop(7)
                .dropLast(1)
                .split(", ")
                .let { it.first() to it.last() }

            key to lrValues
        }
        .associateBy({ (key, _) -> key }, { (_, lrValues) -> lrValues })

    return CircularString(lrInstructions) to nodes
}

class CircularString(private val lrInstructions: String) : Iterable<Char> {

    override fun iterator() = object : Iterator<Char> {

        private var currentPos = -1

        override fun hasNext(): Boolean = true

        override fun next(): Char {
            currentPos = (currentPos + 1) % lrInstructions.length
            return lrInstructions[currentPos]
        }
    }
}
