package org.docear.plugin.services.features.documentretrieval;

import org.docear.plugin.services.features.documentretrieval.recommendations.RecommendationsController;
import org.freeplane.core.ui.ribbon.event.AboutToPerformEvent;
import org.freeplane.core.ui.ribbon.event.IActionEventListener;

public class RibbonActionEventListener implements IActionEventListener {

	public void aboutToPerform(AboutToPerformEvent event) {
		RecommendationsController.getController().closeDocumentView();		
	}
	
}
