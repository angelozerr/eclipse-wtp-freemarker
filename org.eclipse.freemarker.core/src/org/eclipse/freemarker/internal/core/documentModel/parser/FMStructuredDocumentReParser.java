package org.eclipse.freemarker.internal.core.documentModel.parser;

import java.util.List;

import org.eclipse.wst.sse.core.internal.provisional.events.RegionChangedEvent;
import org.eclipse.wst.sse.core.internal.provisional.events.StructuredDocumentEvent;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredDocumentRegion;
import org.eclipse.wst.sse.core.internal.provisional.text.IStructuredTextReParser;
import org.eclipse.wst.sse.core.internal.provisional.text.ITextRegion;
import org.eclipse.wst.xml.core.internal.parser.XMLStructuredDocumentReParser;

public class FMStructuredDocumentReParser extends
		XMLStructuredDocumentReParser {

	public FMStructuredDocumentReParser() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.wst.xml.core.internal.parser.XMLStructuredDocumentReParser
	 * #newInstance()
	 */
	public IStructuredTextReParser newInstance() {
		return new FMStructuredDocumentReParser();
	}

	/**
	 * Adding the support to php comments
	 */
	protected StructuredDocumentEvent checkForComments() {
		StructuredDocumentEvent result = checkForCriticalKey("/*"); //$NON-NLS-1$
		if (result == null) {
			result = checkForCriticalKey("*/"); //$NON-NLS-1$
		}
		return result != null ? result : super.checkForComments();
	}

	/**
	 * This function was added in order to support asp tags in PHP (bug fix
	 * #150363)
	 */
	protected StructuredDocumentEvent checkForCrossStructuredDocumentRegionSyntax() {
		StructuredDocumentEvent result = super
				.checkForCrossStructuredDocumentRegionSyntax();
		if (result == null) {
			result = checkForCriticalKey("<%"); //$NON-NLS-1$
			if (result == null)
				result = checkForCriticalKey("%>"); //$NON-NLS-1$

		}
		return result;
	}

	/**
	 * Change PHP Script Regions nodes...
	 */
	protected StructuredDocumentEvent regionCheck(
			IStructuredDocumentRegion oldNode, IStructuredDocumentRegion newNode) {
		final StructuredDocumentEvent event = super.regionCheck(oldNode,
				newNode);

		if (event instanceof RegionChangedEvent) {
			RegionChangedEvent changedEvent = (RegionChangedEvent) event;

			if (changedEvent.getRegion().getType() == FMRegionContext.PHP_CONTENT) {
				oldNode.setRegions(newNode.getRegions());
			}
		}
		return event;
	}

	/**
	 * This implementation updates the php tokens model after updating WST
	 * editor model
	 */
	public StructuredDocumentEvent reparse() {
		final StructuredDocumentEvent documentEvent = super.reparse();
		return documentEvent;
	}

	@Override
	public StructuredDocumentEvent _checkBlockNodeList(List blockTagList) {
		// There are no blocktags that should be checked within PHP script
		// content
		if (dirtyStart.equals(dirtyEnd)) {
			ITextRegion region = dirtyStart.getRegionAtCharacterOffset(fStart);
			if (region != null
					&& region.getType().equals(FMRegionContext.PHP_CONTENT)
					&& (dirtyStart.getStart() + region.getEnd() >= (fStart + fLengthToReplace))) {
				return null;
			}
		}
		return super._checkBlockNodeList(blockTagList);
	}

}
