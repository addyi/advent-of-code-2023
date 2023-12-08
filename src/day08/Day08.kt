package day08

import leastCommonMultiple
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

fun main() {
    val testInputPart1 = Path("src/day08/Day08_part1_test.txt")
    val testInputPart2 = Path("src/day08/Day08_part2_test.txt")
    val input = Path("src/day08/Day08.txt")

    testInputPart1.part1().also { check(it == 6) }

    measureTime {
        input.part1().also { check(it == 13771) }
    }.also { println("Part 1 took $it") }

    println()

    testInputPart2.part2().also { check(it == 6L) }

    println()

    measureTime {
        input.part2().also { check(it == 13129439557681L) }
    }.also { println("Part 2 took $it") }
}

private fun Path.part1() = this.readLines()
    .parseInput()
    .let { (lrInstructions, nodes) -> traverseNodesIterative(nodes, lrInstructions.iterator()) }
    .also { println("Result: $it") }

private fun Path.part2() = this.readLines()
    .parseInput()
    .let { (lrInstructions, nodes) ->
        nodes
            .filter { it.key.last() == 'A' }
            .map { it.key }
            .also { println("Start nodes $it") }
            .map { startNode -> traverseNodesIterativePart2(nodes, startNode, lrInstructions.iterator()) }
            .also { println("Steps till 'Z': $it") }
            .reduce { acc, i -> leastCommonMultiple(acc, i) }
    }
    .also { println("Result: $it") }


private fun traverseNodesIterativePart2(
    nodes: Map<String, Pair<String, String>>,
    startNode: String,
    lrInstructions: Iterator<Char>
): Long {
    var currentNode = startNode
    var numberOfNavigationSteps = 0L

    while (!currentNode.endsWith('Z')) {
        currentNode = if (lrInstructions.next() == 'L') nodes[currentNode]!!.first else nodes[currentNode]!!.second
        numberOfNavigationSteps++
    }

    return numberOfNavigationSteps
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

private class CircularString(private val lrInstructions: String) : Iterable<Char> {

    override fun iterator() = object : Iterator<Char> {

        private var currentPos = -1

        override fun hasNext(): Boolean = true

        override fun next(): Char {
            currentPos = (currentPos + 1) % lrInstructions.length
            return lrInstructions[currentPos]
        }
    }
}
