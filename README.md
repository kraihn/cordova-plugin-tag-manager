# Tag Manager

---

Tag Manager plugin for Android and iOS. It allows you to post usage information to your Google Tag Manager account.

This plugin defines a global `TagManager` Constructor.

Although in the global scope, it is not available until after the `deviceready` event.

    document.addEventListener("deviceready", onDeviceReady, false);
    function onDeviceReady() {
        console.log(TagManager);
    }

When being used in combination with AngularJS, it is recommended to use [Angulartics](https://luisfarzati.github.io/angulartics/) with the Cordova GTM plugin (angulartics-gtm-cordova).

## Installation

    cordova plugin add com.jareddickson.cordova.tag-manager

## Supported Platforms

- Android
- iOS

## TagManager

    var tagManager = cordova.require('com.jareddickson.cordova.tag-manager.TagManager');

### Methods

- `tagManager.init`: Initialize Tag Manager with an account ID and the number of seconds between dispatching analytics.

- `tagManager.trackEvent`: Log an event.

- `tagManager.trackPage`: Log a page view.

- `tagManager.dispatch`: Force an immediate dispatch to Tag Manager.

- `tagManager.exit`: Exit the TagManager instance and stop setInterval.


## tagManager.init

Initialize Tag Manager with an account ID and the number of seconds between dispatching analytics.

    tagManager.init(success, error, id, [period]);

### Parameters

- __success__: (Optional) The callback to execute if successful.

- __error__: (Optional) The callback to execute if an error occurs.

- __id__: The GTM account ID of the form 'GTM-000000'.

- __period__: The interval for sending tracking events if any exist in the queue.

### Quick Example

    // Tag Manager
    var tagManager = cordova.require('com.jareddickson.cordova.tag-manager.TagManager');
    var trackingId = 'GTM-000000';
    var intervalPeriod = 30; // seconds

    // Initialize Tag Manager
    tagManager.init(null, null, trackingId, intervalPeriod);

## tagManager.trackEvent

Log an event.

    tagManager.trackEvent(success, error, category, eventAction, eventLabel, eventValue);

### Parameters

- __success__: (Optional) The callback to execute if successful.

- __error__: (Optional) The callback to execute if an error occurs.

- __category__: The event category. This parameter is required to be non-empty.

- __eventAction__: The event action. This parameter is required to be non-empty.

- __eventLabel__: The event label. This parameter may be a blank string to indicate no label.

- __eventValue__: (Optional) The event value. This parameter may be -1 to indicate no value. Only accepts digits.

### Quick Example

    // Track a click-to-call event
    tagManager.trackEvent(null, null, 'Link', 'Click', 'Call (800) 123-1234', -1);

## tagManager.trackPage

Log a page view.

    tagManager.trackPage(success, error, pageURL);

### Parameters

- __success__: (Optional) The callback to execute if successful.

- __error__: (Optional) The callback to execute if an error occurs.

- __pageURL__: The URL of the page view.

### Quick Example

    // Track a pageview on a Contact Us page
    tagManager.trackPage(null, null, '/contact-us');

## tagManager.dispatch

Force an immediate dispatch to Tag Manager.

    tagManager.dispatch([success], [error]);

### Parameters

- __success__: (Optional) The callback to execute if successful.

- __error__: (Optional) The callback to execute if an error occurs.

### Quick Example

    // Dispatch
    tagManager.dispatch();

## tagManager.exit

Exit the TagManager instance and stop setInterval.

    tagManager.exit([success], [error]);

### Parameters

- __success__: (Optional) The callback to execute if successful.

- __error__: (Optional) The callback to execute if an error occurs.

### Quick Example

    // Exit
    tagManager.exit();


## License ##

The MIT License

Copyright (c) 2014 Jared Dickson

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
