var __awaiter=this&&this.__awaiter||function(e,t,n,o){return new(n||(n=Promise))(function(i,r){function a(e){try{l(o.next(e))}catch(e){r(e)}}function c(e){try{l(o.throw(e))}catch(e){r(e)}}function l(e){e.done?i(e.value):new n(function(t){t(e.value)}).then(a,c)}l((o=o.apply(e,t||[])).next())})},__generator=this&&this.__generator||function(e,t){var n,o,i,r,a={label:0,sent:function(){if(1&i[0])throw i[1];return i[1]},trys:[],ops:[]};return r={next:c(0),throw:c(1),return:c(2)},"function"==typeof Symbol&&(r[Symbol.iterator]=function(){return this}),r;function c(r){return function(c){return function(r){if(n)throw new TypeError("Generator is already executing.");for(;a;)try{if(n=1,o&&(i=2&r[0]?o.return:r[0]?o.throw||((i=o.return)&&i.call(o),0):o.next)&&!(i=i.call(o,r[1])).done)return i;switch(o=0,i&&(r=[2&r[0],i.value]),r[0]){case 0:case 1:i=r;break;case 4:return a.label++,{value:r[1],done:!1};case 5:a.label++,o=r[1],r=[0];continue;case 7:r=a.ops.pop(),a.trys.pop();continue;default:if(!(i=(i=a.trys).length>0&&i[i.length-1])&&(6===r[0]||2===r[0])){a=0;continue}if(3===r[0]&&(!i||r[1]>i[0]&&r[1]<i[3])){a.label=r[1];break}if(6===r[0]&&a.label<i[1]){a.label=i[1],i=r;break}if(i&&a.label<i[2]){a.label=i[2],a.ops.push(r);break}i[2]&&a.ops.pop(),a.trys.pop();continue}r=t.call(e,a)}catch(e){r=[6,e],o=0}finally{n=i=0}if(5&r[0])throw r[1];return{value:r[0]?r[1]:void 0,done:!0}}([r,c])}}};Ionic.loadBundle("wvqczsjg",["require","exports","./chunk-a99bd936.js","./chunk-2c5e69a8.js","./chunk-9f86f253.js"],function(e,t,n,o,i){var r=window.Ionic.h,a=function(){function t(){var e=this;this.inputId="ion-tg-"+c++,this.lastDrag=0,this.activated=!1,this.name=this.inputId,this.checked=!1,this.disabled=!1,this.value="on",this.onFocus=function(){e.ionFocus.emit()},this.onBlur=function(){e.ionBlur.emit()}}return t.prototype.checkedChanged=function(e){this.ionChange.emit({checked:e,value:this.value})},t.prototype.disabledChanged=function(){this.emitStyle(),this.gesture&&this.gesture.setDisabled(this.disabled)},t.prototype.componentWillLoad=function(){this.emitStyle()},t.prototype.componentDidLoad=function(){return __awaiter(this,void 0,void 0,function(){var t,n=this;return __generator(this,function(o){switch(o.label){case 0:return t=this,[4,new Promise(function(t,n){e(["./chunk-54ca8d01.js"],t,n)})];case 1:return t.gesture=o.sent().createGesture({el:this.el,queue:this.queue,gestureName:"toggle",gesturePriority:100,threshold:5,passive:!1,onStart:function(){return n.onStart()},onMove:function(e){return n.onMove(e)},onEnd:function(e){return n.onEnd(e)}}),this.disabledChanged(),[2]}})})},t.prototype.componentDidUnload=function(){this.gesture&&(this.gesture.destroy(),this.gesture=void 0)},t.prototype.onClick=function(){this.lastDrag+300<Date.now()&&(this.checked=!this.checked)},t.prototype.emitStyle=function(){this.ionStyle.emit({"interactive-disabled":this.disabled})},t.prototype.onStart=function(){this.activated=!0,this.setFocus()},t.prototype.onMove=function(e){var t,n;t=e.deltaX,n="rtl"===this.doc.dir,(this.checked?!n&&-10>t||n&&10<t:!n&&10<t||n&&-10>t)&&(this.checked=!this.checked,i.hapticSelection())},t.prototype.onEnd=function(e){this.activated=!1,this.lastDrag=Date.now(),e.event.preventDefault(),e.event.stopImmediatePropagation()},t.prototype.getValue=function(){return this.value||""},t.prototype.setFocus=function(){this.buttonEl&&this.buttonEl.focus()},t.prototype.hostData=function(){var e=this,t=e.disabled,i=e.checked,r=e.activated,a=e.color,c=e.el,l=e.inputId+"-lbl",u=o.findItemLabel(c);return u&&(u.id=l),{role:"checkbox","aria-disabled":t?"true":null,"aria-checked":""+i,"aria-labelledby":l,class:Object.assign({},n.createColorClasses(a),{"in-item":n.hostContext("ion-item",c),"toggle-activated":r,"toggle-checked":i,"toggle-disabled":t,interactive:!0})}},t.prototype.render=function(){var e=this,t=this.getValue();return o.renderHiddenInput(!0,this.el,this.name,this.checked?t:"",this.disabled),[r("div",{class:"toggle-icon"},r("div",{class:"toggle-inner"})),r("button",{type:"button",onFocus:this.onFocus,onBlur:this.onBlur,disabled:this.disabled,ref:function(t){return e.buttonEl=t}})]},Object.defineProperty(t,"is",{get:function(){return"ion-toggle"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"encapsulation",{get:function(){return"shadow"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"properties",{get:function(){return{activated:{state:!0},checked:{type:Boolean,attr:"checked",mutable:!0,watchCallbacks:["checkedChanged"]},color:{type:String,attr:"color"},disabled:{type:Boolean,attr:"disabled",watchCallbacks:["disabledChanged"]},doc:{context:"document"},el:{elementRef:!0},mode:{type:String,attr:"mode"},name:{type:String,attr:"name"},queue:{context:"queue"},value:{type:String,attr:"value"}}},enumerable:!0,configurable:!0}),Object.defineProperty(t,"events",{get:function(){return[{name:"ionChange",method:"ionChange",bubbles:!0,cancelable:!0,composed:!0},{name:"ionFocus",method:"ionFocus",bubbles:!0,cancelable:!0,composed:!0},{name:"ionBlur",method:"ionBlur",bubbles:!0,cancelable:!0,composed:!0},{name:"ionStyle",method:"ionStyle",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(t,"listeners",{get:function(){return[{name:"click",method:"onClick"}]},enumerable:!0,configurable:!0}),Object.defineProperty(t,"style",{get:function(){return":host{-webkit-box-sizing:content-box!important;box-sizing:content-box!important;display:inline-block;outline:none;contain:content;cursor:pointer;-ms-touch-action:none;touch-action:none;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;z-index:2}:host(.ion-focused) input{border:2px solid #5e9ed6}:host(.toggle-disabled){pointer-events:none}button{left:0;top:0;margin-left:0;margin-right:0;margin-top:0;margin-bottom:0;position:absolute;width:100%;height:100%;border:0;background:transparent;cursor:pointer;-webkit-appearance:none;-moz-appearance:none;appearance:none;outline:none}:host-context([dir=rtl]) button{right:0}button::-moz-focus-inner{border:0}:host{--background:var(--ion-item-background,var(--ion-background-color,#fff));--background-checked:var(--ion-color-primary,#3880ff);--handle-background:var(--ion-item-background,var(--ion-background-color,#fff));--handle-background-checked:var(--ion-item-background,var(--ion-background-color,#fff));-webkit-box-sizing:content-box;box-sizing:content-box;position:relative;width:51px;height:32px;contain:strict}:host(.ion-color.toggle-checked) .toggle-icon{background:var(--ion-color-base)}:host(.ion-color.toggle-checked) .toggle-inner{background:var(--ion-color-contrast)}.toggle-icon{border-radius:16px;display:block;position:relative;width:100%;height:100%;-webkit-transform:translateZ(0);transform:translateZ(0);-webkit-transition:background-color .3s;transition:background-color .3s;background-color:var(--ion-color-step-50,#f2f2f2);overflow:hidden;pointer-events:none}.toggle-icon:before{right:2px;bottom:2px;border-radius:16px;-webkit-transform:scaleX(1);transform:scaleX(1);-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s;background:var(--background);content:\"\"}.toggle-icon:before,.toggle-inner{left:2px;top:2px;position:absolute}.toggle-inner{border-radius:14px;width:28px;height:28px;-webkit-transition:width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms,-webkit-transform .3s;transition:width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms,-webkit-transform .3s;transition:transform .3s,width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms;transition:transform .3s,width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms,-webkit-transform .3s;background:var(--handle-background);-webkit-box-shadow:0 3px 12px rgba(0,0,0,.16),0 3px 1px rgba(0,0,0,.1);box-shadow:0 3px 12px rgba(0,0,0,.16),0 3px 1px rgba(0,0,0,.1);will-change:transform;contain:strict}:host-context([dir=rtl]) .toggle-inner{right:2px}:host(.toggle-checked) .toggle-icon{background:var(--background-checked)}:host(.toggle-activated) .toggle-icon:before,:host(.toggle-checked) .toggle-icon:before{-webkit-transform:scale3d(0,0,0);transform:scale3d(0,0,0)}:host(.toggle-checked) .toggle-inner{-webkit-transform:translate3d(19px,0,0);transform:translate3d(19px,0,0);background:var(--handle-background-checked)}:host([dir=rtl].toggle-checked) .toggle-inner{-webkit-transform:translate3d(calc(-1 * 19px),0,0);transform:translate3d(calc(-1 * 19px),0,0)}:host(.toggle-activated.toggle-checked) .toggle-inner:before{-webkit-transform:scale3d(0,0,0);transform:scale3d(0,0,0)}:host(.toggle-activated) .toggle-inner{width:34px}:host(.toggle-activated.toggle-checked) .toggle-inner{left:-4px}:host([dir=rtl].toggle-activated.toggle-checked) .toggle-inner{right:-4px}:host(.toggle-disabled){opacity:.3}:host(.in-item[slot]){margin-left:0;margin-right:0;margin-top:0;margin-bottom:0;padding-left:16px;padding-right:8px;padding-top:6px;padding-bottom:5px}\@supports ((-webkit-margin-start:0) or (margin-inline-start:0)) or (-webkit-margin-start:0){:host(.in-item[slot]){padding-left:unset;padding-right:unset;-webkit-padding-start:16px;padding-inline-start:16px;-webkit-padding-end:8px;padding-inline-end:8px}}:host(.in-item[slot=start]){padding-left:0;padding-right:16px;padding-top:6px;padding-bottom:5px}\@supports ((-webkit-margin-start:0) or (margin-inline-start:0)) or (-webkit-margin-start:0){:host(.in-item[slot=start]){padding-left:unset;padding-right:unset;-webkit-padding-start:0;padding-inline-start:0;-webkit-padding-end:16px;padding-inline-end:16px}}"},enumerable:!0,configurable:!0}),Object.defineProperty(t,"styleMode",{get:function(){return"ios"},enumerable:!0,configurable:!0}),t}(),c=0;t.IonToggle=a,Object.defineProperty(t,"__esModule",{value:!0})});