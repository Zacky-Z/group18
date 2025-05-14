package com.forbiddenisland.core.model;

import com.forbiddenisland.enums.SpecialCardType;

public class SpecialActionCard extends Card {
    private final SpecialCardType cardType;

    public SpecialActionCard(SpecialCardType cardType) {
        super(cardType.getDisplayName());
        this.cardType = cardType;
    }

    public SpecialCardType getCardType() {
        return cardType;
    }

    @Override
    public String getDescription() {
        return cardType.getDescription();
    }
}    