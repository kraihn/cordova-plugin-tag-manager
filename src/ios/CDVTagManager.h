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

#import <Cordova/CDV.h>
#import "TAGContainer.h"
#import "TAGContainerOpener.h"
#import "TAGLogger.h"
#import "TAGManager.h"
#import "TAGDataLayer.h"

@class TAGManager;
@class TAGContainer;

@interface CDVTagManager : CDVPlugin
{
    BOOL    inited;
}

@property(nonatomic, retain) TAGManager *tagManager;
@property(nonatomic, retain) TAGContainer *container;

- (void) initGTM:(CDVInvokedUrlCommand*)command;
- (void) containerAvailable:(TAGContainer *)container;
- (void) exitGTM:(CDVInvokedUrlCommand*)command;
- (void) trackEvent:(CDVInvokedUrlCommand*)command;
- (void) pushEvent:(CDVInvokedUrlCommand*)command;
- (void) trackPage:(CDVInvokedUrlCommand*)command;
- (void) dispatch:(CDVInvokedUrlCommand*)command;

@end
