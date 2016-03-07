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

import net.datacrow.console.windows.ICalendarExporterDialog;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.UserMode;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;

public class ICalendarExporter extends Plugin {

	private static final long serialVersionUID = 5625627781645588838L;

	public ICalendarExporter(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
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
    public int getXpLevel() {
        return UserMode._XP_EXPERT;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ICalendarExporterDialog dlg = new ICalendarExporterDialog();
        dlg.setVisible(true);
    }  
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoCalendar;
    }

    @Override
    public String getLabel() {
        return DcResources.getText("lblICalendarLoanExport");
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpICalendarLoanExport");
    }      
}
