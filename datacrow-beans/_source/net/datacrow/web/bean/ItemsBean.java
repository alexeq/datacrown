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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import net.datacrow.core.DcConfig;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.server.Connector;
import net.datacrow.web.DcBean;
import net.datacrow.web.model.Item;
import net.datacrow.web.model.Items;
import net.datacrow.web.util.WebUtilities;

import org.apache.log4j.Level;
import org.primefaces.event.SelectEvent;

@ManagedBean
@SessionScoped
public class ItemsBean extends DcBean {

    private static final long serialVersionUID = 1L;
	
    private Items items = new Items();
    
    // The selected item. Note that this is passed through to the view or edit bean when 
    // the user makes a selection
    private Item selectedItem;
    
    private int moduleIdx;
    
    private List<Item> selectedItems = new ArrayList<Item>();
    private Map<Integer, Items> itemMap = new HashMap<Integer, Items>();
    
    public ItemsBean() {
        initialize();
    }
    
    private void initialize() {
        ELContext elContext = FacesContext.getCurrentInstance().getELContext();
        ModulesBean modulesBean = (ModulesBean) elContext.getELResolver().getValue(elContext, null, "modulesBean");
        setSelectedModule(modulesBean.getSelectedModuleIdx());
        changeModule(moduleIdx);
    }
    
    public void reset() {
        itemMap.clear();
        selectedItems.clear();
        items = null;
        selectedItem = null;
        initialize();
    }

    public void setSelectedModule(int moduleIdx) {
        changeModule(moduleIdx);
    }
    
    public String changeModule(int moduleIdx) {
        
        if (!isUserLoggedOn() || !isAuthorized(DcModules.get(moduleIdx)))
            return "/login?faces-redirect=true";
        
        this.selectedItem = null;
	    this.moduleIdx = moduleIdx;
	    
	    items = itemMap.get(Integer.valueOf(moduleIdx)); 
	    
	    if (items == null) {
	        items = new Items(moduleIdx);
	        items.search();
	        itemMap.put(Integer.valueOf(moduleIdx), items);
	    }
	    
	    return "/index";
    }
    
    public String reload() {
        search();
        return "/index?faces-redirect=true";
    }
    
    public String search() {
        if (!isUserLoggedOn() || !isAuthorized(DcModules.get(moduleIdx)))
            return "/login?faces-redirect=true";
        
        items.search();
        return "/index";
    }
    
    public List<Item> getSelectedItems() {
        return selectedItems;
    }
    
    public void setSelectedItems(List<Item> selectedItems) {
        this.selectedItems = selectedItems;

        if (selectedItems != null && selectedItems.size() > 0) {
            Item item = selectedItems.get(0);
            setSelectedItem(item);
        }
    }
    
    public Item getSelectedItem() {
        try {
            return selectedItem;
        } catch (Exception e) {
            WebUtilities.log(Level.ERROR_INT, e);
        }
        return null;
    }
    
    public void startViewItem() {
        try {
            ViewItemBreadCrumbBean breadCrumb = (ViewItemBreadCrumbBean) WebUtilities.getBean("viewItemBreadCrumbBean");
            breadCrumb.reset();
        } catch (Exception e) {
            WebUtilities.log(Level.ERROR_INT, "Could not find / instantiate the Bread Crumb Bean", e);
        }
        
        // default is view (on row select)
        try {
            ItemBean itemBean = (ItemBean) WebUtilities.getBean("viewItemBean");
            
            Connector conn = DcConfig.getInstance().getConnector();
            DcObject dco = conn.getItem(selectedItem.getModuleIdx(), selectedItem.getID(), null);
            
            itemBean.setItem(new Item(dco));
        } catch (Exception e) {
            WebUtilities.log(Level.ERROR_INT, e);
        }
    }
    
    public void startEditItem() {
        
        try {
            EditItemBreadCrumbBean breadCrumb = (EditItemBreadCrumbBean) WebUtilities.getBean("editItemBreadCrumbBean");
            breadCrumb.reset();
        } catch (Exception e) {
            WebUtilities.log(Level.ERROR_INT, "Could not find / instantiate the Bread Crumb Bean", e);
        }
        
        try {
            ItemBean itemBean = (ItemBean) WebUtilities.getBean("editItemBean");
            
            Connector conn = DcConfig.getInstance().getConnector();
            DcObject dco = conn.getItem(selectedItem.getModuleIdx(), selectedItem.getID(), null);
            
            itemBean.setItem(new Item(dco));
        } catch (Exception e) {
            WebUtilities.log(Level.ERROR_INT, e);
        }
    }
    
    public void onRowSelect(SelectEvent event) {
        startViewItem();
    }
    
    public void setSelectedItem(Item item) {
        selectedItem = item;
    }
    
    public String getName() {
        return "Data Crow";
    }
    
    public Items getItems() {
        return items;
    }
}
