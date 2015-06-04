/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010-2015, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
window.Graphene = window.Graphene || {};

window.Graphene.Page = window.Graphene.Page || {};

window.Graphene.Page.AjaxHalter = (function() {

    var STATE_CONSTRUCTED = -2,
        STATE_UNINITIALIZED = -1,
        STATE_UNSENT = 0,
        STATE_OPENED = 1,
        STATE_HEADERS_RECEIVED = 2,
        STATE_LOADING = 3,
        STATE_DONE = 4,
        
        DEFAULT_RESPONSE_TEXT = '',
        DEFAULT_RESPONSE_XML = null,
        DEFAULT_STATUS = 0,
        DEFAULT_STATUS_TEXT = '',

        _instances = new Array(),
        _associations = {},
        _haltCounter = 0,
        _enabled = true;
        
    function HaltedXHR(xhr, wrapper) {
        if (this.id === undefined) {
            _instances.push(this);
            this.id = _instances.length - 1;
        }
        this.currentState = STATE_CONSTRUCTED - 1;
        this.availableStates = {};
        this._proceeds = {};
        this.continueToState = STATE_CONSTRUCTED;
        this.xhr = xhr;
        this.wrapper = wrapper;
        this.sendParams = {};
        
        this.tryProcessStates = function() {
            while (this.currentState < this.continueToState && this.currentState < this.getLastAvailableState()) {
                //console.log('current: ' + this.currentState + ' < continueTo: ' + this.continueToState + ' < lastAvailable: ' + this.getLastAvailableState());
                this.currentState += 1;
                this.processState(this.currentState);
            }
        };
        
        this.processState = function(state) {
            if (this._proceeds[state]) {
                this.loadXhrParams(state);
                this._proceeds[state]();
                this.loadXhrParams(this.getLastAvailableState());
            }  
        };
        
        
        this.getLastAvailableState = function() {
            var last = STATE_CONSTRUCTED;
            for (var i in this._proceeds) {
                last = Math.max(last, i);
            }
            return last;
        };
        
        this.loadXhrParams = function(readyState) {
            var holder = this.availableStates[readyState];
            this.wrapper.readyState = Math.max(0, readyState);
            if(readyState === 4) {
                this.wrapper.responseText = holder.responseText;
                this.wrapper.responseXML = holder.responseXML;
                this.wrapper.status = holder.status;
                this.wrapper.statusText = holder.statusText;
            } else {
                this.wrapper.responseText = DEFAULT_RESPONSE_TEXT;
                this.wrapper.responseXML = DEFAULT_RESPONSE_XML;
                this.wrapper.status = DEFAULT_STATUS;
                this.wrapper.statusText = DEFAULT_STATUS_TEXT;
            }
        };
        
        this.saveXhrParams = function(readyState) {
            var holder = this.availableStates[readyState] = {};
            if(readyState === 4) {
                holder.responseText = this.xhr.responseText;
                holder.responseXML = this.xhr.responseXML;
                holder.status = this.xhr.status;
                holder.statusText = this.xhr.statusText;
            } else {
                holder.responseText = DEFAULT_RESPONSE_TEXT;
                holder.responseXML = DEFAULT_RESPONSE_XML;
                holder.status = DEFAULT_STATUS;
                holder.statusText = DEFAULT_STATUS_TEXT;
            }
            holder.onreadystatechange = this.xhr.onreadystatechange;
        };
        
        this.isXhrCompleted = function() {
            return this.currentState === STATE_DONE;
        };
        
        this.wait = function() {
            var that = this;
            this.tryProcessStates();
            if (!this.isXhrCompleted()) {
                setTimeout(function() {
                    that.wait.call(that);
                }, 100);
            }
        };
    }

    return {
        setEnabled: function(enabled) {
            _enabled = enabled;
        },
        isEnabled: function() {
            return _enabled;
        },
        getHandle: function() {
            if (this.isHandleAvailable()) {
                return _haltCounter++;
            }
            return -1;
        },
        isHandleAvailable: function() {
            return _instances.length - 1 >= _haltCounter;
        },
        continueTo: function(id, state) {
            var halter = _instances[id];
            if (state < halter.continueToState) {
                throw new Error("cannot continue to state (" + state + ") before the actual state (" + halter.continueToState + ")");
            }
            halter.continueToState = state;
        },
        getCurrentStateId: function(id) {
            var halter = _instances[id];
            return halter.currentState;
        },
        install: function() {
            window.Graphene.xhrInterception.onOpen( function(context) {
                if (_enabled) {
                    var halter = _associations[context.xhrOriginal] = new HaltedXHR(context.xhrOriginal, context.xhrWrapper);
                    halter.saveXhrParams(STATE_CONSTRUCTED);
                    halter._proceeds[STATE_CONSTRUCTED] = context.proceed();
                    halter.wait();
                } else {
                    return context.proceed();
                }
            });
            
            window.Graphene.xhrInterception.onSend( function(context) {
                var halter = _associations[context.xhrOriginal];

                if (halter !== undefined) {
                    halter.sendParams = arguments;
                    halter.saveXhrParams(STATE_UNINITIALIZED);
                    halter._proceeds[STATE_UNINITIALIZED] = context.proceed;
                } else {
                    return context.proceed();
                }
            });
            
            window.Graphene.xhrInterception.onreadystatechange( function(context) {
                var halter = _associations[context.xhrOriginal];
                
                if (halter !== undefined) {
                    var readyState = context.xhrOriginal.readyState;
                    halter.saveXhrParams(readyState);
                    halter._proceeds[readyState] = context.proceed;
                } else {
                    return context.proceed();
                }
            });

        }
    };
})();