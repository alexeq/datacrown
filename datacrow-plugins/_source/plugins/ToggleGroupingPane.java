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

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import net.datacrow.console.GUI;
import net.datacrow.core.DcRepository;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.console.IMasterView;
import net.datacrow.core.modules.DcModule;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;
import net.datacrow.settings.DcSettings;

public class ToggleGroupingPane extends Plugin {

	private static final long serialVersionUID = 2260171553909026479L;

	public ToggleGroupingPane(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
        super(dco, template, viewIdx, moduleIdx, viewType);
    }
    
    @Override
    public boolean isAdminOnly() {
        return false;
    }
    
    @Override
    public boolean isAuthorizable() {
        return false;
    }    
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        boolean b = !DcSettings.getBoolean(DcRepository.Settings.stShowGroupingPanel);
        DcSettings.set(DcRepository.Settings.stShowGroupingPanel, b);
        
        if (ae.getSource() instanceof AbstractButton)
            ((AbstractButton) ae.getSource()).setIcon(getIcon());
        
        IMasterView mv;
        for (DcModule module : DcModules.getModules()) {
        	
        	if (!module.isTopModule()) continue;
        	
        	mv = GUI.getInstance().getSearchView(module.getIndex());
        	
        	if (mv.getCurrent() != null && module.isTopModule()) {
                mv.getCurrent().applyViewDividerLocation();
                mv.applySettings();
            }
        }
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke("F10");
    }
    
    @Override
    public ImageIcon getIcon() {
        return DcSettings.getBoolean(DcRepository.Settings.stShowGroupingPanel) ? IconLibrary._icoOK : null;
    }

    @Override
    public String getLabel() {
        return DcResources.getText("lblToggleGroupingPane");
    } 
}