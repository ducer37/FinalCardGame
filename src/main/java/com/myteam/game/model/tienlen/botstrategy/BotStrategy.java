package com.myteam.game.model.tienlen.botstrategy;

import com.myteam.game.model.core.card.StandardCard;
import com.myteam.game.model.game.TienLenMienBacGameLogic;
import com.myteam.game.model.tienlen.player.TienLenBotPlayer;

import java.util.List;

public interface BotStrategy {
    List<StandardCard> decideCardsToPlay(List<StandardCard> hand, List<StandardCard> cardsOnTable, TienLenMienBacGameLogic gameLogic);
}