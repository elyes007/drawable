Ionic.loadBundle("chunk-1cb6d537.js",["exports","./chunk-2c5e69a8.js"],function(t,n){window;var e=new WeakMap;function o(t,n,o,r){void 0===r&&(r=0),e.has(t)!==o&&(o?function(t,n,o){var r=n.parentNode,i=n.cloneNode(!1);i.classList.add("cloned-input"),i.tabIndex=-1,r.appendChild(i),e.set(t,i);var a="rtl"===t.ownerDocument.dir?9999:-9999;t.style.pointerEvents="none",n.style.transform="translate3d("+a+"px,"+o+"px,0) scale(0)"}(t,n,r):function(t,n){var o=e.get(t);o&&(e.delete(t),o.remove()),t.style.pointerEvents="",n.style.transform=""}(t,n))}function r(t){return t===t.getRootNode().activeElement}var i="input, textarea, [no-blur]",a=.3;function u(t,e,i,u){var c,s=function(t){c=n.pointerCoord(t)},l=function(s){c&&(function(t,n,e){if(c&&e){var o=c.x-e.x,r=c.y-e.y;return o*o+r*r>36}return!1}(0,0,n.pointerCoord(s))||r(e)||(s.preventDefault(),s.stopPropagation(),function(t,n,e,r){var i=function(t,n,e){return function(n,e,o,r){var i=n.top,u=n.bottom,c=e.top+15,s=.5*Math.min(e.bottom,t.ownerDocument.defaultView.innerHeight-o)-u,l=c-i,d=Math.round(s<0?-s:l>0?-l:0),f=Math.abs(d);return{scrollAmount:d,scrollDuration:Math.min(400,Math.max(150,f/a)),scrollPadding:o,inputSafeY:4-(i-c)}}((t.closest("ion-item,[ion-item]")||t).getBoundingClientRect(),n.getBoundingClientRect(),e)}(t,e,r);Math.abs(i.scrollAmount)<4?n.focus():(o(t,n,!0,i.inputSafeY),n.focus(),e.scrollByPoint(0,i.scrollAmount,i.scrollDuration).then(function(){o(t,n,!1,i.inputSafeY),n.focus()}))}(t,e,i,u)))};return t.addEventListener("touchstart",s,!0),t.addEventListener("touchend",l,!0),function(){t.removeEventListener("touchstart",s,!0),t.removeEventListener("touchend",l,!0)}}var c="$ionPaddingTimer";function s(t,n){if("INPUT"===t.tagName&&(!t.parentElement||"ION-INPUT"!==t.parentElement.tagName)){var e=t.closest("ion-content");if(null!==e){var o=e[c];o&&clearTimeout(o),n>0?e.style.setProperty("--keyboard-offset",n+"px"):e[c]=setTimeout(function(){e.style.setProperty("--keyboard-offset","0px")},120)}}}t.startInputShims=function(t,n){var e=n.getNumber("keyboardHeight",290),a=n.getBoolean("scrollAssist",!0),c=n.getBoolean("hideCaretOnScroll",!0),l=n.getBoolean("inputBlurring",!0),d=n.getBoolean("scrollPadding",!0),f=new WeakMap,v=new WeakMap;function p(t){var n=(t.shadowRoot||t).querySelector("input"),i=t.closest("ion-content");if(n){if(i&&c&&!f.has(t)){var s=function(t,n,e){if(!e||!n)return function(){};var i=function(e){r(n)&&o(t,n,e)},a=function(){return o(t,n,!1)},u=function(){return i(!0)},c=function(){return i(!1)};return e.addEventListener("ionScrollStart",u),e.addEventListener("ionScrollEnd",c),n.addEventListener("blur",a),function(){e.removeEventListener("ionScrollStart",u),e.removeEventListener("ionScrollEnd",c),n.addEventListener("ionBlur",a)}}(t,n,i);f.set(t,s)}i&&a&&!v.has(t)&&(s=u(t,n,i,e),v.set(t,s))}}l&&function(t){var n=!0,e=!1;t.addEventListener("ionScrollStart",function(){e=!0}),t.addEventListener("focusin",function(){n=!0},!0),t.addEventListener("touchend",function(o){if(e)e=!1;else{var r=t.activeElement;if(r&&!r.matches(i)){var a=o.target;a!==r&&(a.matches(i)||a.closest(i)||(n=!1,setTimeout(function(){n||r.blur()},50)))}}},!1)}(t),d&&function(t,n){t.addEventListener("focusin",function(t){s(t.target,n)}),t.addEventListener("focusout",function(t){s(t.target,0)})}(t,e);for(var m=0,g=Array.from(t.querySelectorAll("ion-input"));m<g.length;m++)p(g[m]);t.body.addEventListener("ionInputDidLoad",function(t){p(t.target)}),t.body.addEventListener("ionInputDidUnload",function(t){var n,e;n=t.target,c&&((e=f.get(n))&&e(),f.delete(n)),a&&((e=v.get(n))&&e(),v.delete(n))})}});