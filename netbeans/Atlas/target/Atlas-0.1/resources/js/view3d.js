// three.js, STLLoader and OrbitControls.js must be loaded for this code to work!
// modelPath, strings[] and labels[] must be pre-set

// basic 3D world objects
var container;
var renderer, camera, scene;
var controls, mouseMode = 'view';
var projector, hitOffset, lastMouseX, lastMouseY;
var pointLight, ambientLight, lightOffset;
var boneMaterial, lineMaterial, markMaterial,
        pinMaterial, pinSelectedMaterial, greyMaterial;
var model, labelHolder, invisiboard, newMark;
var textBox, textBoxContent, modeSelect, newLabelButton, saveButton, loading;
var lastIntersect = null;
var mode = 'labels';

// currently selected label
var selectedLabel = null;

// initialize
init();
// render (loop not needed, render should be called by controls)
render();

// initialize 3D world
// everything that happens before first render goes here
function init() {
    // get containter element for canvas
    container = document.getElementById('container');
    
    // create a scene
    scene = new THREE.Scene();
    // container for labels
    labelHolder = new THREE.Object3D();
    scene.add( labelHolder );
    // create a camera
    camera = new THREE.PerspectiveCamera(45, 0.75, 1, 10000);
    // add the camera to the scene
    scene.add(camera);
    // pull back the camera
    camera.position.z = 200;

    // create renderer
    renderer = new THREE.WebGLRenderer();
    //renderer.setSize(width, height);
    renderer.setClearColor(0xF0F0F0, 0);
    // append renderer element to container
    container.appendChild(renderer.domElement);
    // setup orbit controls
    controls = new THREE.OrbitControls(camera, renderer.domElement);
    controls.addEventListener('change', render);

    // create materials
    boneMaterial = new THREE.MeshLambertMaterial({color: 0xFFFF99});
    pinMaterial = new THREE.MeshLambertMaterial({color: 0xAAAAAA});
    pinSelectedMaterial = new THREE.MeshLambertMaterial({color: 0xFF0000});
    lineMaterial = new THREE.LineBasicMaterial({
        vertexColors: THREE.VertexColors,
        color: 0xFFFFFF });
    markMaterial = new THREE.MeshBasicMaterial({color: 0xFF0000});
    greyMaterial = new THREE.MeshBasicMaterial({color: 0xAAAAAA});

    // create a point light
    pointLight = new THREE.PointLight(0xFFFFFF);
    // set offset from camera position (used on render)
    lightOffset = new THREE.Vector3(300, 300, 300);
    // create an ambient light
    ambientLight = new THREE.AmbientLight(0x333333);
    // add lights to the scene
    scene.add(pointLight);
    scene.add(ambientLight);
    
    // picking
    projector = new THREE.Projector();
    
    // listeners
    window.addEventListener('mousedown', onMouseDown, false);
    window.addEventListener('mouseup', onMouseUp, false);
    window.addEventListener('mousemove', onMouseMove, false);
    window.addEventListener('resize', onWindowResize, false);
    document.addEventListener("DOMContentLoaded", function() {
        onWindowResize();
    });
    // warning when leaving unsaved changes
    if (editable) {
        window.onbeforeunload = function() {
            var changed = false;
            labelHolder.children.forEach(function(label) {
                changed = changed | label.needsUpdate;
            });
            if (changed) return strings['unsavedChanges'];
        };
    }
    
    
    // textbox
    setupTextBox();
    
    // mode selector
    setupModeSelect();
    
    // new label button
    setupNewLabelButton();
    
    // save button
    setupSaveButton();

    // loading mesh / text
    setupLoading();

    // load 3D model
    // label creation is in loader's listener to avoid labels with no model
    loadModel(modelPath);
    
    // create invisible sprite for moving labels
    var geometry = new THREE.PlaneGeometry(1000, 1000);
    invisiboard = new THREE.Mesh(geometry, boneMaterial); // any material
    invisiboard.name = 'invisiboard';
    invisiboard.visible = false;
    scene.add(invisiboard);
    // create marker for "new label" position
    geometry = new THREE.SphereGeometry(1, 8, 8);
    newMark = new THREE.Mesh(geometry, markMaterial);
    newMark.name = 'newMark';
    newMark.visible = false;
    scene.add(newMark);

}

function loadModel(path) {
    var loader = new THREE.STLLoader();
    loader.addEventListener('load', function (event) {
        // get geometry
        var geometry = event.content;
        
        // get bounding box for normalization
        geometry.computeBoundingBox();

        // shift to center
        var shift = geometry.boundingBox.min.clone();
        shift.add(geometry.boundingBox.max);
        shift.divideScalar(-2);
        // scale longest dimension to 100
        var dim = geometry.boundingBox.max.clone();
        dim.sub(geometry.boundingBox.min);
        var scale = 100 / Math.max(dim.x, dim.y, dim.z);
        // create mesh with material
        model = new THREE.Mesh(geometry, boneMaterial);
        model.name = "model";
        // apply transforms
        model.scale.set(scale, scale, scale);
        model.translateX(shift.x * scale);
        model.translateY(shift.y * scale);
        model.translateZ(shift.z * scale);
                
        // add model and render
        scene.add(model);
        
        // create labels
        labels.forEach(function (label) {
            labelHolder.add(makeLabel(label, false));
        });
        
        // make loading disappear
        loading.style.display = 'none';
        
        render();
    } );
    loader.load(path); 
}

function setupLoading() {
    var loadingDiv = document.createElement('div');
    loadingDiv.style.fontSize = '20pt';
    loadingDiv.style.position = 'absolute';
    loadingDiv.innerHTML = 'LOADING';
    
    container.appendChild(loadingDiv);
    loading = loadingDiv;
} 

function setupTextBox() {
    
    var padding = 10;
    
    var holderDiv = document.createElement('div');
    holderDiv.style.position = 'absolute';
    holderDiv.style.backgroundColor = 'rgba(0, 0, 0, 0.8)';
    holderDiv.style.borderRadius = 10 + 'px';
    holderDiv.style.overflowY = 'auto';
    
    var textDiv = document.createElement('div');
    textDiv.style.color = 'rgba(255, 255, 255, 1)';
    textDiv.style.padding = padding + 'px';
    textDiv.style.textAlign = 'left';
    textDiv.innerHTML = strings['selectLabel'];
    
    holderDiv.appendChild(textDiv);
    container.appendChild(holderDiv);
    textBox = holderDiv;
    textBoxContent = textDiv;
}

function setupModeSelect() {
    
    var selectList = document.createElement("select");
    selectList.id = "modeSelect";
    selectList.style.position = 'absolute';

    var options = [strings['labels'], strings['pins'], strings['none']];
    var values = ['labels', 'pins', 'none'];
    
    for (var i = 0; i < options.length; i++) {
        var option = new Option(options[i], values[i]);
        selectList.add(option);
    }
    
    selectList.onchange = function(){
        switch (this.value) {
            case 'labels':
                labelHolder.visible = true;
                labelHolder.children.forEach(function(label) {
                    label.getObjectByName('sprite').visible = true;
                    label.getObjectByName('pin').visible = false;
                });
                textBox.style.visibility = 'visible';
                render();
                break;
            case 'pins':
                labelHolder.visible = true;
                labelHolder.children.forEach(function(label) {
                    label.getObjectByName('sprite').visible = false;
                    label.getObjectByName('pin').visible = true;
                });
                textBox.style.visibility = 'visible';
                render();
                break;
            case 'none':
                labelHolder.visible = false;
                textBox.style.visibility = 'hidden';
                render();
                break;
        }
    };
    
    container.appendChild(selectList);
    modeSelect = selectList;
    
}

function setupNewLabelButton() {
    var button = document.createElement("button");
    button.name = "newLabelButton";
    button.style.position = 'absolute';
    button.innerHTML = strings['addLabel'];
    
    button.onclick = function(){
        if (mouseMode === 'addLabel') {
            switchMouseMode('view');
            
        } else {
            switchMouseMode('addLabel');
        }
    };
    
    if (!editable) button.style.visibility = 'hidden';
    container.appendChild(button);
    newLabelButton = button;
}

function setupSaveButton() {
    var button = document.createElement("button");
    button.name = "saveButton";
    button.style.position = 'absolute';
    button.innerHTML = strings['saveButton'];
    
    button.onclick = function(){
        if (!confirm(strings['confirmSave'])) return;
        // add labels that need updating in specified format
        var labelUpdates = [];
        labelHolder.children.forEach(function(label) {
            if (label.needsUpdate) {
                var tempLabel = new Object();
                tempLabel.markX = label.markPosition.x;
                tempLabel.markY = label.markPosition.y;
                tempLabel.markZ = label.markPosition.z;
                tempLabel.labelX = label.labelPosition.x;
                tempLabel.labelY = label.labelPosition.y;
                tempLabel.labelZ = label.labelPosition.z;
                tempLabel.title = label.title;
                tempLabel.text = label.text;
                if (label.isNew) {
                    tempLabel.action = 'create';
                } else if (label.isDeleted) {
                    tempLabel.action = 'delete';
                    tempLabel.id = label.id;
                } else {
                    tempLabel.action = 'update';
                    tempLabel.id = label.id;
                }
                labelUpdates.push(tempLabel);
            }
        });
        // create JSON string
        labelUpdateInput.value = JSON.stringify(labelUpdates);
        // disable "unsaved changes" pop up
        window.onbeforeunload = function(){};
        // submit
        labelUpdateButton.click();
    };
    
    if (!editable) button.style.visibility = 'hidden';
    container.appendChild(button);
    saveButton = button;
}

function makeLabel(label, isNew) {

    var newLabel = new THREE.Object3D();
    // add attributes
    // flags for CRUD
    newLabel.needsUpdate = isNew;
    newLabel.isNew = isNew;
    newLabel.isDeleted = false;
    // other attribs
    newLabel.markPosition = new THREE.Vector3(label.markX, label.markY, label.markZ); 
    newLabel.labelPosition = new THREE.Vector3(label.labelX, label.labelY, label.labelZ);
    newLabel.id = label.id;
    // replace new lines with <br /> and add content
    var breakTag = '<br />';
    newLabel.title = label.title.replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag + '$2');    
    newLabel.text = label.text.replace(/([^>\r\n]?)(\r\n|\n\r|\r|\n)/g, '$1' + breakTag + '$2');
    
    var sprite = makeSprite(newLabel.title, newLabel.labelPosition);
    newLabel.add(sprite);

    var line = makeLine(newLabel.markPosition, newLabel.labelPosition);
    newLabel.add(line);
    
    var mark = makeMark(newLabel.markPosition);
    newLabel.add(mark);
    
    var pin = makePin(newLabel.labelPosition);
    pin.visible = false;
    newLabel.add(pin);
    
    return newLabel;
}

function makeSprite(title, position) {
    
    // create canvas to draw texture on
    var canvas = document.createElement('canvas');
    // grab 2D context
    var context = canvas.getContext('2d');
    // set fontsize
    var fontsize = 50;
    // set horizontal margin
    var margin = 20;
    // calculate text width with current font
    context.font = fontsize + "px Arial";
    var metrics = context.measureText(title);
    var textWidth = metrics.width;
    // texture dimensions
    var hSize = textWidth + 2*margin;
    var vSize = fontsize*1.4;
    // change canvas size to fit our texture
    canvas.width = textWidth + 2*margin;
    // re-set fonts, they changed with canvas size
    context.font = fontsize + "px Arial";
    // set fill and border color
    context.fillStyle = "rgba(200, 200, 200, 0.8)";
    context.strokeStyle = "rgba(0, 0, 0, 1.0)";
    // draw rounded rectangle
    roundRect(context, 0, 0, hSize, vSize, 15);
    
    context.fillStyle = "rgba(0,0,0,255)";
    context.fillText(title, margin, fontsize);

    // use canvas contents as a texture
    var texture = new THREE.Texture(canvas);
        texture.needsUpdate = true;

    var material = new THREE.MeshBasicMaterial({ map: texture, transparent: true });
    
    var geometry = new THREE.PlaneGeometry(hSize/vSize, 1);
    // setup UVs
    var coords = [new THREE.Vector2(0, 1-vSize/canvas.height),
                  new THREE.Vector2(hSize/canvas.width, 1-vSize/canvas.height),
                  new THREE.Vector2(hSize/canvas.width, 1),
                  new THREE.Vector2(0, 1)];
    geometry.faceVertexUvs[0] = [];
    geometry.faceVertexUvs[0][0] = [coords[3], coords[0], coords[2]];
    geometry.faceVertexUvs[0][1] = [coords[0], coords[1], coords[2]];
    
    var sprite = new THREE.Mesh(geometry, material);

    sprite.scale.set(4, 4, 1);
    sprite.position.copy(position);
    sprite.name = 'sprite';

    return sprite;	
}

function makeLine(start, end) {
    
    // a bit shorter looks better? best would be to always end at label edge
    var ratio = 0.9;
    var newEnd = new THREE.Vector3();
    newEnd.subVectors(start, end).multiplyScalar(1 - ratio);
    newEnd.add(end);
    // add midpoint for better coloring
    var mid = new THREE.Vector3();
    mid.addVectors(start, newEnd).multiplyScalar(0.5);
    
    var geometry = new THREE.Geometry();
    
    geometry.vertices.push(start, mid, newEnd);
    geometry.colors[0] = markMaterial.color;
    geometry.colors[1] = markMaterial.color;
    geometry.colors[2] = renderer.getClearColor();
    geometry.colorsNeedUpdate = true;
    var line = new THREE.Line(geometry, lineMaterial);
    line.name = 'line';

    return line;
}

function makeMark(position) {
    
    var geometry = new THREE.SphereGeometry(0.5, 16, 16);
    var mark = new THREE.Mesh(geometry, markMaterial);
    
    mark.position.copy(position);
    mark.name = 'mark';
    
    return mark;  
}

function makePin(position) {
    
    var geometry = new THREE.SphereGeometry(2, 16, 16);
    var pin = new THREE.Mesh(geometry, pinMaterial);
    
    pin.position.copy(position);
    pin.name = 'pin';
    
    return pin;  
}


// function for drawing rounded rectangles
// fill and stroke properties should be set prior to call
function roundRect(context, x, y, w, h, r) {

    context.beginPath();
    context.moveTo(x+r, y);
    context.lineTo(x+w-r, y);
    context.quadraticCurveTo(x+w, y, x+w, y+r);
    context.lineTo(x+w, y+h-r);
    context.quadraticCurveTo(x+w, y+h, x+w-r, y+h);
    context.lineTo(x+r, y+h);
    context.quadraticCurveTo(x, y+h, x, y+h-r);
    context.lineTo(x, y+r);
    context.quadraticCurveTo(x, y, x+r, y);
    context.closePath();
    context.fill();
    //context.stroke(); // border if wanted
    
}

// selects a label and updates GUI accordingly
function select(label) {

    if (selectedLabel !== null) {
        selectedLabel.getObjectByName('sprite').material.color.setHex(0xFFFFFF);
        selectedLabel.getObjectByName('pin').material = pinMaterial;
    }
    
    if (label === null || selectedLabel === label) {
        selectedLabel = null;
        textBoxContent.innerHTML = strings['selectLabel'];
        textBox.style.top = container.offsetTop + height - textBox.offsetHeight
                - 5 + "px";
    } else {
        selectedLabel = label;
        selectedLabel.getObjectByName('sprite').material.color.setHex(0xFF3030);
        selectedLabel.getObjectByName('pin').material = pinSelectedMaterial;
        
        if (editable) {
            // setup editable fields with listeners
            textBoxContent.innerHTML = '';
            var titleInput = document.createElement('INPUT');
            titleInput.setAttribute('type', 'text');
            titleInput.value = label.title;
            titleInput.style.width = '98%';
            titleInput.style.paddingBottom = '3px';
            
            // title input
            titleInput.oninput = function(){
                label.needsUpdate = true;
                label.title = this.value;
                var sprite = label.getObjectByName('sprite');
                label.remove(sprite);
                // dispose of resources
                // would be best to dispose of texture too, but it should be ok
                sprite.geometry.dispose();
                // remake sprite with new text
                sprite = makeSprite(this.value, label.labelPosition);
                sprite.material.color.setHex(0xFF3030);
                label.add(sprite);
                // update view
                render();
            };
            textBoxContent.appendChild(titleInput);
            
            // text input
            var textInput = document.createElement('TEXTAREA');
            // replace br with blank (I expected newline but it's already there?)
            textInput.value = label.text.replace(/<br \/>/g, '');
            textInput.style.width = '98%';
            textInput.rows = 5;
            textInput.oninput = function(){
                label.needsUpdate = true;
                label.text = this.value;
            };
            textBoxContent.appendChild(textInput);
            
            // delete button
            var delButton = document.createElement("button");
            delButton.innerHTML = strings['deleteLabel'];
            delButton.style.cssFloat = 'right';
            delButton.onclick = function(){ 
                if (!confirm(strings['confirmDelete'])) return;
                select(null);
                label.isDeleted = true;
                label.needsUpdate = true;
                label.visible = false;
                // if it was created and deleted, just toss it altogether
                if (label.isNew) {
                    labelHolder.remove(label);
                }
                // update view
                render();
            };
            textBoxContent.appendChild(delButton);
            
            // move invisible billboard to sprite position
            invisiboard.position.copy(label.getObjectByName('sprite').position);
            
        } else {
            // if not editable, fill in static content
            textBoxContent.innerHTML = '';
            var hTitle = document.createElement('h3');
            hTitle.innerHTML = label.title;
            hTitle.style.marginTop = '0px';
            textBoxContent.appendChild(hTitle);
            var pText = document.createElement('p');
            pText.innerHTML = label.text;
            textBoxContent.appendChild(pText);
        }
        
        textBox.style.top = container.offsetTop + height - textBox.offsetHeight
                    - 5 + "px";
    }
}

function switchMouseMode(mode) {
    switch (mode) {
        case 'view':
            mouseMode = 'view';
            controls.enabled = true;
            controls.noRotate = false;
            newMark.visible = false;
            newLabelButton.style.backgroundColor = '';
            break;
        case 'moveSprite':
            mouseMode = 'moveSprite';
            controls.enabled = true;
            controls.noRotate = true;
            newMark.visible = false;
            newLabelButton.style.backgroundColor = '';
            break;
        case 'addLabel':
            mouseMode = 'addLabel';
            controls.enabled = false;
            newMark.visible = true;
            newLabelButton.style.backgroundColor = 'rgba(255, 0, 0, 1.0)';
            invisiboard.position = new THREE.Vector3();
            break;
        default:
            mouseMode = mode;
            controls.enabled = true;
            controls.noRotate = false;
            newMark.visible = false;
            break;
    }
}

function onMouseDown(event) {
    var inters = getIntersects(event, labelHolder.children, true);
    // don't do anything if nothing was clicked
    if (inters.length === 0) {
        return;
    }
    // remember what was hit first, exclude "lines", they only mess things up
    var i = 0;
    while (i < inters.length) {
        if (inters[i].object.name === 'line') i++;
        else {
            lastIntersect = inters[i];
            break;
        }
    }
    lastIntersect = inters[0];
    // in edit mode, allow "dragging" while disabling controls
    if (editable && lastIntersect.object.name === 'sprite'
            && lastIntersect.object.parent === selectedLabel ) {
        switchMouseMode('moveSprite');
        // remember where we grabbed the sprite (offset from center)
        hitOffset = new THREE.Vector3()
                .subVectors(lastIntersect.object.position, lastIntersect.point); 
    }
}

function onMouseUp(event) {  
    var intersects = getIntersects(event, labelHolder.children, true);

    switch (mouseMode) {
        case 'view':
            intersects.forEach(function(intersect) {
                if (lastIntersect === null) return;
                if (intersect.object === lastIntersect.object) {    
                    var objName = intersect.object.name;
                    if (objName === 'sprite' | objName === 'pin' | objName === 'mark') {
                        select(intersect.object.parent);
                    }
                    render();
                }
            });
            break;
        case 'moveSprite':
            switchMouseMode('view');
            break;
        case 'addLabel':
            var inters = getIntersects(event, [model], false);
            // if model was hit, create default label at the position
            if (inters.length > 0) {            
                // get normal
                model.geometry.computeVertexNormals();
                var idx = inters[0].indices[0]; // index on one vertice
                var normal = new THREE.Vector3(
                        model.geometry.getAttribute('normal').array[3*idx],
                        model.geometry.getAttribute('normal').array[3*idx+1],
                        model.geometry.getAttribute('normal').array[3*idx+2]);
                // create label attribs
                var label = new Object();
                // mark at intersect
                label.markX = inters[0].point.x;
                label.markY = inters[0].point.y;
                label.markZ = inters[0].point.z;
                // label at intersect + normal (scaled)
                label.labelX = inters[0].point.x + normal.x * 20;
                label.labelY = inters[0].point.y + normal.y * 20;
                label.labelZ = inters[0].point.z + normal.z * 20;
                // default content
                label.title = strings['defaultTitle'];
                label.text = strings['defaultText'];
            }
            // create, add and select label
            var label3d = makeLabel(label, true);
            labelHolder.add(label3d);
            select(label3d);
            switchMouseMode('view');
            render();
    }
    
}

function onMouseMove(event) {
    
    switch (mouseMode) {
        case 'moveSprite':
            var sprite = selectedLabel.getObjectByName('sprite');
            // move sprite (todo: bigger "hitbox")
            sprite.position.copy(getIntersects(event, [invisiboard])[0].point, false)
                    .add(hitOffset);
            // move line (much easier to recreate, performance shouldn't matter much)
            var line = selectedLabel.getObjectByName('line');
            // remove old line
            selectedLabel.remove(line);
            line.geometry.dispose();
            // remake line with new position
            line = makeLine( selectedLabel.markPosition, sprite.position);
            selectedLabel.add(line);
            // update label attribs
            selectedLabel.needsUpdate = true;
            selectedLabel.labelPosition = sprite.position;
            render();
            break;
        case 'addLabel':
            var inters = getIntersects(event, [model], false);
            if (inters.length > 0) {
                newMark.material = markMaterial;
                newMark.position.copy(inters[0].point);
            } else {
                newMark.material = greyMaterial;
                inters = getIntersects(event, [invisiboard], false);
                if (inters.length > 0) {
                    newMark.position.copy(inters[0].point);
                }
            }
            render();
            break;
    }
}

// get intersects of mouse cursor (event) with objects
// filter out invisibles if set
function getIntersects(event, objects, filterInvisible) {
    var mouse = {x: 0, y: 0};
    
    mouse.x = ((event.clientX - renderer.domElement.getBoundingClientRect().left) / width) * 2 - 1;
    mouse.y = - ((event.clientY - renderer.domElement.getBoundingClientRect().top) / height) * 2 + 1;
    
    var vector = new THREE.Vector3(mouse.x, mouse.y, 1);
	projector.unprojectVector(vector, camera);
	var ray = new THREE.Raycaster(camera.position, vector.sub(camera.position).normalize());
    
    // get intersected objects
    var intersects = ray.intersectObjects(objects, true);
    // filter only visible ones (need to check ancestor visibility too!)
    if (filterInvisible) {    
        var visibles = [];
        var visible;
        var ancestor;
        for (var i = 0; i < intersects.length; i++) {
            visible = true;
            ancestor = intersects[i].object;
            while (ancestor !== null & typeof ancestor !== 'undefined') {
                visible = ancestor.visible & visible;
                ancestor = ancestor.parent;
            }
            if (visible) {
                visibles.push(intersects[i]);
            }
        }
        return visibles;
    } else {
        // return all if not filtered
        return intersects;
    }
}

function onWindowResize() {

    // set dimensions to container size
    width = container.offsetWidth;
    // prefer fixed ratio
    height = 0.75 * width;
    // but fit to window if it would overflow / cause scrolling
    width = Math.min(width, window.innerWidth);
    height = Math.min(height, window.innerHeight);
    // set renderer (canvas) size
    renderer.setSize(width, height);
    // fix camera aspect
    camera.aspect = width / height;
    camera.updateProjectionMatrix();
    
    // textbox
    var w = Math.min(400, width);
    var h = Math.min(200, height*0.4);
    textBox.style.width = w + 'px';
    textBox.style.maxWidth = width + 'px';
    textBox.style.maxHeight = h - 15 + 'px';
    textBox.style.top = container.offsetTop + height - textBox.offsetHeight
                - 5 + 'px';
    textBox.style.left = container.offsetLeft +
            0.5*( width - Math.min(w, width)) + 'px';
    
    // select box
    var msWidth = 100;
    modeSelect.style.width = msWidth + 'px';
    modeSelect.style.top = container.offsetTop + 5 + 'px';
    modeSelect.style.left = container.offsetLeft + width - msWidth - 5 + 'px';
    
    // new label button
    newLabelButton.style.width = 80 + 'px';
    newLabelButton.style.top = container.offsetTop + height
            - newLabelButton.offsetHeight - 5 + 'px';
    newLabelButton.style.left = container.offsetLeft + 5 + 'px';
    
    // save button
    saveButton.style.width = 80 + 'px';
    saveButton.style.top = container.offsetTop + height
            - saveButton.offsetHeight - 5 + 'px';
    saveButton.style.left = container.offsetLeft + width
            - saveButton.offsetWidth - 5 + 'px';
    
    // loading text
    loading.style.left = container.offsetLeft + 0.5*(width-loading.offsetWidth) + 'px';
    loading.style.top = container.offsetTop + 0.5*(height-loading.offsetHeight) + 'px';
    
    render();
}

// render function
// everything that happens every frame goes here
function render() {
    // move point light with camera
    var lightShift = new THREE.Vector3();
    lightShift.copy(lightOffset); // value of offset
    // light position: apply camera transform to offset, add to camera position
    pointLight.position.addVectors(
            lightShift.applyQuaternion(camera.quaternion), camera.position);
    // rotate all sprites to face camera
    for(var i = 0; i < labelHolder.children.length; i++) {
        labelHolder.children[i].getObjectByName('sprite').quaternion.copy(camera.quaternion);
    }
    invisiboard.quaternion.copy(camera.quaternion);
    
    // draw scene
    renderer.render(scene, camera);
}