package net.sectorsoftware.ygo.deck;

import static org.junit.Assert.assertEquals;

import java.io.Closeable;
import java.io.IOException;
import java.lang.RuntimeException;
import java.util.Map;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sectorsoftware.ygo.data.DataTypes;
import net.sectorsoftware.ygo.data.DataTypes.DeckType;
import net.sectorsoftware.ygo.data.DataTypes.StaticCardData;
import net.sectorsoftware.ygo.deck.DataTypes.DeckError;

public class DeckSetTest
{
    static {
        System.loadLibrary("ygodeck-jni");
    }

    static class DeckSet_Fixture implements Closeable
    {
        public DeckSet_Fixture()
        {
            DB.setPath(System.getenv("CARD_DB_PATH"));
            User user = new User("DeckSetUser", true);
            Format format = new Format(DataTypes.Format.ADVANCED, "April 2004");
            deckSet = new DeckSet("DeckSetTest", user, format, true);
            user.delete();
            format.delete();
        }

        @Override
        public void close() throws IOException
        {
            deckSet.remove();
            deckSet.delete();
        }

    }

    private DeckSet_Fixture fixture;
    private static DeckSet deckSet;

    @Before
    public void setup()
    {
        fixture = new DeckSet_Fixture();
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
        DeckError mainErr = deckSet.addCard(DeckType.MAIN,
                "Blue-Eyes White Dragon");
        assertEquals(mainErr, DeckError.OK);

        DeckError sideErr = deckSet.addCard(DeckType.SIDE, "Mirror Force");
        assertEquals(sideErr, DeckError.OK);

        DeckError extraErr = deckSet.addCard(DeckType.EXTRA, "Stardust Dragon");
        assertEquals(extraErr, DeckError.OK);

        Map<DeckType, List<StaticCardData>> cards = deckSet.cards();
        List<StaticCardData> main = cards.get(DeckType.MAIN);
        List<StaticCardData> side = cards.get(DeckType.SIDE);
        List<StaticCardData> extra = cards.get(DeckType.EXTRA);

        assertEquals(main.size(), 1);
        assertEquals(main.get(0).name, "Blue-Eyes White Dragon");
        assertEquals(side.size(), 1);
        assertEquals(side.get(0).name, "Mirror Force");
        assertEquals(extra.size(), 1);
        assertEquals(extra.get(0).name, "Stardust Dragon");
    }

    @Test
    public void DeleteCard()
    {
        deckSet.addCard(DeckType.MAIN, "Evilswarm Castor");
        deckSet.addCard(DeckType.SIDE, "Pot of Duality");
        deckSet.addCard(DeckType.EXTRA, "Abyss Dweller");

        Map<DeckType, List<StaticCardData>> cards = deckSet.cards();
        List<StaticCardData> main = cards.get(DeckType.MAIN);
        List<StaticCardData> side = cards.get(DeckType.SIDE);
        List<StaticCardData> extra = cards.get(DeckType.EXTRA);
        assertEquals(main.size(), 1);
        assertEquals(side.size(), 1);
        assertEquals(extra.size(), 1);

        deckSet.deleteCard(DeckType.MAIN, "Evilswarm Castor");
        deckSet.deleteCard(DeckType.SIDE, "Pot of Duality");
        deckSet.deleteCard(DeckType.EXTRA, "Abyss Dweller");

        cards = deckSet.cards();
        main = cards.get(DeckType.MAIN);
        side = cards.get(DeckType.SIDE);
        extra = cards.get(DeckType.EXTRA);
        assertEquals(main.size(), 0);
        assertEquals(side.size(), 0);
        assertEquals(extra.size(), 0);
    }

    @Test
    public void DeleteNotPresentCard()
    {
        deckSet.addCard(DeckType.MAIN, "Evilswarm Castor");
        deckSet.addCard(DeckType.SIDE, "Pot of Duality");
        deckSet.addCard(DeckType.EXTRA, "Abyss Dweller");

        Map<DeckType, List<StaticCardData>> cards = deckSet.cards();
        List<StaticCardData> main = cards.get(DeckType.MAIN);
        List<StaticCardData> side = cards.get(DeckType.SIDE);
        List<StaticCardData> extra = cards.get(DeckType.EXTRA);
        assertEquals(main.size(), 1);
        assertEquals(side.size(), 1);
        assertEquals(extra.size(), 1);

        deckSet.deleteCard(DeckType.MAIN, "Blue-Eyes White Dragon");
        deckSet.deleteCard(DeckType.SIDE, "Pot of Greed");
        deckSet.deleteCard(DeckType.EXTRA, "Gagaga Cowboy");

        cards = deckSet.cards();
        main = cards.get(DeckType.MAIN);
        side = cards.get(DeckType.SIDE);
        extra = cards.get(DeckType.EXTRA);
        assertEquals(main.size(), 1);
        assertEquals(side.size(), 1);
        assertEquals(extra.size(), 1);
    }

    @Test
    public void Validate()
    {
        deckSet.addCard(DeckType.MAIN, "Evilswarm Castor");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Castor");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Castor");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Heliotrope");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Heliotrope");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Heliotrope");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Mandragora");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Mandragora");
        deckSet.addCard(DeckType.MAIN, "Evilswarm Mandragora");
        deckSet.addCard(DeckType.MAIN, "Gravekeeper's Commandant");
        deckSet.addCard(DeckType.MAIN, "Gravekeeper's Commandant");
        deckSet.addCard(DeckType.MAIN, "Gravekeeper's Descendant");
        deckSet.addCard(DeckType.MAIN, "Gravekeeper's Spy");
        deckSet.addCard(DeckType.MAIN, "Gravekeeper's Spy");
        deckSet.addCard(DeckType.MAIN, "Gravekeeper's Spy");
        deckSet.addCard(DeckType.MAIN, "Rescue Rabbit");
        deckSet.addCard(DeckType.MAIN, "Thunder King Rai-Oh");
        deckSet.addCard(DeckType.MAIN, "Allure of Darkness");

        assertEquals(deckSet.validate(), false);

        deckSet.addCard(DeckType.MAIN, "Dark Hole");
        deckSet.addCard(DeckType.MAIN, "Infestation Pandemic");
        deckSet.addCard(DeckType.MAIN, "Infestation Pandemic");
        deckSet.addCard(DeckType.MAIN, "Necrovalley");
        deckSet.addCard(DeckType.MAIN, "Necrovalley");
        deckSet.addCard(DeckType.MAIN, "Necrovalley");
        deckSet.addCard(DeckType.MAIN, "Pot of Duality");
        deckSet.addCard(DeckType.MAIN, "Pot of Duality");
        deckSet.addCard(DeckType.MAIN, "Reinforcement of the Army");
        deckSet.addCard(DeckType.MAIN, "Reinforcement of the Army");
        deckSet.addCard(DeckType.MAIN, "Bottomless Trap Hole");
        deckSet.addCard(DeckType.MAIN, "Compulsory Evacuation Device");
        deckSet.addCard(DeckType.MAIN, "Dimensional Prison");
        deckSet.addCard(DeckType.MAIN, "Dimensional Prison");
        deckSet.addCard(DeckType.MAIN, "Dimensional Prison");
        deckSet.addCard(DeckType.MAIN, "Fiendish Chain");
        deckSet.addCard(DeckType.MAIN, "Fiendish Chain");
        deckSet.addCard(DeckType.MAIN, "Infestation Infection");
        deckSet.addCard(DeckType.MAIN, "Solemn Warning");
        deckSet.addCard(DeckType.MAIN, "Torrential Tribute");
        deckSet.addCard(DeckType.MAIN, "Wiretap");
        deckSet.addCard(DeckType.MAIN, "Wiretap");

        assertEquals(deckSet.validate(), true);

        deckSet.addCard(DeckType.EXTRA, "Abyss Dweller");
        deckSet.addCard(DeckType.EXTRA, "Cairngorgon, Antiluminescent Knight");
        deckSet.addCard(DeckType.EXTRA, "Evilswarm Bahamut");
        deckSet.addCard(DeckType.EXTRA, "Evilswarm Exciton Knight");
        deckSet.addCard(DeckType.EXTRA, "Evilswarm Ophion");
        deckSet.addCard(DeckType.EXTRA, "Evilswarm Ophion");
        deckSet.addCard(DeckType.EXTRA, "Evilswarm Ouroboros");
        deckSet.addCard(DeckType.EXTRA, "Evilswarm Thanatos");
        deckSet.addCard(DeckType.EXTRA, "Gagaga Cowboy");
        deckSet.addCard(DeckType.EXTRA, "Maestroke the Symphony Djinn");
        deckSet.addCard(DeckType.EXTRA, "Number 101: Silent Honor ARK");
        deckSet.addCard(DeckType.EXTRA, "Number 101: Silent Honor ARK");
        deckSet.addCard(DeckType.EXTRA, "Number 103: Ragnazero");
        deckSet.addCard(DeckType.EXTRA, "Number 66: Master Key Beetle");
        deckSet.addCard(DeckType.EXTRA, "Number 82: Heartlandraco");

        assertEquals(deckSet.validate(), true);

        deckSet.addCard(DeckType.SIDE, "Trap Hole");
        deckSet.addCard(DeckType.SIDE, "White Hole");
        deckSet.addCard(DeckType.SIDE, "Debunk");
        deckSet.addCard(DeckType.SIDE, "Debunk");
        deckSet.addCard(DeckType.SIDE, "Mirror Force");
        deckSet.addCard(DeckType.SIDE, "Mirror Force");
        deckSet.addCard(DeckType.SIDE, "Mirror Force");
        deckSet.addCard(DeckType.SIDE, "Evilswarm Mandragora");
        deckSet.addCard(DeckType.SIDE, "Mind Control");
        deckSet.addCard(DeckType.SIDE, "Soul Release");
        deckSet.addCard(DeckType.SIDE, "Spiritualism");
        deckSet.addCard(DeckType.SIDE, "Spiritualism");
        deckSet.addCard(DeckType.SIDE, "Vanity's Emptiness");
        deckSet.addCard(DeckType.SIDE, "Vanity's Emptiness");
        deckSet.addCard(DeckType.SIDE, "Vanity's Emptiness");

        assertEquals(deckSet.validate(), true);

        deckSet.addCard(DeckType.MAIN, "Archfiend Heiress");
        deckSet.addCard(DeckType.MAIN, "Armageddon Knight");
        deckSet.addCard(DeckType.MAIN, "Dark Grepher");
        deckSet.addCard(DeckType.MAIN, "Dark Grepher");
        deckSet.addCard(DeckType.MAIN, "Infernity Archfiend");
        deckSet.addCard(DeckType.MAIN, "Infernity Archfiend");
        deckSet.addCard(DeckType.MAIN, "Infernity Archfiend");
        deckSet.addCard(DeckType.MAIN, "Infernity Necromancer");
        deckSet.addCard(DeckType.MAIN, "Infernity Necromancer");
        deckSet.addCard(DeckType.MAIN, "Stygian Street Patrol");
        deckSet.addCard(DeckType.MAIN, "Stygian Street Patrol");
        deckSet.addCard(DeckType.MAIN, "Stygian Street Patrol");
        deckSet.addCard(DeckType.MAIN, "Summoner Monk");
        deckSet.addCard(DeckType.MAIN, "Summoner Monk");
        deckSet.addCard(DeckType.MAIN, "Infernity Barrier");
        deckSet.addCard(DeckType.MAIN, "Infernity Break");
        deckSet.addCard(DeckType.MAIN, "Infernity Break");
        deckSet.addCard(DeckType.MAIN, "Infernity Break");
        deckSet.addCard(DeckType.MAIN, "Trap Stun");
        deckSet.addCard(DeckType.MAIN, "Trap Stun");

        assertEquals(deckSet.validate(), true);
    }
}
