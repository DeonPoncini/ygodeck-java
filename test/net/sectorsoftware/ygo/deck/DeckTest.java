package net.sectorsoftware.ygo.deck;

import static org.junit.Assert.assertEquals;

import java.io.Closeable;
import java.io.IOException;
import java.lang.RuntimeException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sectorsoftware.ygo.data.DataTypes.DeckType;
import net.sectorsoftware.ygo.data.DataTypes.StaticCardData;
import net.sectorsoftware.ygo.deck.DataTypes.DeckError;

public class DeckTest
{
    static {
        System.loadLibrary("ygodeck-jni");
    }

    static class Deck_Fixture implements Closeable
    {
        public Deck_Fixture()
        {
            DB.setPath(System.getenv("CARD_DB_PATH"));
            main = new Deck(DeckType.MAIN);
            side = new Deck(DeckType.SIDE);
            extra = new Deck(DeckType.EXTRA);
        }

        @Override
        public void close() throws IOException
        {
            main.remove();
            side.remove();
            extra.remove();
            main.delete();
            side.delete();
            extra.delete();
        }

    }

    private Deck_Fixture fixture;
    private static Deck main;
    private static Deck side;
    private static Deck extra;

    @Before
    public void setup()
    {
        fixture = new Deck_Fixture();
    }

    @After
    public void tearDown() throws IOException
    {
        fixture.close();
        fixture = null;
    }

    @Test
    public void AddCard()
    {
        DeckError mainErr = main.addCard("Blue-Eyes White Dragon");
        assertEquals(mainErr, DeckError.OK);
        List<StaticCardData> mainCards = main.cards();
        assertEquals(mainCards.size(), 1);
        assertEquals(mainCards.get(0).name, "Blue-Eyes White Dragon");

        DeckError sideErr = side.addCard("Mirror Force");
        assertEquals(sideErr, DeckError.OK);
        List<StaticCardData> sideCards = side.cards();
        assertEquals(sideCards.size(), 1);
        assertEquals(sideCards.get(0).name, "Mirror Force");

        DeckError extraErr = extra.addCard("Stardust Dragon");
        assertEquals(extraErr, DeckError.OK);
        List<StaticCardData> extraCards = extra.cards();
        assertEquals(extraCards.size(), 1);
        assertEquals(extraCards.get(0).name, "Stardust Dragon");
    }

    @Test
    public void DeleteCard()
    {
        main.addCard("Evilswarm Castor");
        List<StaticCardData> mainCards = main.cards();
        assertEquals(mainCards.size(), 1);
        main.deleteCard("Evilswarm Castor");
        mainCards = main.cards();
        assertEquals(mainCards.size(), 0);

        side.addCard("Pot of Duality");
        List<StaticCardData> sideCards = side.cards();
        assertEquals(sideCards.size(), 1);
        side.deleteCard("Pot of Duality");
        sideCards = side.cards();
        assertEquals(sideCards.size(), 0);

        extra.addCard("Abyss Dweller");
        List<StaticCardData> extraCards = extra.cards();
        assertEquals(extraCards.size(), 1);
        extra.deleteCard("Abyss Dweller");
        extraCards = extra.cards();
        assertEquals(extraCards.size(), 0);
    }

    @Test
    public void DeleteNotPresentCard()
    {
        main.addCard("Evilswarm Castor");
        List<StaticCardData> mainCards = main.cards();
        assertEquals(mainCards.size(), 1);
        main.deleteCard("Blue-Eyes White Dragon");
        mainCards = main.cards();
        assertEquals(mainCards.size(), 1);

        side.addCard("Pot of Duality");
        List<StaticCardData> sideCards = side.cards();
        assertEquals(sideCards.size(), 1);
        side.deleteCard("Pot of Greed");
        sideCards = side.cards();
        assertEquals(sideCards.size(), 1);

        extra.addCard("Abyss Dweller");
        List<StaticCardData> extraCards = extra.cards();
        assertEquals(extraCards.size(), 1);
        extra.deleteCard("Gagaga Cowboy");
        extraCards = extra.cards();
        assertEquals(extraCards.size(), 1);
    }

    @Test
    public void DeckLimits()
    {
        main.addCard("Evilswarm Castor");
        main.addCard("Evilswarm Castor");
        main.addCard("Evilswarm Castor");
        main.addCard("Evilswarm Heliotrope");
        main.addCard("Evilswarm Heliotrope");
        main.addCard("Evilswarm Heliotrope");
        main.addCard("Evilswarm Mandragora");
        main.addCard("Evilswarm Mandragora");
        main.addCard("Evilswarm Mandragora");
        main.addCard("Gravekeeper's Commandant");
        main.addCard("Gravekeeper's Commandant");
        main.addCard("Gravekeeper's Descendant");
        main.addCard("Gravekeeper's Spy");
        main.addCard("Gravekeeper's Spy");
        main.addCard("Gravekeeper's Spy");
        main.addCard("Rescue Rabbit");
        main.addCard("Thunder King Rai-Oh");
        main.addCard("Allure of Darkness");
        main.addCard("Dark Hole");
        main.addCard("Infestation Pandemic");
        main.addCard("Infestation Pandemic");
        main.addCard("Necrovalley");
        main.addCard("Necrovalley");
        main.addCard("Necrovalley");
        main.addCard("Pot of Duality");
        main.addCard("Pot of Duality");
        main.addCard("Reinforcement of the Army");
        main.addCard("Reinforcement of the Army");
        main.addCard("Bottomless Trap Hole");
        main.addCard("Compulsory Evacuation Device");
        main.addCard("Dimensional Prison");
        main.addCard("Dimensional Prison");
        main.addCard("Dimensional Prison");
        main.addCard("Fiendish Chain");
        main.addCard("Fiendish Chain");
        main.addCard("Infestation Infection");
        main.addCard("Solemn Warning");
        main.addCard("Torrential Tribute");
        main.addCard("Wiretap");
        main.addCard("Wiretap");
        List<StaticCardData> mainCards = main.cards();
        assertEquals(mainCards.size(), 40);

        extra.addCard("Abyss Dweller");
        extra.addCard("Cairngorgon, Antiluminescent Knight");
        extra.addCard("Evilswarm Bahamut");
        extra.addCard("Evilswarm Exciton Knight");
        extra.addCard("Evilswarm Ophion");
        extra.addCard("Evilswarm Ophion");
        extra.addCard("Evilswarm Ouroboros");
        extra.addCard("Evilswarm Thanatos");
        extra.addCard("Gagaga Cowboy");
        extra.addCard("Maestroke the Symphony Djinn");
        extra.addCard("Number 101: Silent Honor ARK");
        extra.addCard("Number 101: Silent Honor ARK");
        extra.addCard("Number 103: Ragnazero");
        extra.addCard("Number 66: Master Key Beetle");
        extra.addCard("Number 82: Heartlandraco");
        List<StaticCardData> extraCards = extra.cards();
        assertEquals(extraCards.size(), 15);

        side.addCard("Trap Hole");
        side.addCard("White Hole");
        side.addCard("Debunk");
        side.addCard("Debunk");
        side.addCard("Mirror Force");
        side.addCard("Mirror Force");
        side.addCard("Mirror Force");
        side.addCard("Evilswarm Mandragora");
        side.addCard("Mind Control");
        side.addCard("Soul Release");
        side.addCard("Spiritualism");
        side.addCard("Spiritualism");
        side.addCard("Vanity's Emptiness");
        side.addCard("Vanity's Emptiness");
        side.addCard("Vanity's Emptiness");
        List<StaticCardData> sideCards = side.cards();
        assertEquals(sideCards.size(), 15);

        main.addCard("Archfiend Heiress");
        main.addCard("Armageddon Knight");
        main.addCard("Dark Grepher");
        main.addCard("Dark Grepher");
        main.addCard("Infernity Archfiend");
        main.addCard("Infernity Archfiend");
        main.addCard("Infernity Archfiend");
        main.addCard("Infernity Necromancer");
        main.addCard("Infernity Necromancer");
        main.addCard("Stygian Street Patrol");
        main.addCard("Stygian Street Patrol");
        main.addCard("Stygian Street Patrol");
        main.addCard("Summoner Monk");
        main.addCard("Summoner Monk");
        main.addCard("Infernity Barrier");
        main.addCard("Infernity Break");
        main.addCard("Infernity Break");
        main.addCard("Infernity Break");
        main.addCard("Trap Stun");
        main.addCard("Trap Stun");
        mainCards = main.cards();
        assertEquals(mainCards.size(), 60);

        DeckError mainErr = main.addCard("Trap Stun");
        assertEquals(mainErr,  DeckError.DECK_FULL);
        mainCards = main.cards();
        assertEquals(mainCards.size(), 60);

        DeckError extraErr = extra.addCard("Stardust Dragon");
        assertEquals(extraErr, DeckError.DECK_FULL);
        extraCards = extra.cards();
        assertEquals(extraCards.size(), 15);

        DeckError sideErr = side.addCard("Magic Cylinder");
        assertEquals(sideErr, DeckError.DECK_FULL);
        sideCards = side.cards();
        assertEquals(sideCards.size(), 15);
    }
}
