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

import net.datacrow.console.windows.log.LogForm;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;

public class Log extends Plugin {

	private static final long serialVersionUID = -7796824520070538427L;

	public Log(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
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
        LogForm lf = new LogForm();
        lf.setVisible(true);
        lf.toFront();
    }
    
    @Override
    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK);
    }
    
    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoEventLog;
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public String getLabel() {
        return DcResources.getText("lblShowLog");
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpShowLog");
    }      
}
