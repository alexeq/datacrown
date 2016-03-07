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

import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import net.datacrow.console.windows.DcDialog;
import net.datacrow.console.windows.DcFrame;
import net.datacrow.console.windows.help.HelpDialog;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;

public class Help extends Plugin {

	private static final long serialVersionUID = -1064150561616268562L;

	public Help(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
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
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        
        while (!(o instanceof Window) && o != null)
            o = ((Component) o).getParent();
        
        if (o != null) { 
            if (o instanceof DcFrame)
                HelpDialog.setHelpIndex(((DcFrame) o).getHelpIndex());
            else if (o instanceof DcDialog)
                HelpDialog.setHelpIndex(((DcDialog) o).getHelpIndex());
        }
        
        new HelpDialog((Window) o);
    }
    
    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoHelp;
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke("F1");
    }
    
    @Override
    public String getLabel() {
        return DcResources.getText("lblHelp");
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpHelp");
    }
}
