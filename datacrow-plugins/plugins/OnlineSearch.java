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
import net.datacrow.core.IconLibrary;
import net.datacrow.core.modules.DcModule;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;
import net.datacrow.core.services.OnlineServices;
import net.datacrow.core.services.Servers;

public class OnlineSearch extends Plugin {

	private static final long serialVersionUID = 1119318129008874523L;

	public OnlineSearch(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
        super(dco, template, viewIdx, moduleIdx, viewType);
    } 
    
    @Override
    public boolean isAdminOnly() {
        return false;
    }
    
    @Override
    public boolean isAuthorizable() {
        return true;
    }    
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	DcModule module = getModule();
    	OnlineServices os = Servers.getInstance().getOnlineServices(module.getIndex());
    	GUI.getInstance().getOnlineSearchForm(os, null, null, true).setVisible(true);
    }
    
    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoSearchOnline16;
    }
    
    @Override
    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK);
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public String getLabel() {
        return DcResources.getText("lblOnlineSearch");
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpOnlineSearch");
    }      
}
