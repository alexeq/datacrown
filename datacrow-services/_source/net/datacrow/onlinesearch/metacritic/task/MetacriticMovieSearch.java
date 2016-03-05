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
import net.datacrow.core.objects.helpers.Movie;
import net.datacrow.core.services.IOnlineSearchClient;
import net.datacrow.core.services.SearchMode;
import net.datacrow.core.services.plugin.IServer;
import net.datacrow.core.utilities.CoreUtilities;
import net.datacrow.core.utilities.StringUtils;
import net.datacrow.core.utilities.html.HtmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MetacriticMovieSearch extends MetacriticSearch {

    private final static XPath xpath = XPathFactory.newInstance().newXPath();
    
    public MetacriticMovieSearch(IOnlineSearchClient listener, IServer server,
            SearchMode mode, String query) {

        super(listener, server, mode, query);
    }

    @Override
    protected DcObject getItem(Object key, boolean full) throws Exception {
        return getItem(new URL(getServer().getUrl() + key), full);
    }
    
    @Override
    protected DcObject getItem(URL url, boolean full) throws Exception {
        DcObject movie = DcModules.get(DcModules._MOVIE).getItem();

        Document document = HtmlUtils.getDocument(url, "UTF-8");
        Document documentDetails = HtmlUtils.getDocument(new URL(url.toString() + "/details"), "UTF-8");
        
        String link = url.toString();
        String id = link.substring(getServer().getUrl().length());
        
        movie.addExternalReference(DcRepository.ExternalReferences._METACRITICS, id);
        
        setTitle(movie, document);
        movie.setValue(Movie._G_WEBPAGE, link);
        movie.setValue(DcObject._SYS_SERVICEURL, link);

        setDescription(movie, document, documentDetails);
        setYear(movie, document);
        setRating(movie, document);
        setRuntime(movie, document);
        setMPAARating(movie, document);
        
        setCountry(movie, documentDetails);
        setInfo(movie, Movie._H_GENRES, document, "Genre(s):", ",");
        
        if (full) {
            setInfo(movie, Movie._J_DIRECTOR, document, "Director:", ",");
            setActors(movie, documentDetails);
            setFrontImage(movie, Movie._X_PICTUREFRONT, document);
        }
        
        return movie;
    }
    
    private void setCountry(DcObject movie, Document documentDetails) throws Exception { 
    	Node node = (Node) xpath.evaluate("//tr[th='Country:']/td", documentDetails, XPathConstants.NODE);
    	if (node != null) {
    		String country = node.getTextContent();
    		movie.createReference(Movie._F_COUNTRY, StringUtils.trim(country));
    	}
    }
    
    private void setRuntime(DcObject movie, Document document) throws Exception {
        String minutes = getInfo(movie, document, "Runtime:");
        if (minutes != null) {
            minutes = minutes.indexOf(",") > -1 ? minutes.substring(0, minutes.indexOf(",")) : minutes;
            minutes = StringUtils.getContainedNumber(minutes);
            
            if (!CoreUtilities.isEmpty(minutes)) {
                movie.setValue(Movie._L_PLAYLENGTH, Long.valueOf(Integer.valueOf(minutes) * 60));    
            }
        }
    }

    private void setActors(DcObject movie, Document document) throws Exception {
        NodeList actors = (NodeList) xpath.evaluate("//table[@class='credits']//a[starts-with(@href,'/person/')]", document, XPathConstants.NODESET);
        if (actors != null) {
        	for (int i = 0; i < actors.getLength(); i++)
        		movie.createReference(Movie._I_ACTORS, StringUtils.trim(actors.item(i).getTextContent()));
        }
    }
    
    private void setMPAARating(DcObject movie, Document document) throws Exception {
        String mpaa = getInfo(movie, document, "Rating:");
        if (mpaa != null) movie.setValue(Movie._3_CERTIFICATION, mpaa);
    }
    
    private void setYear(DcObject movie, Document document) throws Exception {
        String date = getInfo(movie, document, "Release Date:");
        if (date != null) {
            String year = date.substring(date.indexOf(",") + 1);
            try {
                movie.setValue(Movie._C_YEAR, Long.valueOf(StringUtils.trim(year)));
            } catch (NumberFormatException nfe) {}
        }
    }
    
    @Override
    protected Collection<Object> getItemKeys() throws Exception {
        Collection<Object> keys = new ArrayList<Object>();
        
        String searchURL = getServer().getUrl() + "/search/movie/" + getQuery() + "/results";
        Document document = HtmlUtils.getDocument(new URL(searchURL), "UTF8");
        NodeList nodeList = (NodeList) xpath.evaluate("html//a[starts-with(@href,'/movie/')]/@href", document, XPathConstants.NODESET);
        int length = nodeList.getLength();
        for(int i = 0; i < length; i++) {
            String link = nodeList.item(i).getTextContent();
            keys.add(link);
        }
        return keys;
    }
    
}
