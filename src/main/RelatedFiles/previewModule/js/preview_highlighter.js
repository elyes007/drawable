var hoveredElement;
var selectedElement;
var isSetting = true;

window.onload = function () {
    document.getElementsByTagName("body")[0].onclick = unfocus;
    var elements = document.getElementsByClassName("clickable");
    Array.prototype.forEach.call(elements, function(element) {
        element.onclick = clicked;
        element.onmouseenter = onHover;
        element.onmouseleave = onQuit;
    });
}

function setIsSetting(boolValue) {
    isSetting = boolValue;
}

function clicked(event) {
    if (isSetting) {
        event.stopPropagation();
        if(selectedElement) {
            selectedElement.classList.remove("selected");
        }
        selectedElement = this;
        this.classList.add("selected");
        app.setEelement(this);
    }
}

function unfocus(event) {
    if(selectedElement) {
        selectedElement.classList.remove("selected");
    }
}

function onHover(event) {
    if (isSetting) {
        if(hoveredElement) {
            hoveredElement.classList.remove("hovered");
        }
        hoveredElement = this;
        this.classList.add("hovered");
    }
}

function onQuit(event) {
    this.classList.remove("hovered");
}

function snapshot(){
    var elements = document.getElementsByClassName("selected");
    Array.prototype.forEach.call(elements, function(element) {
        element.classList.remove("selected")
    });
    elements = document.getElementsByClassName("hovered");
    Array.prototype.forEach.call(elements, function(element) {
        element.classList.remove("hovered")
    });
    app.snapshot();
}