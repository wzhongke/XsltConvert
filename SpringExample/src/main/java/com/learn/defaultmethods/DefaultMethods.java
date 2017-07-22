package com.learn.defaultmethods;

import java.util.*;

/**
 * Created by admin on 2017/7/18.
 */
public class DefaultMethods {
    public static void main(String[] args) {
        StandardDeck deck = new StandardDeck();
        deck.shuffle();
        deck.sort(Comparator.comparing(Card::getRank));
//        deck.sort(
//                Comparator
//                        .comparing(Card::getSuit)
//                        .thenComparing(Comparator.comparing(Card::getRank)));
        System.out.println(deck.getEntireDeck());
        deck.sort(
                Comparator.comparing(Card::getSuit)
                        .reversed()
                        .thenComparing(Comparator.comparing(Card::getRank)));
        System.out.println(deck.getEntireDeck());
    }
}

class StandardDeck implements Deck {

    private List<Card> entireDeck;

    StandardDeck() {
        entireDeck = new ArrayList<>();
        entireDeck.add(new PlayingCard(Card.Suit.CLUBS, Card.Rank.ACE));
        entireDeck.add(new PlayingCard(Card.Suit.DIAMONDS, Card.Rank.JACK));
        entireDeck.add(new PlayingCard(Card.Suit.SPADES, Card.Rank.QUEEN));
        entireDeck.add(new PlayingCard(Card.Suit.SPADES, Card.Rank.KING));
        entireDeck.add(new PlayingCard(Card.Suit.HEARTS, Card.Rank.FIVE));
    }

    @Override
    public List<Card> getCards() {
        return null;
    }

    @Override
    public Deck deckFactory() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void addCard(Card card) {

    }

    @Override
    public void addCards(List<Card> cards) {

    }

    @Override
    public void addDeck(Deck deck) {

    }

    @Override
    public void shuffle() {

    }

    public void sort() {
        Collections.sort(entireDeck);
    }

    @Override
    public void sort(Comparator<Card> c) {
        Collections.sort(entireDeck, c);
    }

    @Override
    public String deckToString() {
        return null;
    }

    @Override
    public Map<Integer, Deck> deal(int players, int numberOfCards) throws IllegalArgumentException {
        return null;
    }

    public List<Card> getEntireDeck () {return this.entireDeck;}
}

class PlayingCard implements Card {

    private Suit suit;
    private Rank rank;

    PlayingCard(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    @Override
    public Suit getSuit() {
        return suit;
    }

    @Override
    public Rank getRank() {
        return rank;
    }

    @Override
    public int compareTo(Card o) {
        return this.hashCode() - o.hashCode();
    }

    public int hashCode() {
        return ((suit.value()-1)*13) + rank.value();
    }

    @Override
    public String toString() {
        return suit.value() + "  " + rank.value();
    }
}