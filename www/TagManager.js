(function () {
    var cordovaRef = window.PhoneGap || window.cordova || window.Cordova;

    function TagManager() {
    }

    // initialize google analytics with an account ID and the min number of seconds between posting
    //
    // id = the GTM account ID of the form 'GTM-000000'
    // period = the minimum interval for transmitting tracking events if any exist in the queue
    TagManager.prototype.init = function (success, fail, id, period) {
        return cordovaRef.exec(success, fail, 'TagManager', 'initGTM', [id, period]);
    };

    // log an event
    //
    // category = The event category. This parameter is required to be non-empty.
    // eventAction = The event action. This parameter is required to be non-empty.
    // eventLabel = The event label. This parameter may be a blank string to indicate no label.
    // eventLabel = The event label. This parameter may be a blank string to indicate no label.
    // eventValue = The event value. This parameter may be -1 to indicate no value.
    TagManager.prototype.trackEvent = function (success, fail, category, eventAction, eventLabel, eventValue) {
        return cordovaRef.exec(success, fail, 'TagManager', 'trackEvent', [category, eventAction, eventLabel, eventValue]);
    };

    // log a page view
    //
    // pageURL = the URL of the page view
    TagManager.prototype.trackPage = function (success, fail, pageURL) {
        return cordovaRef.exec(success, fail, 'TagManager', 'trackPage', [pageURL]);
    };

    TagManager.prototype.exit = function (success, fail) {
        return cordovaRef.exec(success, fail, 'TagManager', 'exitGTM', []);
    };

    if (cordovaRef && cordovaRef.addConstructor) {
        cordovaRef.addConstructor(init);
    }
    else {
        init();
    }

    function init() {
        if (!window.plugins) {
            window.plugins = {};
        }
        if (!window.plugins.TagManager) {
            window.plugins.TagManager = new TagManager();
        }
    }

    if (typeof module != 'undefined' && module.exports) {
        module.exports = new TagManager();
    }
})();
/* End of Temporary Scope. */