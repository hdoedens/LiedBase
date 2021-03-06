package org.gkvassenpeelo.liedbase.songbook;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.gkvassenpeelo.liedbase.liturgy.SlideContents;
import org.junit.Test;

public class SongBookTest {

	@Test
	public void getOpwekking194Text() throws UnsupportedEncodingException {
		try {
			List<List<SongLine>> songText = SongBook.getOpwekkingSongTekst("194");

			// assert first line
			assertEquals("U maakt ons één.", songText.get(0).get(0).getContent());
			// assert last line
			assertEquals("Ned. tekst: JmeO",
					songText.get(songText.size() - 1).get(songText.get(songText.size() - 1).size() - 1).getContent());

		} catch (SongBookException e) {
			fail("test failed with error: " + e.getMessage());
		}
	}

	@Test
	public void getOpwekking717Text() {
		try {
			List<List<SongLine>> songText = SongBook.getOpwekkingSongTekst("717");

			// assert first line
			assertEquals("Stil mijn ziel wees stil,", songText.get(0).get(0).getContent());
			// assert last line
			assertEquals("Ik rust in U alleen.",
					songText.get(songText.size() - 1).get(songText.get(songText.size() - 1).size() - 1).getContent());

		} catch (SongBookException e) {
			fail("test failed with error: " + e.getMessage());
		}
	}

	@Test
	public void getGezang111Text() {
		try {
			List<SongLine> songText = SongBook.getSongText(SlideContents.Type.gezang, "111", "1");

			// assert first line
			assertEquals("refrein", songText.get(0).getContent());
			// assert last line
			assertEquals("Hij is de Heer van mijn leven.", songText.get(songText.size() - 1).getContent());

		} catch (SongBookException e) {
			fail("test failed with error: " + e.getMessage());
		}
	}

	@Test
	public void getLevenslied1Text() throws SongBookException {
		List<SongLine> songText = SongBook.getSongText(SlideContents.Type.levenslied, "1", "2");

		// assert first line
		assertEquals("Hij is te vergelijken met een boom,", songText.get(0).getContent());
		// assert last line
		assertEquals("de HEER is hem in alles goedgezind.", songText.get(songText.size() - 1).getContent());
	}

	@Test
	public void readingNextSong() {
		assert (SongBook.readingNextSong(178, "", "gereformeerd kerkboek 179a: 1"));
		assert (SongBook.readingNextSong(178, "", "gereformeerd kerkboek 179b: 1"));
		assert (SongBook.readingNextSong(179, "a", "gereformeerd kerkboek 179b: 1"));
		assert (SongBook.readingNextSong(179, "a", "gereformeerd kerkboek 179c: 1"));
		assert (SongBook.readingNextSong(179, "a", "gereformeerd kerkboek 181b: 1"));
		assert (SongBook.readingNextSong(179, "", "gereformeerd kerkboek 180: 1-5"));
		assert (SongBook.readingNextSong(179, "", "gereformeerd kerkboek 182: 1-5"));
		assert !(SongBook.readingNextSong(179, "", "gereformeerd kerkboek 118: 1-5"));
	}
}
