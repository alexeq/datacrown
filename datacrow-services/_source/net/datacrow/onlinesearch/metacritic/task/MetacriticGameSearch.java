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

package net.datacrow.onlinesearch.metacritic.task;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.datacrow.core.DcRepository;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.helpers.Software;
import net.datacrow.core.services.IOnlineSearchClient;
import net.datacrow.core.services.SearchMode;
import net.datacrow.core.services.plugin.IServer;
import net.datacrow.core.utilities.StringUtils;
import net.datacrow.core.utilities.html.HtmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MetacriticGameSearch extends MetacriticSearch {

    private final static XPath xpath = XPathFactory.newInstance().newXPath();
    
    public MetacriticGameSearch(IOnlineSearchClient listener, IServer server,
            SearchMode mode, String query) {

        super(listener, server, mode, query);
    }

    @Override
    protected DcObject getItem(Object key, boolean full) throws Exception {
        return getItem(new URL(getServer().getUrl() + key), full);
    }
    
    @Override
    protected DcObject getItem(URL url, boolean full) throws Exception {
        DcObject software = DcModules.get(DcModules._SOFTWARE).getItem();

        Document document = HtmlUtils.getDocument(url, "UTF-8");
        Document documentDetails = HtmlUtils.getDocument(new URL(url.toString() + "/details"), "UTF-8");
        
        String link = url.toString();
        String id = link.substring(getServer().getUrl().length());
        
        software.addExternalReference(DcRepository.ExternalReferences._METACRITICS, id);
        
        setTitle(software, document);
        software.setValue(Software._I_WEBPAGE, link);
        software.setValue(DcObject._SYS_SERVICEURL, link);

        setDescription(software, document, documentDetails);
        setPlatform(software, document);
        setYear(software, document);
        setRating(software, document);
        setInfo(software, Software._K_CATEGORIES, document, "Genre(s):", ",");
        
        if (full) {
            setInfo(software, Software._G_PUBLISHER, document, "Publisher:", ",");
            setInfo(software, Software._F_DEVELOPER, document, "Developer:", ",");
            setMultiplayer(software, document);
            setFrontImage(software, Software._M_PICTUREFRONT, document);
        }
        
        return software;
    }
    
    private void setPlatform(DcObject software, Document document) throws Exception {
        Node node = (Node) xpath.evaluate("//div[@class='product_title']/span", document, XPathConstants.NODE);
        if (node != null)
        	software.createReference(Software._H_PLATFORM, StringUtils.trim(node.getTextContent()));
    }
    
    private void setMultiplayer(DcObject software, Document document) throws Exception {
        String players = getInfo(software, document, "# of players:");
        if (players != null) {
            try {
                int count = players.equals("1") ? 1 : 2;
                software.setValue(Software._AB_MULTI, count > 1 ? Boolean.TRUE : Boolean.FALSE);
            } catch (NumberFormatException nfe) {}
        }
    }

    private void setYear(DcObject software, Document document) throws Exception {
        String date = getInfo(software, document, "Release Date:");
        if (date != null && date.indexOf(", ") > -1) {
            String year = date.substring(date.indexOf(", ") + 2);
            try {
                software.setValue(Software._C_YEAR, Long.valueOf(year));
            } catch (NumberFormatException nfew) {}
        }
    }
    
    @Override
    protected Collection<Object> getItemKeys() throws Exception {
        Collection<Object> keys = new ArrayList<Object>();
        
        String searchURL = getServer().getUrl() + "/search/game/" + getQuery() + "/results";
        Document document = HtmlUtils.getDocument(new URL(searchURL), "UTF8");
        NodeList nodeList = (NodeList) xpath.evaluate("html//a[starts-with(@href,'/game/')]/@href", document, XPathConstants.NODESET);
        int length = nodeList.getLength();
        for(int i = 0; i < length; i++) {
            String link = nodeList.item(i).getTextContent();
            keys.add(link);
        }
        return keys;
    }
    
}
