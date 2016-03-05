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

package net.datacrow.web.model;

import java.util.ArrayList;
import java.util.List;

import net.datacrow.core.DcConfig;
import net.datacrow.core.DcRepository;
import net.datacrow.core.data.DataFilter;
import net.datacrow.core.data.DataFilters;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.server.Connector;
import net.datacrow.web.DcBean;

/**
 * Data model for the search page. 
 */
public class Items extends DcBean {

    private static final long serialVersionUID = 1L;

    private List<Item> items = new ArrayList<Item>();
    
    private int moduleIdx;
	private String name;
    private String searchString;
    
    private int[] overviewFields = new int[] {};
    
    public Items() {}
    
    public Items(int module) {
        this();
        
        moduleIdx = module;
    	name = DcModules.get(module).getObjectNamePlural();
    	
    	setOverviewFields();
    }
    
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    public String getSearchString() {
        return searchString;
    }
    
    public void search() {
        
        setOverviewFields();
        
        Connector conn = DcConfig.getInstance().getConnector();
        int[] indices = getOverviewFields();
        
        DataFilter df = DataFilters.createSearchAllFilter(moduleIdx, searchString);
        df = df == null ? new DataFilter(moduleIdx) : df;
        
        List<DcObject> c = conn.getItems(df, indices);
        setItems(c);
    }
    
    public void setOverviewFields() {
        List<Integer> c = new ArrayList<Integer>();
        
        int[] fields= DcModules.get(moduleIdx).getSettings().getIntArray(DcRepository.ModuleSettings.stWebOverviewFields);
        if (fields != null) {
            for (int field : fields)
                c.add(Integer.valueOf(field));
        }
        
        if (!c.contains(DcObject._ID))
            c.add(Integer.valueOf(DcObject._ID));
        
        overviewFields = new int[c.size()];
        int idx = 0;
        for (Integer i : c) 
            overviewFields[idx++] = i.intValue();
    }
    
    private int[] getOverviewFields() {
        return overviewFields;
    }    

    public void setItems(List<DcObject> data) {
        
        items.clear();
        
        for (DcObject dco : data)
            items.add(new Item(dco));
    }
    
    public List<Item> getItems() {
    	return items;
    }
    
    public String getName() {
        return name;
    }
}
