import*as tslib_1 from "../polyfills/tslib.js";import{h}from "../ionic.core.js";import{c as createColorClasses,d as hostContext}from "./chunk-7c632336.js";import{d as findItemLabel,e as renderHiddenInput}from "./chunk-6d7d2f8c.js";import{d as hapticSelection}from "./chunk-81780b86.js";var Toggle=function(){function e(){var e=this;this.inputId="ion-tg-"+toggleIds++,this.lastDrag=0,this.activated=!1,this.name=this.inputId,this.checked=!1,this.disabled=!1,this.value="on",this.onFocus=function(){e.ionFocus.emit()},this.onBlur=function(){e.ionBlur.emit()}}return e.prototype.checkedChanged=function(e){this.ionChange.emit({checked:e,value:this.value})},e.prototype.disabledChanged=function(){this.emitStyle(),this.gesture&&this.gesture.setDisabled(this.disabled)},e.prototype.componentWillLoad=function(){this.emitStyle()},e.prototype.componentDidLoad=function(){return tslib_1.__awaiter(this,void 0,void 0,function(){var e,t=this;return tslib_1.__generator(this,function(o){switch(o.label){case 0:return e=this,[4,import("./chunk-f56eaea8.js")];case 1:return e.gesture=o.sent().createGesture({el:this.el,queue:this.queue,gestureName:"toggle",gesturePriority:100,threshold:5,passive:!1,onStart:function(){return t.onStart()},onMove:function(e){return t.onMove(e)},onEnd:function(e){return t.onEnd(e)}}),this.disabledChanged(),[2]}})})},e.prototype.componentDidUnload=function(){this.gesture&&(this.gesture.destroy(),this.gesture=void 0)},e.prototype.onClick=function(){this.lastDrag+300<Date.now()&&(this.checked=!this.checked)},e.prototype.emitStyle=function(){this.ionStyle.emit({"interactive-disabled":this.disabled})},e.prototype.onStart=function(){this.activated=!0,this.setFocus()},e.prototype.onMove=function(e){shouldToggle(this.doc,this.checked,e.deltaX,-10)&&(this.checked=!this.checked,hapticSelection())},e.prototype.onEnd=function(e){this.activated=!1,this.lastDrag=Date.now(),e.event.preventDefault(),e.event.stopImmediatePropagation()},e.prototype.getValue=function(){return this.value||""},e.prototype.setFocus=function(){this.buttonEl&&this.buttonEl.focus()},e.prototype.hostData=function(){var e=this,t=e.disabled,o=e.checked,n=e.activated,i=e.color,r=e.el,s=e.inputId+"-lbl",a=findItemLabel(r);return a&&(a.id=s),{role:"checkbox","aria-disabled":t?"true":null,"aria-checked":""+o,"aria-labelledby":s,class:Object.assign({},createColorClasses(i),{"in-item":hostContext("ion-item",r),"toggle-activated":n,"toggle-checked":o,"toggle-disabled":t,interactive:!0})}},e.prototype.render=function(){var e=this,t=this.getValue();return renderHiddenInput(!0,this.el,this.name,this.checked?t:"",this.disabled),[h("div",{class:"toggle-icon"},h("div",{class:"toggle-inner"})),h("button",{type:"button",onFocus:this.onFocus,onBlur:this.onBlur,disabled:this.disabled,ref:function(t){return e.buttonEl=t}})]},Object.defineProperty(e,"is",{get:function(){return"ion-toggle"},enumerable:!0,configurable:!0}),Object.defineProperty(e,"encapsulation",{get:function(){return"shadow"},enumerable:!0,configurable:!0}),Object.defineProperty(e,"properties",{get:function(){return{activated:{state:!0},checked:{type:Boolean,attr:"checked",mutable:!0,watchCallbacks:["checkedChanged"]},color:{type:String,attr:"color"},disabled:{type:Boolean,attr:"disabled",watchCallbacks:["disabledChanged"]},doc:{context:"document"},el:{elementRef:!0},mode:{type:String,attr:"mode"},name:{type:String,attr:"name"},queue:{context:"queue"},value:{type:String,attr:"value"}}},enumerable:!0,configurable:!0}),Object.defineProperty(e,"events",{get:function(){return[{name:"ionChange",method:"ionChange",bubbles:!0,cancelable:!0,composed:!0},{name:"ionFocus",method:"ionFocus",bubbles:!0,cancelable:!0,composed:!0},{name:"ionBlur",method:"ionBlur",bubbles:!0,cancelable:!0,composed:!0},{name:"ionStyle",method:"ionStyle",bubbles:!0,cancelable:!0,composed:!0}]},enumerable:!0,configurable:!0}),Object.defineProperty(e,"listeners",{get:function(){return[{name:"click",method:"onClick"}]},enumerable:!0,configurable:!0}),Object.defineProperty(e,"style",{get:function(){return":host{-webkit-box-sizing:content-box!important;box-sizing:content-box!important;display:inline-block;outline:none;contain:content;cursor:pointer;-ms-touch-action:none;touch-action:none;-webkit-user-select:none;-moz-user-select:none;-ms-user-select:none;user-select:none;z-index:2}:host(.ion-focused) input{border:2px solid #5e9ed6}:host(.toggle-disabled){pointer-events:none}button{left:0;top:0;margin-left:0;margin-right:0;margin-top:0;margin-bottom:0;position:absolute;width:100%;height:100%;border:0;background:transparent;cursor:pointer;-webkit-appearance:none;-moz-appearance:none;appearance:none;outline:none}:host-context([dir=rtl]) button{right:0}button::-moz-focus-inner{border:0}:host{--background:var(--ion-item-background,var(--ion-background-color,#fff));--background-checked:var(--ion-color-primary,#3880ff);--handle-background:var(--ion-item-background,var(--ion-background-color,#fff));--handle-background-checked:var(--ion-item-background,var(--ion-background-color,#fff));-webkit-box-sizing:content-box;box-sizing:content-box;position:relative;width:51px;height:32px;contain:strict}:host(.ion-color.toggle-checked) .toggle-icon{background:var(--ion-color-base)}:host(.ion-color.toggle-checked) .toggle-inner{background:var(--ion-color-contrast)}.toggle-icon{border-radius:16px;display:block;position:relative;width:100%;height:100%;-webkit-transform:translateZ(0);transform:translateZ(0);-webkit-transition:background-color .3s;transition:background-color .3s;background-color:var(--ion-color-step-50,#f2f2f2);overflow:hidden;pointer-events:none}.toggle-icon:before{right:2px;bottom:2px;border-radius:16px;-webkit-transform:scaleX(1);transform:scaleX(1);-webkit-transition:-webkit-transform .3s;transition:-webkit-transform .3s;transition:transform .3s;transition:transform .3s,-webkit-transform .3s;background:var(--background);content:\"\"}.toggle-icon:before,.toggle-inner{left:2px;top:2px;position:absolute}.toggle-inner{border-radius:14px;width:28px;height:28px;-webkit-transition:width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms,-webkit-transform .3s;transition:width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms,-webkit-transform .3s;transition:transform .3s,width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms;transition:transform .3s,width .12s ease-in-out 80ms,left .11s ease-in-out 80ms,right .11s ease-in-out 80ms,-webkit-transform .3s;background:var(--handle-background);-webkit-box-shadow:0 3px 12px rgba(0,0,0,.16),0 3px 1px rgba(0,0,0,.1);box-shadow:0 3px 12px rgba(0,0,0,.16),0 3px 1px rgba(0,0,0,.1);will-change:transform;contain:strict}:host-context([dir=rtl]) .toggle-inner{right:2px}:host(.toggle-checked) .toggle-icon{background:var(--background-checked)}:host(.toggle-activated) .toggle-icon:before,:host(.toggle-checked) .toggle-icon:before{-webkit-transform:scale3d(0,0,0);transform:scale3d(0,0,0)}:host(.toggle-checked) .toggle-inner{-webkit-transform:translate3d(19px,0,0);transform:translate3d(19px,0,0);background:var(--handle-background-checked)}:host([dir=rtl].toggle-checked) .toggle-inner{-webkit-transform:translate3d(calc(-1 * 19px),0,0);transform:translate3d(calc(-1 * 19px),0,0)}:host(.toggle-activated.toggle-checked) .toggle-inner:before{-webkit-transform:scale3d(0,0,0);transform:scale3d(0,0,0)}:host(.toggle-activated) .toggle-inner{width:34px}:host(.toggle-activated.toggle-checked) .toggle-inner{left:-4px}:host([dir=rtl].toggle-activated.toggle-checked) .toggle-inner{right:-4px}:host(.toggle-disabled){opacity:.3}:host(.in-item[slot]){margin-left:0;margin-right:0;margin-top:0;margin-bottom:0;padding-left:16px;padding-right:8px;padding-top:6px;padding-bottom:5px}\@supports ((-webkit-margin-start:0) or (margin-inline-start:0)) or (-webkit-margin-start:0){:host(.in-item[slot]){padding-left:unset;padding-right:unset;-webkit-padding-start:16px;padding-inline-start:16px;-webkit-padding-end:8px;padding-inline-end:8px}}:host(.in-item[slot=start]){padding-left:0;padding-right:16px;padding-top:6px;padding-bottom:5px}\@supports ((-webkit-margin-start:0) or (margin-inline-start:0)) or (-webkit-margin-start:0){:host(.in-item[slot=start]){padding-left:unset;padding-right:unset;-webkit-padding-start:0;padding-inline-start:0;-webkit-padding-end:16px;padding-inline-end:16px}}"},enumerable:!0,configurable:!0}),Object.defineProperty(e,"styleMode",{get:function(){return"ios"},enumerable:!0,configurable:!0}),e}();function shouldToggle(e,t,o,n){var i="rtl"===e.dir;return t?!i&&n>o||i&&-n<o:!i&&-n<o||i&&n>o}var toggleIds=0;export{Toggle as IonToggle};