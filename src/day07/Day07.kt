package day07

import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun main() {
    val testInput = Path("src/day07/Day07_test.txt")
    val input = Path("src/day07/Day07.txt")

    testInput.part1().also { check(it == 6440) }
    input.part1().also { check(it == 253313241) }
    testInput.secondAttempt()
    val second = input.secondAttempt()



//    measureTime {
//        input.part1()
//    }.also { println("Part 1 took ${it.inWholeSeconds}s") }
}


private fun Path.secondAttempt() = this.readLines()
    .map { handWithBid ->
        handWithBid
            .split(" ")
            .let {
                Triple(it.first().trim().toTypeV2(), it.first(), it.last().toInt())
            }

    }
    .sortedWith(Comp())
    .sortedBy { it.first.values.max() }
    .mapIndexed { index, (type, hand, bid) ->
        hand//.also (::println)//(index + 1) * bid
    }
    //.sum()
    .map { it.toList().sorted().joinToString(separator = "").groupingBy { it }.eachCount() } // sort cards in hand
    .also(::println)


private class Comp : Comparator<Triple<Map<Char, Int>, String, Int>> {
    override fun compare(o1: Triple<Map<Char, Int>, String, Int>, o2: Triple<Map<Char, Int>, String, Int>): Int {
        val o1MaxRank = o1.first.values.max()
        val o2MaxRank = o2.first.values.max()

        if (o1MaxRank != o2MaxRank) return o1MaxRank.compareTo(o2MaxRank)

        if (o2MaxRank == 3 || o1MaxRank == 3) {
            val isO1FullHouse = o1.first.values.groupingBy { it }.eachCount().size == 2
            val isO2FullHouse = o2.first.values.groupingBy { it }.eachCount().size == 2
            if (isO1FullHouse != isO2FullHouse) return isO1FullHouse.compareTo(isO2FullHouse)
        }

        if (o2MaxRank == 2 || o1MaxRank == 2) {
            val o1NumOfPairs = o1.first.values.groupingBy { it }.eachCount()[2]
            val o2NumOfPairs = o2.first.values.groupingBy { it }.eachCount()[2]
            if (o1NumOfPairs != o2NumOfPairs) return o1NumOfPairs!!.compareTo(o2NumOfPairs!!)
        }

        for (i in 0..o1.second.length) {
            if (o1.second[i] != o2.second[i]) {
                return cardRank.indexOf(o1.second[i]).compareTo(cardRank.indexOf(o2.second[i]))
            }
        }

        return 0
    }
}

private const val cardRank = "23456789TJQKA"

private fun Path.part1() = this.readLines()
    .map { handWithBid ->
        handWithBid
            .split(" ")
            .let {
                Triple(
                    first = it.first().toType(),
                    second = Hand(it.first().map { card -> card.toCard() }),
                    third = it.last().toInt()
                )
            }
    }
    .sortedByDescending { it.second }
    .sortedByDescending { it.first }
    //.onEach { println(it) }
    .mapIndexed { index, triple ->
       (index + 1) * triple.third
    }
    //.map { it.toList().sorted().joinToString(separator = "").groupingBy { it }.eachCount() } // sort cards in hand
    .sum()
    .also(::println)


private data class Hand(val cards: List<Card>) : Comparable<Hand> {
    override fun compareTo(other: Hand): Int {
        for (i in 0..this.cards.size) {
            if (this.cards[i] != other.cards[i]) {
                return this.cards[i].compareTo(other.cards[i])
            }
        }
        return 0
    }
}


private enum class Card(val c: Char) {
    A('A'), K('K'), Q('Q'), J('J'), T('T'), Nine('9'), Eight('8'), Seven('7'),
    Six('6'), Five('5'), Four('4'), Three('3'), Two('2')
}

private fun Char.toCard() = when (this) {
    'A' -> Card.A
    'K' -> Card.K
    'Q' -> Card.Q
    'J' -> Card.J
    'T' -> Card.T
    '9' -> Card.Nine
    '8' -> Card.Eight
    '7' -> Card.Seven
    '6' -> Card.Six
    '5' -> Card.Five
    '4' -> Card.Four
    '3' -> Card.Three
    '2' -> Card.Two
    else -> throw IllegalStateException("Unexpected card $this")
}


private enum class Type { FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPair, OnePair, HighCard }

private fun String.toType() = this
    .trim()
    .groupBy { it }
    .map { (card, cardIndices) -> card to cardIndices.size }
    .let { hand: List<Pair<Char, Int>> ->
        hand
            .map { it.second }
            .let { numberOfMatchingCards ->
                when {
                    numberOfMatchingCards.contains(5) -> Type.FiveOfAKind
                    numberOfMatchingCards.contains(4) -> Type.FourOfAKind
                    numberOfMatchingCards.contains(3) -> {
                        if (numberOfMatchingCards.size == 2) {
                            Type.FullHouse
                        } else {
                            Type.ThreeOfAKind
                        }
                    }

                    numberOfMatchingCards.contains(2) -> {
                        numberOfMatchingCards
                            .count { it == 2 }
                            .let { numberOfPairs ->
                                when (numberOfPairs) {
                                    2 -> Type.TwoPair
                                    1 -> Type.OnePair
                                    else -> throw IllegalStateException("Contains more or less pairs then expected $numberOfPairs")
                                }
                            }
                    }

                    numberOfMatchingCards.contains(1) -> Type.HighCard
                    else -> throw IllegalStateException("Unexpected number of matching cards $numberOfMatchingCards")
                }
            }
    }

private fun String.toTypeV2() = this
    .trim()
    .groupingBy { it }
    .eachCount()




