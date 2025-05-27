package com.myteam.game.model.tienlen.player;

import com.myteam.game.model.core.card.StandardCard;
import com.myteam.game.model.game.TienLenMienBacGameLogic;
import com.myteam.game.model.tienlen.botstrategy.BotStrategy;
import com.myteam.game.model.tienlen.gamestate.TienLenGameState;

import java.util.*;

/**
 * A bot player implementation for TienLen game
 */
public class TienLenBotPlayer extends TienLenPlayer {
    private final TienLenMienBacGameLogic gameLogic;
    private final BotStrategy strategy;

    public TienLenBotPlayer(String name, TienLenMienBacGameLogic gameLogic, BotStrategy strategy) {
        super(name);
        this.strategy = strategy;
        this.gameLogic = gameLogic;
    }

    @Override
    public List<StandardCard> decideCardsToPlay(TienLenGameState gameState) {
        List<StandardCard> cardsOnTable = gameState.getCardsOnTable();

        return strategy.decideCardsToPlay(getHand(), cardsOnTable, gameLogic);
    }
}