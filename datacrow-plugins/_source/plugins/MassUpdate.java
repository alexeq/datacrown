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

import javax.swing.ImageIcon;

import net.datacrow.console.windows.ItemSynchronizerDialog;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;
import net.datacrow.core.synchronizers.Synchronizer;
import net.datacrow.core.synchronizers.Synchronizers;

public class MassUpdate extends Plugin {
	
	private static final long serialVersionUID = 1400444469763091661L;

	public MassUpdate(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
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
    	ItemSynchronizerDialog dlg = new ItemSynchronizerDialog(DcModules.getCurrent());
    	dlg.setVisible(true);
    }
    
    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoMassUpdate;
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public String getLabel() {
        
    	if (Synchronizers.getInstance().hasSynchronizer(getModuleIdx())) {
    	    Synchronizer sunchronizer = Synchronizers.getInstance().getSynchronizer(getModuleIdx());
    		return sunchronizer.getTitle();
    	}

		return "";
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpMassUpdate");
    }     
}