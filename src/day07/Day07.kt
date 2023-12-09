package day07

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val testInput = Path("src/day07/Day07_test.txt")
    val input = Path("src/day07/Day07.txt")

    testInput.part1().also { check(it == 6440) }
    input.part1().also { check(it == 253313241) }
}

private fun Path.part1() = this
    .readLines()
    .map { handWithBid -> handWithBid.parseInput() }
    .sortedWith(
        compareBy(
            { (type, _, _) -> type },
            { (_, hand, _) -> hand }
        )
    )
    .reversed()
    .foldIndexed(0) { index, acc, (_, _, bid) ->
        acc + (index + 1) * bid
    }
    .also(::println)

private fun String.parseInput() = this
    .split(" ")
    .let {
        Triple(
            first = it.first().toType(),
            second = Hand(it.first()),
            third = it.last().toInt()
        )
    }


private data class Hand(val cards: String) : Comparable<Hand> {

    override fun compareTo(other: Hand): Int {
        for ((thisCard, otherCard) in this.cards.zip(other.cards)) {
            if (thisCard != otherCard) {
                return CARD_RANK.indexOf(thisCard).compareTo(CARD_RANK.indexOf(otherCard))
            }
        }
        return 0
    }

    private companion object {
        private const val CARD_RANK = "AKQJT98765432"
    }
}


private enum class Type { FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard }

private fun String.toType() = this
    .groupingBy { it }
    .eachCount()
    .values
    .let { counts ->
        when {
            5 in counts -> Type.FiveOfAKind
            4 in counts -> Type.FourOfAKind
            3 in counts && 2 in counts -> Type.FullHouse
            3 in counts -> Type.ThreeOfAKind
            2 in counts -> if (counts.count { it == 2 } == 2) Type.TwoPair else Type.OnePair
            else -> Type.HighCard
        }
    }
