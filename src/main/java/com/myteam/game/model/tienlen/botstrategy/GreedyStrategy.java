package com.myteam.game.model.tienlen.botstrategy;

import com.myteam.game.model.core.card.StandardCard;
import com.myteam.game.model.core.card.StandardCardComparator;
import com.myteam.game.model.core.enums.Rank;
import com.myteam.game.model.core.enums.Suit;
import com.myteam.game.model.game.TienLenMienBacGameLogic;
import com.myteam.game.model.tienlen.player.TienLenBotPlayer;

import java.util.*;

public class GreedyStrategy implements BotStrategy{
    private final StandardCardComparator comparator = new StandardCardComparator();
    @Override
    public List<StandardCard> decideCardsToPlay(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic) {
        List<StandardCard> selected = new ArrayList<>();

        boolean tableIsNone = cardsOnTable.isEmpty();
        boolean tableIsSingle = (cardsOnTable.size() == 1);
        boolean tableIsPair = gameLogic.isPair(cardsOnTable);
        boolean tableIsThree = gameLogic.isThreeOfKind(cardsOnTable);
        boolean tableIsFour = gameLogic.isFourOfKind(cardsOnTable);
        boolean tableIsSequence = gameLogic.isSequence(cardsOnTable);

        hand.sort(Comparator.comparing((StandardCard c) -> c.getRank().ordinal())
                .thenComparing((StandardCard c) -> c.getSuit().ordinal()));

        if (tableIsNone) {
            if(gameLogic.isFirstTurn()) {
                for (StandardCard card : hand) {
                    if (card.getRank() == Rank.THREE && card.getSuit() == Suit.SPADES) {
                        selected = List.of(card);
                        return selected;
                    }
                }
            }
            StandardCard randomCard = hand.get(new Random().nextInt(hand.size()));
            selected = List.of(randomCard);
        } else if (tableIsSingle) {
            StandardCard singleCard = findSingleCard(hand, cardsOnTable, gameLogic);
            if (singleCard != null) {
                selected = List.of(singleCard);
            } else if (cardsOnTable.getFirst().getRank() == Rank.TWO) {
                selected = findFour(hand, cardsOnTable, gameLogic);
            }
        } else if (tableIsPair) {
            selected = findPair(hand, cardsOnTable, gameLogic);
        } else if (tableIsThree) {
            selected = findThree(hand, cardsOnTable, gameLogic);
        } else if (tableIsFour) {
            selected = findFour(hand, cardsOnTable, gameLogic);
        } else if (tableIsSequence) {
            selected = findSequence(hand, cardsOnTable, gameLogic);
        } else {
            System.err.println("ERROR: Cards on table is invalid!");
        }

        if (selected == null || selected.isEmpty()) {
            return new ArrayList<>();
        } else {
            return selected;
        }
    }

    private StandardCard findSingleCard(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic) {
        StandardCard topCard = cardsOnTable.getFirst();

        for (StandardCard card : hand) {
            // Nếu là lá 2 → chỉ cần mạnh hơn (cùng rank, chất cao hơn)
            if (topCard.getRank() == Rank.TWO) {
                if (card.getRank() == Rank.TWO && comparator.compare(card, topCard) > 0) {
                    return card;
                }
            } else {
                // Bài khác → cùng chất và mạnh hơn
                if (card.getSuit() == topCard.getSuit() && comparator.compare(card, topCard) > 0) {
                    return card;
                }
            }
        }
        return null;
    }

    private List<StandardCard> findPair(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic) {
        if (hand.size() < 2) {
            return null;
        }
        for (int i = 0; i < hand.size() - 1; i++) {
            List<StandardCard> selectedCards = new ArrayList<>(Arrays.asList(hand.get(i), hand.get(i + 1)));
            if (gameLogic.isPair(selectedCards)
                    && gameLogic.isSameColor(selectedCards.getFirst(), cardsOnTable.getFirst())
                    && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                return selectedCards;
            }
        }
        return new ArrayList<>();
    }

    private List<StandardCard> findThree(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic) {
        if (hand.size() < 3) {
            return new ArrayList<>();
        }
        for (int i = 0; i < hand.size() - 2; i++) {
            List<StandardCard> selectedCards = new ArrayList<>(
                    Arrays.asList(hand.get(i), hand.get(i + 1), hand.get(i + 2)));
            if (gameLogic.isThreeOfKind(selectedCards)
                    && gameLogic.isSameColor(selectedCards.getFirst(), cardsOnTable.getFirst())
                    && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                return selectedCards;
            }
        }
        return new ArrayList<>();
    }

    private List<StandardCard> findFour(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic) {
        if (hand.size() < 4) {
            return new ArrayList<>();
        }
        for (int i = 0; i < hand.size() - 3; i++) {
            List<StandardCard> selectedCards = new ArrayList<>(
                    Arrays.asList(hand.get(i), hand.get(i + 1), hand.get(i + 2), hand.get(i + 3)));
            if (gameLogic.isFourOfKind(selectedCards)
                    && gameLogic.isSameColor(selectedCards.getFirst(), cardsOnTable.getFirst())
                    && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                return selectedCards;
            }
        }
        return new ArrayList<>();
    }

    private List<StandardCard> findSequence(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic) {
        int length = cardsOnTable.size();
        if (hand.size() < length) {
            return new ArrayList<>();
        }
        for (int i = 0; i <= hand.size() - length; i++) {
            List<StandardCard> selectedCards = hand.subList(i, i + length);
            if (gameLogic.isSequence(selectedCards)
                    && comparator.compare(selectedCards.getFirst(), cardsOnTable.getFirst()) > 0) {
                return new ArrayList<>(selectedCards);
            }
        }
        return new ArrayList<>();
    }
}