/******************************************************************************
 *                                     __                                     *
 *                              <-----/@@\----->                              *
 *                             <-< <  \\//  > >->                             *
 *                               <-<-\ __ /->->                               *
 *                               Data /  \ Crow                               *
 *                                   ^    ^                                   *
 *                              info@datacrow.net                             *
 *                                                                            *
 *                       This file is part of Data Crow.                      *
 *       Data Crow is free software; you can redistribute it and/or           *
 *        modify it under the terms of the GNU General Public                 *
 *       License as published by the Free Software Foundation; either         *
 *              version 3 of the License, or any later version.               *
 *                                                                            *
 *        Data Crow is distributed in the hope that it will be useful,        *
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *           MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.             *
 *           See the GNU General Public License for more details.             *
 *                                                                            *
 *        You should have received a copy of the GNU General Public           *
 *  License along with this program. If not, see http://www.gnu.org/licenses  *
 *                                                                            *
 ******************************************************************************/

package plugins;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import net.datacrow.console.GUI;
import net.datacrow.console.windows.settings.CardViewSettingsDialog;
import net.datacrow.console.windows.settings.TableViewSettingsDialog;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.console.IMasterView;
import net.datacrow.core.console.IView;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;

import org.apache.log4j.Logger;

public class ViewSettings extends Plugin {

	private static Logger logger = Logger.getLogger(ViewSettings.class.getName());
	
	private static final long serialVersionUID = -8261181858476124190L;

	public ViewSettings(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
        super(dco, template, viewIdx, moduleIdx, viewType);
    }
    
    @Override
    public boolean isAdminOnly() {
        return true;
    }
    
    @Override
    public boolean isAuthorizable() {
        return false;
    }    
    
    @Override
    public void actionPerformed(ActionEvent e) {
        IView view = null;
        
        if (getViewType() == -1) {
            view = GUI.getInstance().getSearchView(getModuleIdx()).getCurrent();
        } else {
            if (getViewType() == _VIEWTYPE_SEARCH)
            	view = GUI.getInstance().getSearchView(getModuleIdx()).get(getViewIdx());
            else if (getViewType() == _VIEWTYPE_INSERT)
            	view = GUI.getInstance().getInsertView(getModuleIdx()).get(getViewIdx());
        }
        
        if (view == null) {
        	logger.error("View settings dialog cannot be opened as the view cannot be resolved");
        } else {
	        if (view.getIndex() == IMasterView._LIST_VIEW) {
	            new CardViewSettingsDialog().setVisible(true);
	        } else if (view.getIndex() == IMasterView._TABLE_VIEW) {
	            new TableViewSettingsDialog().setVisible(true);
	        }
        }
    }
    
    @Override
    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK);
    }
    
    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoSettings16;
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public String getLabel() {
        return DcResources.getText("lblViewSettings");
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpViewSettings");
    }       
}