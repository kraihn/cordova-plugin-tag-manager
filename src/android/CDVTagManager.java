/**
 * Copyright (c) 2014 Jared Dickson
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.jareddickson.cordova.tagmanager;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;

import com.google.analytics.tracking.android.GAServiceManager;
import com.google.tagmanager.Container;
import com.google.tagmanager.ContainerOpener;
import com.google.tagmanager.ContainerOpener.OpenType;
import com.google.tagmanager.DataLayer;
import com.google.tagmanager.TagManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class echoes a string called from JavaScript.
 */
public class CDVTagManager extends CordovaPlugin {

    private Container mContainer;
    private boolean inited = false;

    public CDVTagManager() {
    }

    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callback) {
        if (action.equals("initGTM")) {
            try {
                // Set the dispatch interval
                GAServiceManager.getInstance().setLocalDispatchPeriod(args.getInt(1));

                TagManager tagManager = TagManager.getInstance(this.cordova.getActivity().getApplicationContext());
                ContainerOpener.openContainer(
                        tagManager,                             // TagManager instance.
                        args.getString(0),                      // Tag Manager Container ID.
                        OpenType.PREFER_NON_DEFAULT,            // Prefer not to get the default container, but stale is OK.
                        null,                                   // Time to wait for saved container to load (ms). Default is 2000ms.
                        new ContainerOpener.Notifier() {        // Called when container loads.
                            @Override
                            public void containerAvailable(Container container) {
                                // Handle assignment in callback to avoid blocking main thread.
                                mContainer = container;
                                inited = true;
                            }
                        }
                );
                callback.success("initGTM - id = " + args.getString(0) + "; interval = " + args.getInt(1) + " seconds");
                return true;
            } catch (final Exception e) {
                callback.error(e.getMessage());
            }
        } else if (action.equals("exitGTM")) {
            try {
                inited = false;
                callback.success("exitGTM");
                return true;
            } catch (final Exception e) {
                callback.error(e.getMessage());
            }
        } else if (action.equals("trackEvent")) {
            if (inited) {
                try {
                    DataLayer dataLayer = TagManager.getInstance(this.cordova.getActivity().getApplicationContext()).getDataLayer();
                    int value = 0;
                    try {
                        value = args.getInt(3);
                    } catch (Exception e) {
                    }
                    dataLayer.push(DataLayer.mapOf("event", "interaction", "target", args.getString(0), "action", args.getString(1), "target-properties", args.getString(2), "value", value));
                    callback.success("trackEvent - category = " + args.getString(0) + "; action = " + args.getString(1) + "; label = " + args.getString(2) + "; value = " + value);
                    return true;
                } catch (final Exception e) {
                    callback.error(e.getMessage());
                }
            } else {
                callback.error("trackEvent failed - not initialized");
            }
        } else if (action.equals("pushEvent")) {
            if (inited) {
                try {
                    DataLayer dataLayer = TagManager.getInstance(this.cordova.getActivity().getApplicationContext()).getDataLayer();
                    dataLayer.push(objectMap(args.getJSONObject(0)));
                    callback.success("pushEvent: " + dataLayer.toString());
                    return true;
                } catch (final Exception e) {
                    callback.error(e.getMessage());
                }
            } else {
                callback.error("pushEvent failed - not initialized");
            }
        } else if (action.equals("trackPage")) {
            if (inited) {
                try {
                    DataLayer dataLayer = TagManager.getInstance(this.cordova.getActivity().getApplicationContext()).getDataLayer();
                    dataLayer.push(DataLayer.mapOf("event", "content-view", "content-name", args.get(0)));
                    callback.success("trackPage - url = " + args.getString(0));
                    return true;
                } catch (final Exception e) {
                    callback.error(e.getMessage());
                }
            } else {
                callback.error("trackPage failed - not initialized");
            }
        } else if (action.equals("dispatch")) {
            if (inited) {
                try {
                    GAServiceManager.getInstance().dispatchLocalHits();
                    callback.success("dispatch sent");
                    return true;
                } catch (final Exception e) {
                    callback.error(e.getMessage());
                }
            } else {
                callback.error("dispatch failed - not initialized");
            }
        }
        return false;
    }

    private Map<Object, Object> objectMap(JSONObject o) throws JSONException {
        if (o.length() == 0) {
            return Collections.<Object, Object>emptyMap();
        }
        Map<Object, Object> map = new HashMap<Object, Object>(o.length());
        Iterator it = o.keys();
        Object key;
        Object value;
        while (it.hasNext()) {
            key = it.next();
            value = o.has(key.toString()) ? o.get(key.toString()): null;
            map.put(key, value);
        }
        return map;
    }    
}
