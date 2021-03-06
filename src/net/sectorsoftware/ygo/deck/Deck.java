package net.sectorsoftware.ygo.deck;

import java.util.List;

import net.sectorsoftware.ygo.data.DataTypes.DeckType;
import net.sectorsoftware.ygo.data.DataTypes.StaticCardData;
import net.sectorsoftware.ygo.deck.DataTypes.DeckError;

public class Deck
{
    private native long init(int deckType);
    private native long initID(int deckType, String id);
    private native void delete(long p);
    private native int deckType(long p);
    private native String id(long p);
    private native int size(long p);
    private native int addCard(long p, String name);
    private native List<StaticCardData> cards(long p);
    private native void deleteCard(long p, String name);
    private native void remove(long p);

    public Deck(DeckType deckType)
    {
        mDeckHandle = init(deckType.ordinal());
    }

    public Deck(DeckType deckType, String id)
    {
        mDeckHandle = initID(deckType.ordinal(), id);
    }

    /* package */ Deck(long handle)
    {
        mDeckHandle = handle;
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
        assert mDeckHandle != 0;
        return DeckType.values()[deckType(mDeckHandle)];
    }

    public String id()
    {
        assert mDeckHandle != 0;
        return id(mDeckHandle);
    }

    public int size()
    {
        assert mDeckHandle != 0;
        return size(mDeckHandle);
    }

    public DeckError addCard(String name)
    {
        assert mDeckHandle != 0;
        return DeckError.values()[addCard(mDeckHandle, name)];
    }

    public List<StaticCardData> cards()
    {
        assert mDeckHandle != 0;
        return cards(mDeckHandle);
    }

    public void deleteCard(String name)
    {
        assert mDeckHandle != 0;
        deleteCard(mDeckHandle, name);
    }

    public void remove()
    {
        assert mDeckHandle != 0;
        remove(mDeckHandle);
    }

    private long mDeckHandle = 0;
}
