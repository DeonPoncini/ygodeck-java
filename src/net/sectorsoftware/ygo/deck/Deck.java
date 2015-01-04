package net.sectorsoftware.ygo.deck;

import java.util.ArrayList;

import net.sectorsoftware.ygo.data.DataTypes.DeckType;
import net.sectorsoftware.ygo.data.DataTypes.StaticCardData;
import net.sectorsoftware.ygo.deck.DataTypes.DeckError;

public class Deck
{
    private native long init(int deckType);
    private native long init_id(int deckType, String id);
    private native void delete(long p);
    private native int deckType(long p);
    private native String id(long p);
    private native int size(long p);
    private native int addCard(long p, String name);
    private native ArrayList<StaticCardData> cards(long p);
    private native void deleteCard(long p, String name);

    public Deck(DeckType deckType)
    {
        mDeckHandle = init(deckType.ordinal());
    }

    public Deck(DeckType deckType, String id)
    {
        mDeckHandle = init_id(deckType.ordinal(), id);
    }

    public void delete()
    {
        if (mDeckHandle != 0) {
            delete(mDeckHandle);
            mDeckHandle = 0;
        }
    }

    public DeckType deckType()
    {
        return DeckType.values()[deckType(mDeckHandle)];
    }

    public String id()
    {
        return id(mDeckHandle);
    }

    public int size()
    {
        return size(mDeckHandle);
    }

    public DeckError addCard(String name)
    {
        return DeckError.values()[addCard(mDeckHandle, name)];
    }

    public ArrayList<StaticCardData> cards()
    {
        return cards(mDeckHandle);
    }

    public void deleteCard(String name)
    {
        deleteCard(name);
    }

    private long mDeckHandle = 0;
}