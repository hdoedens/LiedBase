package org.gkvassenpeelo.liedbase.papermachine;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Body;
import org.docx4j.wml.Br;
import org.docx4j.wml.ObjectFactory;
import org.gkvassenpeelo.liedbase.bible.BiblePartFragment;
import org.gkvassenpeelo.liedbase.liturgy.Liturgy;
import org.gkvassenpeelo.liedbase.liturgy.LiturgyPart;
import org.gkvassenpeelo.liedbase.liturgy.Scripture;
import org.gkvassenpeelo.liedbase.liturgy.SlideContents;
import org.gkvassenpeelo.liedbase.liturgy.Song;
import org.gkvassenpeelo.liedbase.songbook.SongLine;

public class PaperMachine {

	private Liturgy liturgy;

	private MainDocumentPart mainDocumentPart;

	private WordprocessingMLPackage wordMLPackage;

	private List<LiturgyPart.Type> liturgyPartsToPrint = new ArrayList<LiturgyPart.Type>();

	public PaperMachine(Liturgy liturgy) throws PaperMachineException {

		this.liturgy = liturgy;

		liturgyPartsToPrint.add(LiturgyPart.Type.amen);
		liturgyPartsToPrint.add(LiturgyPart.Type.gathering);
		liturgyPartsToPrint.add(LiturgyPart.Type.law);
		liturgyPartsToPrint.add(LiturgyPart.Type.lecture);
		liturgyPartsToPrint.add(LiturgyPart.Type.prair);
		liturgyPartsToPrint.add(LiturgyPart.Type.scripture);
		liturgyPartsToPrint.add(LiturgyPart.Type.song);
		liturgyPartsToPrint.add(LiturgyPart.Type.votum);

	}

	public void createDocument() throws PaperMachineException {
		try {
			wordMLPackage = WordprocessingMLPackage.createPackage();
			mainDocumentPart = wordMLPackage.getMainDocumentPart();

			for (LiturgyPart lp : liturgy.getLiturgyParts()) {
				try {
					addLiturgyPartToDocument(lp);
				} catch (JAXBException e) {
					throw new PaperMachineException(e.getMessage());
				}
			}

		} catch (InvalidFormatException e) {
			throw new PaperMachineException(String.format("Error while creating paper machine: %s", e.getMessage()), e);
		}
	}

	private void addLiturgyPartToDocument(LiturgyPart lp) throws JAXBException {
		if (!liturgyPartsToPrint.contains(lp.getType())) {
			return;
		}

		if (lp.getType() == LiturgyPart.Type.scripture) {

			mainDocumentPart.addStyledParagraphOfText("", lp.getLine());

			StringBuilder sb = new StringBuilder();

			for (SlideContents sc : lp.getSlides()) {
				for (BiblePartFragment fragment : ((Scripture) sc).getBiblePart()) {
					sb.append(fragment.getContent());

					if (fragment.getDisplayType() == BiblePartFragment.DisplayType.superScript) {
						sb.append(" ");
					}
				}
			}

			mainDocumentPart.addParagraphOfText(sb.toString());

			return;
		}

		if (lp.getType() == LiturgyPart.Type.song) {

			mainDocumentPart.addStyledParagraphOfText("", lp.getLine());
			
			for (SlideContents sc : lp.getSlides()) {

				ObjectFactory factory = Context.getWmlObjectFactory();
				// Create the paragraph
				org.docx4j.wml.P para = factory.createP();

				// Create the run
				org.docx4j.wml.R run = factory.createR();
				
				// Add the current verse above the verse text
				org.docx4j.wml.Text t1 = factory.createText();
				t1.setValue(((Song)sc).getVerseNumber());
				run.getRunContent().add(t1);
				Br br1 = factory.createBr(); // this Br element is used break the current and go for next line
				run.getContent().add(br1);

				for (SongLine line : ((Song) sc).getSongText()) {

					// Add the verse line followed by a line break
					org.docx4j.wml.Text t = factory.createText();
					t.setValue(line.getContent());
					run.getRunContent().add(t);

					Br br = factory.createBr(); // this Br element is used break the current and go for next line
					run.getContent().add(br);

				}

				para.getParagraphContent().add(run);
				// Now add our paragraph to the document body
				Body body = mainDocumentPart.getJaxbElement().getBody();
				body.getEGBlockLevelElts().add(para);

			}
		}
	}

	public void save(String location) throws PaperMachineException {
		try {
			wordMLPackage.save(new java.io.File(location));
		} catch (Docx4JException e) {
			throw new PaperMachineException(String.format("Error while saving paper machine: %s", e.getMessage()), e);
		}
	}

}
