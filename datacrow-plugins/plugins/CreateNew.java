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

import net.datacrow.console.windows.itemforms.ItemForm;
import net.datacrow.console.windows.security.UserForm;
import net.datacrow.core.DcConfig;
import net.datacrow.core.IconLibrary;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.DcTemplate;
import net.datacrow.core.plugin.Plugin;
import net.datacrow.core.resources.DcResources;
import net.datacrow.core.server.Connector;

public class CreateNew extends Plugin {

	private static final long serialVersionUID = 8447703968873760428L;

	public CreateNew(DcObject dco, DcTemplate template, int viewIdx, int moduleIdx, int viewType) {
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
	public boolean isEnabled() {
        Connector connector = DcConfig.getInstance().getConnector();
		return connector.getUser().isEditingAllowed(getModule());
	}    
    
    @Override
    public KeyStroke getKeyStroke() {
        return KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        DcObject dco = getModule().getItem();
        
        ItemForm itemForm;
        if (getModule().getIndex() == DcModules._USER)
            itemForm = new UserForm(false, dco, false, true);
        else
            itemForm = new ItemForm(getTemplate(), false, false, dco, true);

        itemForm.setVisible(true);
        clear();
    }
    
    @Override
    public ImageIcon getIcon() {
        return IconLibrary._icoAdd;
    }
    
    @Override
    public boolean isSystemPlugin() {
        return true;
    }

    @Override
    public String getLabel() {
        DcTemplate template = getTemplate();
        if (template != null)
            return DcResources.getText("lblNewItemTemplate", 
                                       new String[] {getModule().getObjectName(), 
                                                     template.getTemplateName()});

        return DcResources.getText("lblNewItem", getModule().getObjectName());
    }
    
    @Override
    public String getLabelShort() {
        return DcResources.getText("lblNewItem", "");
    }
    
    @Override
    public String getHelpText() {
        return DcResources.getText("tpNewItem");
    }    
}
