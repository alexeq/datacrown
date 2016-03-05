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

package net.datacrow.onlinesearch.sourceforge.task;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.datacrow.core.DcRepository;
import net.datacrow.core.http.HttpConnection;
import net.datacrow.core.modules.DcModules;
import net.datacrow.core.objects.DcImageIcon;
import net.datacrow.core.objects.DcObject;
import net.datacrow.core.objects.helpers.Software;
import net.datacrow.core.services.IOnlineSearchClient;
import net.datacrow.core.services.SearchMode;
import net.datacrow.core.services.SearchTask;
import net.datacrow.core.services.plugin.IServer;
import net.datacrow.core.utilities.html.HtmlUtils;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SourceforgeSoftwareSearch extends SearchTask {

    private static Logger logger = Logger.getLogger(SourceforgeSoftwareSearch.class.getName());
    
    private final static XPath xpath = XPathFactory.newInstance().newXPath();
    
    public SourceforgeSoftwareSearch(IOnlineSearchClient listener, IServer server, SearchMode mode, String query) {
        super(listener, server, null, mode, query);
    }

    @Override
    protected DcObject getItem(URL url) throws Exception {
        return getItem(url, true);
    }
    
    @Override
    public String getWhiteSpaceSubst() {
        return "%";
    }

    @Override
    protected DcObject getItem(Object key, boolean full) throws Exception {
        URL url = new URL(getServer().getUrl() + "/rest/p/" + key);
        System.out.println(url);
        return getItem(url, full);
    }
    
    protected DcObject getItem(URL url, boolean full) throws Exception {
        DcObject software = DcModules.get(DcModules._SOFTWARE).getItem();

        String link = url.toString();
        String id = link.substring(link.lastIndexOf("/") + 1);
        
        HttpConnection httpConnection = new HttpConnection(new URL(link));
        String response = httpConnection.getString();
        
        software.addExternalReference(DcRepository.ExternalReferences._SOURCEFORGE, id);
        software.setValue(DcObject._SYS_SERVICEURL, link);
        
        JsonParser jsonParser = new JsonParser();
        JsonObject project = (JsonObject) jsonParser.parse(response);
        
        software.setValue(Software._A_TITLE, project.get("name").getAsString());
        software.setValue(Software._I_WEBPAGE, project.get("url").getAsString());
        software.setValue(Software._B_DESCRIPTION, project.get("short_description").getAsString());
        
        setDevelopers(software, project);
        
        JsonObject oCategories = project.get("categories").getAsJsonObject();
        setLicense(software, oCategories);
        setCategories(software, oCategories);
        setLanguages(software, oCategories);
        setPlatform(software, oCategories);
        setDevelopers(software, oCategories);

        if (full)
            setImages(software, project);
        
        return software;
    }

    private void setLicense(DcObject software, JsonObject oCategories) {
        JsonElement eLicenses = oCategories.get("license");
        if (eLicenses != null) {
            for (JsonElement eLicense : eLicenses.getAsJsonArray()) {
                JsonObject oLicense = eLicense.getAsJsonObject();
                software.createReference(Software._Z_LICENSE, oLicense.get("fullname").getAsString());
                break;
            }
        }
    }
    
    private void setCategories(DcObject software, JsonObject oCategories) {
        JsonElement eTopics = oCategories.get("topic");
        if (eTopics != null) {
            for (JsonElement eTopic : eTopics.getAsJsonArray()) {
                JsonObject oTopic = eTopic.getAsJsonObject();
                software.createReference(Software._K_CATEGORIES, oTopic.get("fullname").getAsString());
            }
        }
    }
    
    private void setLanguages(DcObject software, JsonObject oCategories) {
        JsonElement eTranslations = oCategories.get("translation");
        if (eTranslations != null) {
            for (JsonElement eTranslation : eTranslations.getAsJsonArray()) {
                JsonObject oTranslation = eTranslation.getAsJsonObject();
                software.createReference(Software._D_LANGUAGE, oTranslation.get("fullname").getAsString());
            }
        }
    }
    
    private void setPlatform(DcObject software, JsonObject oCategories) {
        JsonElement ePlatforms = oCategories.get("os");
        if (ePlatforms != null) {
            for (JsonElement ePlatform : ePlatforms.getAsJsonArray()) {
                JsonObject oPlatform = ePlatform.getAsJsonObject();
                software.createReference(Software._H_PLATFORM, oPlatform.get("fullname").getAsString());
                break;
            }
        }
    }

    private void setImages(DcObject dco, JsonObject project) throws Exception {
        if (project.get("screenshots") != null) {
            JsonArray arImages = project.get("screenshots").getAsJsonArray();
            
            int[] imageFields = {Software._P_SCREENSHOTONE, Software._Q_SCREENSHOTTWO, Software._R_SCREENSHOTTHREE};
            int idx = 0;
            for (JsonElement eImage : arImages) {
                JsonObject oImage = eImage.getAsJsonObject();
                String url = oImage.get("url").getAsString();
                try {
                    HttpConnection connection = new HttpConnection(new URL(url));
                    byte[] imageData = connection.getBytes();
                    connection.close();
                    
                    dco.setValue(imageFields[idx++], new DcImageIcon(imageData));
                    
                    if (idx >= 3) break;
                    
                } catch (Exception e) {
                    logger.error("An error occurred while retrieving image from " + url, e);
                    listener.addError("An error occurred while retrieving image from " + url);
                }
            }
        }
    }
    
    private void setDevelopers(DcObject software, JsonObject oCategories) throws Exception {
        JsonElement eDevelopers = oCategories.get("developers");
        if (eDevelopers != null) {
            for (JsonElement eDeveloper : eDevelopers.getAsJsonArray()) {
                JsonObject oDeveloper = eDeveloper.getAsJsonObject();
                software.createReference(Software._F_DEVELOPER, oDeveloper.get("name").getAsString());
            }
        }
    }
    
    @Override
    protected Collection<Object> getItemKeys() throws Exception {
        Collection<Object> keys = new ArrayList<Object>();
        
        String searchURL = getServer().getUrl() + "/directory/?q=" + getQuery();
        Document document = HtmlUtils.getDocument(new URL(searchURL), "UTF8");
        NodeList nodeList = (NodeList) xpath.evaluate("html//a[starts-with(@href,'/projects/')]/@href", document, XPathConstants.NODESET);
        int length = nodeList.getLength();
        for(int i = 0; i < length; i++) {
            String key = nodeList.item(i).getTextContent();
            
            if (key.indexOf("/files/") > -1) continue;
            
            key = key.substring("/projects/".length());
            key = key.substring(0, key.lastIndexOf("/"));

            if (!keys.contains(key) && !key.endsWith(".mirror"))
                keys.add(key);
        }
        return keys;
    }
    
}
