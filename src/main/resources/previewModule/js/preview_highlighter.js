var hoveredElement;
var selectedElement;
var isSetting = false;

function setIsSetting(boolValue) {
    isSetting = boolValue;
    console.log(boolValue);
}

function clicked(e, dom) {
    if (isSetting) {
        e.stopPropagation();
        console.log(dom);
        if(selectedElement) {
            selectedElement.classList.remove("selected");
        }
        selectedElement = dom;
        dom.classList.add("selected");
        app.setEelement(dom);
        console.log("After");
    }
}

function unfocus() {
    if(selectedElement) {
        selectedElement.classList.remove("selected");
    }
}

function onHover(e, dom) {
    if (isSetting) {
        if(hoveredElement) {
            hoveredElement.classList.remove("hovered");
        }
        hoveredElement = dom;
        dom.classList.add("hovered");
    }
}

function onQuit(e, dom) {
    dom.classList.remove("hovered");
}