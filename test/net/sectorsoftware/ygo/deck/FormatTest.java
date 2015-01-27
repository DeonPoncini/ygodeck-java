package net.sectorsoftware.ygo.deck;

import static org.junit.Assert.assertEquals;

import java.io.Closeable;
import java.io.IOException;
import java.lang.RuntimeException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import net.sectorsoftware.ygo.data.DataTypes;

public class FormatTest
{
    static {
        System.loadLibrary("ygodeck-jni");
    }

    static class Format_Fixture implements Closeable
    {
        public Format_Fixture()
        {
            DB.setPath(System.getenv("CARD_DB_PATH"));
        }

        @Override
        public void close() throws IOException
        {
        }

    }

    private Format_Fixture fixture;

    @Before
    public void setup()
    {
        fixture = new Format_Fixture();
    }

    @After
    public void tearDown() throws IOException
    {
        fixture.close();
        fixture = null;
    }

    @Test
    public void Create()
    {
        List<String> formatDates = Format.formatDates();
        for (String f : formatDates) {
            Format formatA = new Format(DataTypes.Format.ADVANCED, f);
            assertEquals(formatA.formatDate(), f);
            assertEquals(formatA.format(), DataTypes.Format.ADVANCED);
            formatA.delete();

            Format formatT = new Format(DataTypes.Format.TRADITIONAL, f);
            assertEquals(formatT.formatDate(), f);
            assertEquals(formatT.format(), DataTypes.Format.TRADITIONAL);
            formatT.delete();
        }
    }

    @Test
    public void Invalid()
    {
        try {
            Format format = new Format(DataTypes.Format.ADVANCED, "InvalidFormat");
            assertEquals(0, 1);
        } catch (RuntimeException e) {
            assertEquals(1, 1);
        }
    }

    @Test
    public void Limits()
    {
        Format formatA = new Format(DataTypes.Format.ADVANCED, "April 2004");
        Format formatT = new Format(DataTypes.Format.TRADITIONAL, "April 2004");

        assertEquals(0, formatA.cardCount("Change of Heart"));
        assertEquals(1, formatT.cardCount("Change of Heart"));

        assertEquals(1, formatA.cardCount("Mage Power"));
        assertEquals(1, formatT.cardCount("Mage Power"));

        assertEquals(2, formatA.cardCount("Creature Swap"));
        assertEquals(2, formatT.cardCount("Creature Swap"));

        assertEquals(3, formatA.cardCount("Kuriboh"));
        assertEquals(3, formatT.cardCount("Kuriboh"));

        formatA.delete();
        formatT.delete();
    }

}
