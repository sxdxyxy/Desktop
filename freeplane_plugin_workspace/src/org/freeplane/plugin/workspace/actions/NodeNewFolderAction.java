/**
 * author: Marcel Genzmehr
 * 11.11.2011
 */
package org.freeplane.plugin.workspace.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import org.freeplane.core.ui.components.UITools;
import org.freeplane.core.util.TextUtils;
import org.freeplane.plugin.workspace.WorkspaceController;
import org.freeplane.plugin.workspace.WorkspaceUtils;
import org.freeplane.plugin.workspace.components.dialog.WorkspaceNewFolderPanel;
import org.freeplane.plugin.workspace.io.IFileSystemRepresentation;
import org.freeplane.plugin.workspace.model.AWorkspaceTreeNode;
import org.freeplane.plugin.workspace.nodes.FolderLinkNode;
import org.freeplane.plugin.workspace.nodes.FolderVirtualNode;


public class NodeNewFolderAction extends AWorkspaceAction {

	private static final long serialVersionUID = 6126361617680877866L;

	
	/***********************************************************************************
	 * CONSTRUCTORS
	 **********************************************************************************/

	public NodeNewFolderAction() {
		super("workspace.action.node.new.folder");
	}

	/***********************************************************************************
	 * METHODS
	 **********************************************************************************/
	
	/***********************************************************************************
	 * REQUIRED METHODS FOR INTERFACES
	 **********************************************************************************/
	
	public void actionPerformed(ActionEvent e) {
		AWorkspaceTreeNode targetNode = getNodeFromActionEvent(e);
		if(targetNode == null) {
			targetNode = (AWorkspaceTreeNode) WorkspaceUtils.getModel().getRoot();
		}

		int mode = WorkspaceNewFolderPanel.MODE_VIRTUAL_PHYSICAL;
		if(targetNode instanceof IFileSystemRepresentation) {
			mode = WorkspaceNewFolderPanel.MODE_VIRTUAL_ONLY;
		}
		WorkspaceNewFolderPanel dialog = new WorkspaceNewFolderPanel(mode , targetNode);
		int response = JOptionPane.showConfirmDialog(UITools.getFrame(), dialog, TextUtils.getText("workspace.action.node.new.folder.dialog.title"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		if(response == JOptionPane.OK_OPTION) {
			String value = dialog.getFolderName();
			if(value == null || value.trim().length() <= 0) {
				//DOCEAR - prepare message, or call this method (with error message) again? 
				return;
			}
			if(dialog.isLinkedFolder()) {
				File path = new File(dialog.getLinkPath());
				if (path != null) {
					FolderLinkNode node = new FolderLinkNode();				
					node.setName(value);			
					node.setPath(WorkspaceUtils.getWorkspaceRelativeURI(path));
					WorkspaceUtils.getModel().addNodeTo(node, targetNode);					
					node.refresh();
				}				
			}
			else {
				if(targetNode instanceof IFileSystemRepresentation) {
					try {
						WorkspaceController.getController().getFilesystemMgr().createDirectory(value, ((IFileSystemRepresentation) targetNode).getFile());
					}
					catch (IOException e1) {
						JOptionPane.showMessageDialog(UITools.getFrame(), e1.getMessage());
					}
				}
				else {
					FolderVirtualNode node = new FolderVirtualNode();
					node.setName(value);
					WorkspaceUtils.getModel().addNodeTo(node, targetNode);
				}
					
			}
			WorkspaceUtils.saveCurrentConfiguration();
			targetNode.refresh();
			
		}
	}
}