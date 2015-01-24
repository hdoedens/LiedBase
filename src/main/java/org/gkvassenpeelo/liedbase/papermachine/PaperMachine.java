package org.gkvassenpeelo.liedbase.papermachine;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.gkvassenpeelo.liedbase.liturgy.Liturgy;

public class PaperMachine {

	private Liturgy liturgy;

	private MainDocumentPart mainDocumentPart;

	private WordprocessingMLPackage wordMLPackage;

	public PaperMachine(Liturgy liturgy) throws PaperMachineException {

		this.liturgy = liturgy;

	}

	public void createDocument() throws PaperMachineException {
		try {
			wordMLPackage = WordprocessingMLPackage.createPackage();
			mainDocumentPart = wordMLPackage.getMainDocumentPart();

			mainDocumentPart.addStyledParagraphOfText("Heading 1", liturgy.getLiturgyParts().get(0).getLine());
			mainDocumentPart.addParagraphOfText("Hello Word!");

		} catch (InvalidFormatException e) {
			throw new PaperMachineException(String.format("Error while creating paper machine: %s", e.getMessage()), e);
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
