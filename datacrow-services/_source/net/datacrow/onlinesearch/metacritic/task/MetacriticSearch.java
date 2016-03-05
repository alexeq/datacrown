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
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.datacrow.core.DcRepository;
import net.datacrow.core.http.HttpConnectionUtil;
import net.datacrow.core.objects.DcImageIcon;
import net.datacrow.core.objects.DcMediaObject;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.helpers.Software;
import net.datacrow.core.services.IOnlineSearchClient;
import net.datacrow.core.services.SearchMode;
import net.datacrow.core.services.SearchTask;
import net.datacrow.core.services.SearchTaskUtilities;
import net.datacrow.core.services.plugin.IServer;
import net.datacrow.core.utilities.StringUtils;
import net.datacrow.settings.DcSettings;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class MetacriticSearch extends SearchTask {

    private final static XPath xpath = XPathFactory.newInstance().newXPath();
    
    public MetacriticSearch(IOnlineSearchClient listener, IServer server, SearchMode mode, String query) {
        super(listener, server, null, mode, query);
    }

    protected abstract DcObject getItem(URL url, boolean full) throws Exception;
    
    @Override
    protected DcObject getItem(URL url) throws Exception {
        return getItem(url, true);
    }

    @Override
    protected void preSearchCheck() {
        SearchTaskUtilities.checkForIsbn(this);
    }
    
    protected void setTitle(DcObject dco, Document document) throws Exception {
        Node node = (Node) xpath.evaluate("//div[@class='product_title']/a", document, XPathConstants.NODE);
        if (node != null) dco.setValue(DcMediaObject._A_TITLE, StringUtils.trim(node.getTextContent()));
    }
    
    protected void setDescription(DcObject dco, Document document, Document documentDetails) throws Exception {
        Node node = (Node) xpath.evaluate("//div[span='Summary:']/span[@class='data']", documentDetails, XPathConstants.NODE);
        String description = node != null ? StringUtils.trim(node.getTextContent()) : "";
        description = description.startsWith("Summary:") ? description.substring(8) : description;
        description = StringUtils.trim(description);
        
        
        if (DcSettings.getBoolean(DcRepository.Settings.stMetacriticRetrieveCriticReviews)) {
            NodeList reviewsNL = (NodeList) xpath.evaluate("//li[starts-with(@class,'review critic_review')]//div[@class='review_body']", document, XPathConstants.NODESET);
            NodeList sourcesNL = (NodeList) xpath.evaluate("//li[starts-with(@class,'review critic_review')]//div[@class='source']", document, XPathConstants.NODESET);
            
            List<String> sources = new ArrayList<String>();
            
            if (sourcesNL != null && reviewsNL != null) {
	            for (int i = 0; i < sourcesNL.getLength(); i++) {
	            	sources.add(StringUtils.trim(sourcesNL.item(i).getTextContent()));
	            }
	
	            for (int i = 0; i < reviewsNL.getLength(); i++) {
	            	description += "\n\n";
	            	description += sources.get(i) + ": ";
	            	description += StringUtils.trim(reviewsNL.item(i).getTextContent());
	            }
            }
        }
        
        if (description.length() > 0) 
            dco.setValue(Software._B_DESCRIPTION, description);
    }
    
    protected void setFrontImage(DcObject dco, int fieldIdx, Document document) throws Exception {
        Node node = (Node) xpath.evaluate("//img[@class='product_image large_image']/@src", document, XPathConstants.NODE);
        if (node != null) {
            String link = node.getTextContent();
            byte[] image = HttpConnectionUtil.retrieveBytes(link);
            if (image != null) dco.setValue(fieldIdx, new DcImageIcon(image));
        }
    }

    protected void setRating(DcObject dco, Document document) throws Exception {
        Node node = (Node) xpath.evaluate("//span[@class='score_value']", document, XPathConstants.NODE);
        if (node != null) {
            try {
                int rating = Integer.valueOf(StringUtils.trim(node.getTextContent()));
                dco.setValue(DcMediaObject._E_RATING, Long.valueOf(Math.round(rating / 10)));
            } catch (NumberFormatException nfe) {}
        }
    }

    
    protected String getInfo(DcObject dco, Document document, String tag) throws Exception {
        Node node = (Node) xpath.evaluate("//li[span='" + tag + "']/span[@class='data']", document, XPathConstants.NODE);
        if (node != null) {
            String value = node.getTextContent();
            return StringUtils.trim(value);
        }
        return null;
    }
    
    protected void setInfo(DcObject dco, int fieldIdx, Document document, String tag, String seperator) throws Exception {
        String values = getInfo(dco, document, tag);
        if (values != null) {
            seperator = seperator == null ? " " : seperator;
            StringTokenizer st = new StringTokenizer(values, seperator);
            
            while (st.hasMoreElements()) {
                String value = (String) st.nextElement();
                dco.createReference(fieldIdx, StringUtils.trim(value));
            }
        }
    }
}
