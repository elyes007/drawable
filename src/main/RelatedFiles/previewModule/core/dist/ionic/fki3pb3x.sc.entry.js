const e=window.Ionic.h;import{a as t,c as o,d as n,e as s,f as i,g as r,h as a}from "./chunk-265f51bc.js";import{a as l}from "./chunk-7c632336.js";import{a as p,b as c}from "./chunk-99929188.js";import "./chunk-90d954cd.js";import{a as d}from"./chunk-88b2e02b.js";function m(e, t, o){let n="top",s="left";const i=t.querySelector(".popover-content"),r=i.getBoundingClientRect(),a=r.width,l=r.height,p=t.ownerDocument.defaultView.innerWidth,c=t.ownerDocument.defaultView.innerHeight,d=o&&o.target&&o.target.getBoundingClientRect(),m=null!=d&&"top"in d?d.top:c/2-l/2,h=null!=d&&"left"in d?d.left:p/2,y=d&&d.width||0,v=d&&d.height||0,f=t.querySelector(".popover-arrow"),g=f.getBoundingClientRect(),w=g.width,b=g.height;null==d&&(f.style.display="none");const D={top:m+v,left:h+y/2-w/2},P={top:m+v+(b-1),left:h+y/2-a/2};let E=!1,k=!1;P.left<u+25?(E=!0,P.left=u):a+u+P.left+25>p&&(k=!0,P.left=p-a-u,s="right"),m+v+l>c&&m-l>0?(D.top=m-(b+1),P.top=m-l-(b-1),t.className=t.className+" popover-bottom",n="bottom"):m+v+l>c&&(i.style.bottom=u+"%"),f.style.top=D.top+"px",f.style.left=D.left+"px",i.style.top=P.top+"px",i.style.left=P.left+"px",E&&(i.style.left=`calc(${P.left}px + var(--ion-safe-area-left, 0px))`),k&&(i.style.left=`calc(${P.left}px - var(--ion-safe-area-right, 0px))`),i.style.transformOrigin=n+" "+s;const x=new e,S=new e;S.addElement(t.querySelector("ion-backdrop")),S.fromTo("opacity",.01,.08);const B=new e;return B.addElement(t.querySelector(".popover-wrapper")),B.fromTo("opacity",.01,1),Promise.resolve(x.addElement(t).easing("ease").duration(100).add(S).add(B))}const u=5;function h(e,t){const o=new e,n=new e;n.addElement(t.querySelector("ion-backdrop"));const s=new e;return s.addElement(t.querySelector(".popover-wrapper")),s.fromTo("opacity",.99,0),n.fromTo("opacity",.08,0),Promise.resolve(o.addElement(t).easing("ease").duration(500).add(n).add(s))}function y(e,t,o){const n=t.ownerDocument,s="rtl"===n.dir;let i="top",r=s?"right":"left";const a=t.querySelector(".popover-content"),l=a.getBoundingClientRect(),p=l.width,c=l.height,d=n.defaultView.innerWidth,m=n.defaultView.innerHeight,u=o&&o.target&&o.target.getBoundingClientRect(),h=null!=u&&"bottom"in u?u.bottom:m/2-c/2,y=u&&u.height||0,f={top:h,left:null!=u&&"left"in u?s?u.left-p+u.width:u.left:d/2-p/2};f.left<v?f.left=v:p+v+f.left>d&&(f.left=d-p-v,r=s?"left":"right"),h+y+c>m&&h-c>0?(f.top=h-c-y,t.className=t.className+" popover-bottom",i="bottom"):h+y+c>m&&(a.style.bottom=v+"px"),a.style.top=f.top+"px",a.style.left=f.left+"px",a.style.transformOrigin=i+" "+r;const g=new e,w=new e;w.addElement(t.querySelector("ion-backdrop")),w.fromTo("opacity",.01,.32);const b=new e;b.addElement(t.querySelector(".popover-wrapper")),b.fromTo("opacity",.01,1);const D=new e;D.addElement(t.querySelector(".popover-content")),D.fromTo("scale",.001,1);const P=new e;return P.addElement(t.querySelector(".popover-viewport")),P.fromTo("opacity",.01,1),Promise.resolve(g.addElement(t).easing("cubic-bezier(0.36,0.66,0.04,1)").duration(300).add(w).add(b).add(D).add(P))}const v=12;function f(e,t){const o=new e,n=new e;n.addElement(t.querySelector("ion-backdrop"));const s=new e;return s.addElement(t.querySelector(".popover-wrapper")),s.fromTo("opacity",.99,0),n.fromTo("opacity",.32,0),Promise.resolve(o.addElement(t).easing("ease").duration(500).add(n).add(s))}class g{constructor(){this.presented=!1,this.keyboardClose=!0,this.backdropDismiss=!0,this.showBackdrop=!0,this.translucent=!1,this.animated=!0}onDismiss(e){e.stopPropagation(),e.preventDefault(),this.dismiss()}onBackdropTap(){this.dismiss(void 0,t)}lifecycle(e){const t=this.usersElement,o=w[e.type];if(t&&o){const n=new CustomEvent(o,{bubbles:!1,cancelable:!1,detail:e.detail});t.dispatchEvent(n)}}async present(){if(this.presented)return;const e=this.el.querySelector(".popover-content");if(!e)throw new Error("container is undefined");const t=Object.assign({},this.componentProps,{popover:this.el});return this.usersElement=await p(this.delegate,e,this.component,["popover-viewport",this.el["s-sc"]],t),await d(this.usersElement),o(this,"popoverEnter",m,y,this.event)}async dismiss(e,t){const o=await n(this,e,t,"popoverLeave",h,f,this.event);return o&&await c(this.delegate,this.usersElement),o}onDidDismiss(){return s(this.el,"ionPopoverDidDismiss")}onWillDismiss(){return s(this.el,"ionPopoverWillDismiss")}hostData(){return{"aria-modal":"true","no-router":!0,style:{zIndex:2e4+this.overlayIndex},class:Object.assign({},l(this.cssClass),{"popover-translucent":this.translucent})}}render(){return[e("ion-backdrop",{tappable:this.backdropDismiss,visible:this.showBackdrop}),e("div",{class:"popover-wrapper"},e("div",{class:"popover-arrow"}),e("div",{class:"popover-content"}))]}static get is(){return"ion-popover"}static get encapsulation(){return"scoped"}static get properties(){return{animated:{type:Boolean,attr:"animated"},backdropDismiss:{type:Boolean,attr:"backdrop-dismiss"},component:{type:String,attr:"component"},componentProps:{type:"Any",attr:"component-props"},config:{context:"config"},cssClass:{type:String,attr:"css-class"},delegate:{type:"Any",attr:"delegate"},dismiss:{method:!0},el:{elementRef:!0},enterAnimation:{type:"Any",attr:"enter-animation"},event:{type:"Any",attr:"event"},keyboardClose:{type:Boolean,attr:"keyboard-close"},leaveAnimation:{type:"Any",attr:"leave-animation"},mode:{type:String,attr:"mode"},onDidDismiss:{method:!0},onWillDismiss:{method:!0},overlayIndex:{type:Number,attr:"overlay-index"},present:{method:!0},showBackdrop:{type:Boolean,attr:"show-backdrop"},translucent:{type:Boolean,attr:"translucent"}}}static get events(){return[{name:"ionPopoverDidPresent",method:"didPresent",bubbles:!0,cancelable:!0,composed:!0},{name:"ionPopoverWillPresent",method:"willPresent",bubbles:!0,cancelable:!0,composed:!0},{name:"ionPopoverWillDismiss",method:"willDismiss",bubbles:!0,cancelable:!0,composed:!0},{name:"ionPopoverDidDismiss",method:"didDismiss",bubbles:!0,cancelable:!0,composed:!0}]}static get listeners(){return[{name:"ionDismiss",method:"onDismiss"},{name:"ionBackdropTap",method:"onBackdropTap"},{name:"ionPopoverDidPresent",method:"lifecycle"},{name:"ionPopoverWillPresent",method:"lifecycle"},{name:"ionPopoverWillDismiss",method:"lifecycle"},{name:"ionPopoverDidDismiss",method:"lifecycle"}]}static get style(){return".sc-ion-popover-md-h{--background:var(--ion-background-color,#fff);--min-width:0;--min-height:0;--max-width:auto;--height:auto;left:0;right:0;top:0;bottom:0;display:-ms-flexbox;display:flex;position:fixed;-ms-flex-align:center;align-items:center;-ms-flex-pack:center;justify-content:center;color:var(--ion-text-color,#000);z-index:1000}.overlay-hidden.sc-ion-popover-md-h{display:none}.popover-wrapper.sc-ion-popover-md{opacity:0;z-index:10}.popover-content.sc-ion-popover-md{display:-ms-flexbox;display:flex;position:absolute;-ms-flex-direction:column;flex-direction:column;width:var(--width);min-width:var(--min-width);max-width:var(--max-width);height:var(--height);min-height:var(--min-height);max-height:var(--max-height);background:var(--background);-webkit-box-shadow:var(--box-shadow);box-shadow:var(--box-shadow);overflow:auto;z-index:10}.popover-viewport.sc-ion-popover-md{--ion-safe-area-top:0px;--ion-safe-area-right:0px;--ion-safe-area-bottom:0px;--ion-safe-area-left:0px}.sc-ion-popover-md-h{--width:250px;--max-height:90%;--box-shadow:0 5px 5px -3px rgba(0,0,0,0.2),0 8px 10px 1px rgba(0,0,0,0.14),0 3px 14px 2px rgba(0,0,0,0.12)}.popover-content.sc-ion-popover-md{border-radius:4px;-webkit-transform-origin:left top;transform-origin:left top}[dir=rtl].sc-ion-popover-md-h   .popover-content.sc-ion-popover-md, [dir=rtl]   .sc-ion-popover-md-h   .popover-content.sc-ion-popover-md{-webkit-transform-origin:right top;transform-origin:right top}.popover-viewport.sc-ion-popover-md{-webkit-transition-delay:.1s;transition-delay:.1s}"}static get styleMode(){return"md"}}const w={ionPopoverDidPresent:"ionViewDidEnter",ionPopoverWillPresent:"ionViewWillEnter",ionPopoverWillDismiss:"ionViewWillLeave",ionPopoverDidDismiss:"ionViewDidLeave"};class b{create(e){return i(this.doc.createElement("ion-popover"),e)}dismiss(e,t,o){return r(this.doc,e,t,"ion-popover",o)}async getTop(){return a(this.doc,"ion-popover")}static get is(){return"ion-popover-controller"}static get properties(){return{create:{method:!0},dismiss:{method:!0},doc:{context:"document"},getTop:{method:!0}}}}export{g as IonPopover,b as IonPopoverController};