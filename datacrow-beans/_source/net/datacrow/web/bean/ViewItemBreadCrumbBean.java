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

package net.datacrow.web.bean;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import net.datacrow.web.DcBean;
import net.datacrow.web.model.Item;
import net.datacrow.web.util.DcMenuItem;
import net.datacrow.web.util.WebUtilities;

import org.apache.log4j.Level;
import org.primefaces.event.MenuActionEvent;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

@ManagedBean 
@SessionScoped 
public class ViewItemBreadCrumbBean extends DcBean {

    private static final long serialVersionUID = 1L;
    
    private MenuModel model;
    
    private List<DcMenuItem> items = new ArrayList<DcMenuItem>();

    public void reset() {
        model = new DefaultMenuModel();
        items.clear();
    }
    
    public ViewItemBreadCrumbBean() {
        reset();
    }
    
    public void addItem(Item item) {
        DcMenuItem mi = new DcMenuItem(item);
        
        if (!items.contains(mi)) {
            
            if (items.size() == 0)
                mi.setIcon("ui-icon-home");
            
            mi.setUpdate(":viewItemDetail");
            mi.setOncomplete("PF('viewItemDetail').show()");
            
            mi.setCommand("#{viewItemBreadCrumbBean.selectItem}");
            mi.setId(String.valueOf(items.size()));

            model.addElement(mi);
            items.add(mi);
        } else {
            DefaultMenuModel newModel = new DefaultMenuModel();
            items.clear();
            
            List<MenuElement> elements = model.getElements();
            MenuElement element;
            for (int i = 0; i < elements.size(); i++ ) {
                element = elements.get(i);
                
                newModel.addElement(element);
                items.add((DcMenuItem) element);
                
                if (element.equals(mi))
                    break;
            }
            
            model = newModel;
        }
    }
    
    public MenuModel getModel() {
        return model;
    }
    
    public void selectItem(ActionEvent event) {
        DcMenuItem menuItem = (DcMenuItem) ((MenuActionEvent) event).getMenuItem();
        
        Item item = menuItem.getItem();

        // add the item to the bread crumb
        try {
            ItemBean itemBean = (ItemBean) WebUtilities.getBean("viewItemBean");
            itemBean.setItem(item);
        } catch (Exception e) {
            WebUtilities.log(Level.ERROR_INT, "Could not find / instantiate the Bread Crumb Bean", e);
        }
    }
}
