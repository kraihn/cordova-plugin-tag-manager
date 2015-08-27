(function () {
    var cordovaRef = window.PhoneGap || window.cordova || window.Cordova;
    var queue = [];
    var runInterval = 1000;
    var running = false;
    var runner;

    function TagManager() {
    }

    // initialize google analytics with an account ID and the min number of seconds between posting
    //
    // id = the GTM account ID of the form 'GTM-000000'
    // period = the minimum interval for transmitting tracking events if any exist in the queue
    TagManager.prototype.init = function (success, fail, id, period) {
        runner = setInterval(run, runInterval);
        running = true;
        var timestamp = new Date().getTime();
        queue.push({
            timestamp: timestamp,
            method: 'initGTM',
            success: success,
            fail: fail,
            id: id,
            period: period
        });
    };

    // log an event
    //
    // category = The event category. This parameter is required to be non-empty.
    // eventAction = The event action. This parameter is required to be non-empty.
    // eventLabel = The event label. This parameter may be a blank string to indicate no label.
    // eventValue = The event value. This parameter may be -1 to indicate no value.
    TagManager.prototype.trackEvent = function (success, fail, category, eventAction, eventLabel, eventValue) {
        var timestamp = new Date().getTime();
        queue.push({
            timestamp: timestamp,
            method: 'trackEvent',
            success: success,
            fail: fail,
            category: category,
            eventAction: eventAction,
            eventLabel: eventLabel,
            eventValue: eventValue
        });
    };

    // log an event with custom label-value
    //
    // eventAction = The event action. This parameter is required to be non-empty.
    // eventLabel = The event label. This parameter may be a blank string to indicate no label.
    // eventValue = The event value. This parameter may be a blank string to indicate no value.
    TagManager.prototype.trackCustomEvent = function (success, fail, eventAction, eventLabel, eventValue) {
        var timestamp = new Date().getTime();
        queue.push({
            timestamp: timestamp,
            method: 'trackCustomEvent',
            success: success,
            fail: fail,
            eventAction: eventAction,
            eventLabel: eventLabel,
            eventValue: eventValue
        });
    };

    // log a page view
    //
    // pageURL = the URL of the page view
    TagManager.prototype.trackPage = function (success, fail, pageURL) {
        var timestamp = new Date().getTime();
        queue.push({
            timestamp: timestamp,
            method: 'trackPage',
            success: success,
            fail: fail,
            pageURL: pageURL
        });
    };

    // force a dispatch to Tag Manager
    TagManager.prototype.dispatch = function (success, fail) {
        var timestamp = new Date().getTime();
        queue.push({
            timestamp: timestamp,
            method: 'dispatch',
            success: success,
            fail: fail
        });
    };

    // exit the TagManager instance and stop setInterval
    TagManager.prototype.exit = function (success, fail) {
        var timestamp = new Date().getTime();
        queue.push({
            timestamp: timestamp,
            method: 'exitGTM',
            success: success,
            fail: fail
        });
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

    function run() {
        if (queue.length > 0) {
            var item = queue.shift();
            if (item.method === 'initGTM') {
                cordovaRef.exec(item.success, item.fail, 'TagManager', item.method, [item.id, item.period]);
            }
            else if (item.method === 'trackEvent') {
                cordovaRef.exec(item.success, item.fail, 'TagManager', item.method, [item.category, item.eventAction, item.eventLabel, item.eventValue]);
            }
            else if (item.method === 'trackCustomEvent') {
                cordovaRef.exec(item.success, item.fail, 'TagManager', item.method, [item.eventAction, item.eventLabel, item.eventValue]);
            }
            else if (item.method === 'trackPage') {
                cordovaRef.exec(item.success, item.fail, 'TagManager', item.method, [item.pageURL]);
            }
            else if (item.method === 'dispatch') {
                cordovaRef.exec(item.success, item.fail, 'TagManager', item.method, []);
            }
            else if (item.method === 'exitGTM') {
                cordovaRef.exec(item.success, item.fail, 'TagManager', item.method, []);
                clearInterval(runner);
                running = false;
            }
        }
    }

    if (typeof module != 'undefined' && module.exports) {
        module.exports = new TagManager();
    }
})();
/* End of Temporary Scope. */
