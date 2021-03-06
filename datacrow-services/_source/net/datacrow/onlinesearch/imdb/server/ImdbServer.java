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

package net.datacrow.onlinesearch.imdb.server;

import java.util.ArrayList;
import java.util.Collection;

import net.datacrow.core.DcRepository;
import net.datacrow.core.services.Region;
import net.datacrow.core.services.plugin.IServer;
import net.datacrow.core.settings.Setting;
import net.datacrow.settings.DcSettings;

public abstract class ImdbServer implements IServer {

    private static final long serialVersionUID = 4236946766437920675L;

    private Collection<Region> regions = new ArrayList<Region>();
    
    public ImdbServer() {
        regions.add(new Region("default", "Default", "http://www.imdb.com"));
        regions.add(new Region("us", "USA", "http://us.imdb.com"));
        regions.add(new Region("it", "Italy", "http://italian.imdb.com"));
        regions.add(new Region("de", "Germany", "http://german.imdb.com"));
        regions.add(new Region("fr", "France", "http://french.imdb.com"));
        regions.add(new Region("sp", "Spain", "http://spanish.imdb.com"));        
    }

    @Override
    public Collection<Setting> getSettings() {
        Collection<Setting> settings = new ArrayList<Setting>();
        settings.add(DcSettings.getSetting(DcRepository.Settings.stImdbMaxActors));
        return settings;
    }   

    @Override
    public String getName() {
        return "Imdb";
    }
    
    @Override
	public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<Region> getRegions() {
        return regions;
    }

    @Override
    public String getUrl() {
        return "http://www.imdb.com";
    }
    
    @Override
    public String toString() {
        return getName();
    }
}
