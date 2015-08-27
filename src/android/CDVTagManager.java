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

import java.util.concurrent.TimeUnit;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;

import com.google.android.gms.tagmanager.*;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
            	GoogleAnalytics.getInstance(this.cordova.getActivity().getApplicationContext()).setLocalDispatchPeriod(args.getInt(1));

                TagManager tagManager = TagManager.getInstance(this.cordova.getActivity().getApplicationContext());
//                PendingResult<ContainerHolder> pending = tagManager.loadContainerPreferNonDefault(args.getString(0), R.raw.gtm_default_container);
                PendingResult<ContainerHolder> pending = tagManager.loadContainerPreferNonDefault(args.getString(0), 0);

             // The onResult method will be called as soon as one of the following happens:
//              1. a saved container is loaded
//              2. if there is no saved container, a network container is loaded
//              3. the request times out. The example below uses a constant to manage the timeout period.
				 pending.setResultCallback(new ResultCallback<ContainerHolder>() {
				     @Override
				     public void onResult(ContainerHolder containerHolder) {
				         mContainer = containerHolder.getContainer();
				         if (!containerHolder.getStatus().isSuccess()) {
				             return;
				         }
				         inited = true;
//				         ContainerHolderSingleton.setContainerHolder(containerHolder);
//				         ContainerLoadedCallback.registerCallbacksForContainer(container);
//				         containerHolder.setContainerAvailableListener(new ContainerLoadedCallback());
				     }
				 }, 2, TimeUnit.SECONDS);

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
          } else if (action.equals("trackCustomEvent")) {
              if (inited) {
                  try {
                      DataLayer dataLayer = TagManager.getInstance(this.cordova.getActivity().getApplicationContext()).getDataLayer();
                      dataLayer.push(DataLayer.mapOf("event", args.getString(0), args.getString(1), args.getString(2)));
                      callback.success("trackCustomEvent - event = " + args.getString(0) + "; label = " + args.getString(1) + "; value = " + args.getString(2));
                      return true;
                  } catch (final Exception e) {
                      callback.error(e.getMessage());
                  }
              } else {
                  callback.error("trackCustomEvent failed - not initialized");
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
                    GoogleAnalytics.getInstance(this.cordova.getActivity().getApplicationContext()).dispatchLocalHits();
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
}
