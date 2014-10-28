package org.eclipse.freemarker.internal.ui.editor.configuration;

import org.eclipse.freemarker.internal.core.documentModel.partitioner.FMPartitionTypes;
import org.eclipse.freemarker.internal.core.documentModel.partitioner.FMStructuredTextPartitioner;
import org.eclipse.freemarker.internal.ui.editor.highlighter.LineStyleProviderForFM;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.wst.html.core.internal.text.StructuredTextPartitionerForHTML;
import org.eclipse.wst.html.ui.StructuredTextViewerConfigurationHTML;
import org.eclipse.wst.sse.core.text.IStructuredPartitions;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.xml.core.internal.text.rules.StructuredTextPartitionerForXML;

public class FMStructuredTextViewerConfiguration extends
		StructuredTextViewerConfigurationHTML {

	private String[] configuredContentTypes;
	private LineStyleProvider fLineStyleProvider;

	/*
	 * Returns an array of all the contentTypes (partition names) supported by
	 * this editor. They include all those supported by HTML editor plus
	 * Freemarker.
	 */
	@Override
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		if (configuredContentTypes == null) {
			String[] phpTypes = FMStructuredTextPartitioner
					.getConfiguredContentTypes();
			String[] xmlTypes = StructuredTextPartitionerForXML
					.getConfiguredContentTypes();
			String[] htmlTypes = StructuredTextPartitionerForHTML
					.getConfiguredContentTypes();
			configuredContentTypes = new String[2 + phpTypes.length
					+ xmlTypes.length + htmlTypes.length];

			configuredContentTypes[0] = IStructuredPartitions.DEFAULT_PARTITION;
			configuredContentTypes[1] = IStructuredPartitions.UNKNOWN_PARTITION;

			int index = 0;
			System.arraycopy(phpTypes, 0, configuredContentTypes, index += 2,
					phpTypes.length);
			System.arraycopy(xmlTypes, 0, configuredContentTypes,
					index += phpTypes.length, xmlTypes.length);
			System.arraycopy(htmlTypes, 0, configuredContentTypes,
					index += xmlTypes.length, htmlTypes.length);
		}

		return configuredContentTypes;
	}
	
	public LineStyleProvider getLineStyleProvider() {
		if (fLineStyleProvider == null) {
			fLineStyleProvider = new LineStyleProviderForFM();
		}
		return fLineStyleProvider;
	}

	@Override
	public LineStyleProvider[] getLineStyleProviders(
			ISourceViewer sourceViewer, String partitionType) {
		if (partitionType == FMPartitionTypes.FM_DEFAULT) {
			return new LineStyleProvider[] { getLineStyleProvider() };
		}
		return super.getLineStyleProviders(sourceViewer, partitionType);
	}
}
